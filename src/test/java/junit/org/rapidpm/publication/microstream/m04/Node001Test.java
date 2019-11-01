package junit.org.rapidpm.publication.microstream.m04;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.rapidpm.publication.microstream.m04.BinaryTree;

import java.io.File;
import java.time.Instant;
import java.util.Random;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static junit.org.rapidpm.publication.microstream.StorageEngineUtils.infoToCleanFolder;

public class Node001Test {


  @Test
  void test001(TestInfo info, TestReporter reporter) {
    final File                   tempFolder      = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);

    final BinaryTree tree = new BinaryTree();
    new Random().ints()
                .limit(1_000)
                .forEach(tree::add);

    final Instant start = now();
    storageManagerA.setRoot(tree);
    storageManagerA.storeRoot();
    final Instant stop = now();
    reporter.publishEntry("duration store [ms] " + between(start, stop).toMillis());
    storageManagerA.shutdown();

    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final BinaryTree             rootAgain       = (BinaryTree) storageManagerB.root();
    Assertions.assertEquals(tree.traverseLevelOrder(), rootAgain.traverseLevelOrder());
    storageManagerB.shutdown();
  }
}
