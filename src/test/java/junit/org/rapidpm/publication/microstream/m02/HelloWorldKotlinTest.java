package junit.org.rapidpm.publication.microstream.m02;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.publication.microstream.m01.HelloWorld;
import org.rapidpm.publication.microstream.m02.HelloWorldImmutableKotlin;
import org.rapidpm.publication.microstream.m02.HelloWorldKotlin;
import org.rapidpm.publication.microstream.m02.HelloWorldKotlinDataclass;

import java.io.File;

import static junit.org.rapidpm.publication.microstream.StorageEngineUtils.infoToCleanFolder;

public class HelloWorldKotlinTest
    implements HasLogger {

  @Test
  void test001(TestInfo info) {
    final HelloWorldKotlin value = new HelloWorldKotlin();
    value.setValue("HelloWorldKotlin");
    final File                   tempFolder      = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
    storageManagerA.setRoot(value);
    storageManagerA.storeRoot();
    storageManagerA.shutdown();


    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final Object                 root           = storageManagerB.root();
    Assertions.assertTrue(root instanceof HelloWorldKotlin);
    HelloWorldKotlin helloWorld = (HelloWorldKotlin) root;
    Assertions.assertEquals("HelloWorldKotlin", helloWorld.getValue());
    storageManagerB.shutdown();
  }

  @Test
  void test011(TestInfo info) {
    final HelloWorldImmutableKotlin value = new HelloWorldImmutableKotlin("HelloWorldImmutableKotlin");
    final File tempFolder = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
    storageManagerA.setRoot(value);
    storageManagerA.storeRoot();
    storageManagerA.shutdown();

    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final Object                 root           = storageManagerB.root();
    Assertions.assertTrue(root instanceof HelloWorldImmutableKotlin);
    HelloWorldImmutableKotlin helloWorld = (HelloWorldImmutableKotlin) root;
    Assertions.assertEquals("HelloWorldImmutableKotlin", helloWorld.getValue());
    storageManagerB.shutdown();
  }

  @Test
  void test021(TestInfo info) {
    final HelloWorldKotlinDataclass value          = new HelloWorldKotlinDataclass("HelloWorldKotlinDataclass");
    final File tempFolder = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
    storageManagerA.setRoot(value);
    storageManagerA.storeRoot();
    storageManagerA.shutdown();


    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final Object                 root           = storageManagerB.root();
    Assertions.assertTrue(root instanceof HelloWorldKotlinDataclass);
    HelloWorldKotlinDataclass helloWorld = (HelloWorldKotlinDataclass) root;
    Assertions.assertEquals("HelloWorldKotlinDataclass", helloWorld.getValue());
    storageManagerB.shutdown();
  }
}
