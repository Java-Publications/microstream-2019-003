package junit.org.rapidpm.publication.microstream.m05;

import one.microstream.X;
import one.microstream.collections.types.XSequence;
import one.microstream.storage.types.*;
import one.microstream.util.cql.CQL;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
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
import static java.util.stream.Collectors.toList;
import static junit.org.rapidpm.publication.microstream.StorageEngineUtils.*;

public class NodeRing002Test
    implements HasLogger {

  @Test
  void test001(TestInfo info, TestReporter reporter) {
    final Node node01 = createRingOfNodes();

    final File      tempFolder  = infoToCleanFolder().apply(info);
    XSequence<File> exportFiles = writeData(info, reporter, node01, tempFolder);
    //clean data folder
    recreateTempFolder(tempFolder);

    final EmbeddedStorageManager storageManager = importData(info, exportFiles);
    final Node                   node01Again    = (Node) storageManager.root();

    final Node node02Again = node01Again.getLeft();
    final Node node03Again = node02Again.getLeft();
    final Node node04Again = node03Again.getLeft();

    Assertions.assertEquals(1, node01Again.getId());
    Assertions.assertEquals(2, node02Again.getId());
    Assertions.assertEquals(3, node03Again.getId());
    Assertions.assertEquals(4, node04Again.getId());

    Assertions.assertEquals(1, node04Again.getLeft()
                                          .getId());

    storageManager.shutdown();
  }

  @NotNull
  private XSequence<File> writeData(TestInfo info, TestReporter reporter, Node node01, File tempFolder) {
    final EmbeddedStorageManager storageManager = EmbeddedStorage.start(tempFolder);

    final Instant start = now();
    storageManager.setRoot(node01);
    storageManager.storeRoot();
    final Instant stop = now();
    reporter.publishEntry("duration store [ms] " + between(start, stop).toMillis());

    //time to make backup
    XSequence<File> exportFiles = exportData(info, storageManager);
    exportFiles.forEach(f -> logger().info("exported file " + f.getName()));
    storageManager.shutdown();
    return exportFiles;
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

  @NotNull
  private EmbeddedStorageManager importData(TestInfo info, XSequence<File> exportFiles) {
    final File                   tempFolder      = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
    StorageConnection            connection      = storageManagerA.createConnection();


    final File[] files = StreamSupport.stream(exportFiles.spliterator(), false)
                                      .collect(toList())
                                      .toArray(File[]::new);
    connection.importFiles(X.Enum(files));
    return storageManagerA;
  }

  private XSequence<File> exportData(TestInfo info, EmbeddedStorageManager storageManagerA) {
    File targetDirectory = infoToCleanExportFolder().apply(info);

    String            fileSuffix = "bin";
    StorageConnection connection = storageManagerA.createConnection();
    StorageEntityTypeExportStatistics exportResult = connection.exportTypes(
        new StorageEntityTypeExportFileProvider.Default(targetDirectory, fileSuffix), typeHandler -> true);
    // export all, customize if necessary
    return CQL.from(exportResult.typeStatistics()
                                .values())
              .project(s -> new File(s.file()
                                      .identifier()))
              .execute();
  }
}
