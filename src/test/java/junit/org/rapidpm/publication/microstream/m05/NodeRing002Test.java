package junit.org.rapidpm.publication.microstream.m05;

import one.microstream.collections.types.XSequence;
import one.microstream.storage.types.*;
import one.microstream.util.cql.CQL;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.publication.microstream.m04.Node;

import java.io.File;
import java.time.Instant;
import java.util.stream.StreamSupport;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static junit.org.rapidpm.publication.microstream.StorageEngineUtils.*;

public class NodeRing002Test
    implements HasLogger {

  @Test
  void test001(TestInfo info, TestReporter reporter) {
    final Node node01 = createRingOfNodes();

    final File                   tempFolder     = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManager = EmbeddedStorage.start(tempFolder);
    writeData(reporter, storageManager, node01);

    //time to make backup
    XSequence<File> exportFiles = exportData(info, storageManager);
    exportFiles.forEach(f -> logger().info("exported file " + f.getName()));

    //clean data folder
    recreateTempFolder(tempFolder);
    File targetDirectory = infoToCleanExportFolder("export_csv").apply(info);
    StreamSupport.stream(exportFiles.spliterator(), false)
                 .forEach(f -> exportDataAsCSV(storageManager, targetDirectory, f));

    storageManager.shutdown();
  }

  @NotNull
  private void writeData(TestReporter reporter, EmbeddedStorageManager storageManager, Node node) {
    final Instant start = now();
    storageManager.setRoot(node);
    storageManager.storeRoot();
    final Instant stop = now();
    reporter.publishEntry("duration store [ms] " + between(start, stop).toMillis());
  }

  @NotNull
  private Node createRingOfNodes() {
    final Node node01 = new Node(1);
    final Node node02 = new Node(2);
    final Node node03 = new Node(3);
    final Node node04 = new Node(4);

    node01.setLeft(node02);
    node02.setLeft(node03);
    node03.setLeft(node04);

    node04.setLeft(node01);
    return node01;
  }

  private XSequence<File> exportData(TestInfo info, EmbeddedStorageManager storageManagerA) {
    File targetDirectory = infoToCleanExportFolder("export_bin").apply(info);

    StorageConnection connection = storageManagerA.createConnection();
    StorageEntityTypeExportStatistics exportResult = connection.exportTypes(
        new StorageEntityTypeExportFileProvider.Default(targetDirectory, "bin"),
        typeHandler -> true);
    // export all, customize if necessary
    return CQL.from(exportResult.typeStatistics()
                                .values())
              .project(s -> new File(s.file()
                                      .identifier()))
              .execute();
  }

  private void exportDataAsCSV(EmbeddedStorageManager storageManager, File targetDirectory, File typeFile) {
    StorageDataConverterTypeBinaryToCsv converter = new StorageDataConverterTypeBinaryToCsv.UTF8(
        StorageDataConverterCsvConfiguration.defaultConfiguration(),
        new StorageEntityTypeConversionFileProvider.Default(targetDirectory, "csv"), storageManager.typeDictionary(),
        null,         // no type name mapping
        4096, // read buffer size
        4096  // write buffer size
    );
    StorageLockedFile storageFile = StorageLockedFile.openLockedFile(typeFile);
    try {
      converter.convertDataFile(storageFile);
    } finally {
      storageFile.close();
    }
  }
}
