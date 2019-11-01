package junit.org.rapidpm.publication.microstream.m03;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.rapidpm.publication.microstream.m01.HelloWorldImmutable;
import org.rapidpm.publication.microstream.m03.CollectionRootNode;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;
import static junit.org.rapidpm.publication.microstream.StorageEngineUtils.infoToCleanFolder;

public class ListOfElementsTest {


  @Test
  void test001(TestInfo info, TestReporter reporter) {
    final File                   tempFolder      = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);

    List<HelloWorldImmutable> elements = IntStream.range(0, 100_000)
                                                  .mapToObj(i -> new HelloWorldImmutable(i + ""))
                                                  .collect(toList());

    final Instant start = now();
    storageManagerA.setRoot(new CollectionRootNode(elements));
    storageManagerA.storeRoot();
    final Instant stop = now();
    reporter.publishEntry("duration store [ms] " + between(start,stop).toMillis());

    storageManagerA.shutdown();

    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final CollectionRootNode     rootAgain       = (CollectionRootNode) storageManagerB.root();

    Assertions.assertEquals(elements, rootAgain.getElements());

    storageManagerB.shutdown();
  }
}
