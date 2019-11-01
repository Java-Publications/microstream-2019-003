package junit.org.rapidpm.publication.microstream.m01;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.rapidpm.publication.microstream.m01.HelloWorldImmutable;

import java.io.File;

import static junit.org.rapidpm.publication.microstream.StorageEngineUtils.infoToCleanFolder;

public class HelloWorldImmutableTest {

  @Test
  void test001(TestInfo info) {
    final HelloWorldImmutable    value           = new HelloWorldImmutable("HelloWorldImmutable");

    final File tempFolder = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
    storageManagerA.setRoot(value);
    storageManagerA.storeRoot();
    storageManagerA.shutdown();

    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final Object                 root           = storageManagerB.root();
    Assertions.assertTrue(root instanceof HelloWorldImmutable);
    HelloWorldImmutable helloWorld = (HelloWorldImmutable) root;
    Assertions.assertEquals("HelloWorldImmutable", helloWorld.getValue());
    storageManagerB.shutdown();
  }
}
