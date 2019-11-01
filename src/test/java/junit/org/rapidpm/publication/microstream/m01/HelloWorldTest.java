package junit.org.rapidpm.publication.microstream.m01;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.publication.microstream.m01.HelloWorld;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.function.Function;

import static java.nio.file.Files.walk;
import static java.util.Comparator.reverseOrder;
import static junit.org.rapidpm.publication.microstream.StorageEngineUtils.infoToCleanFolder;

public class HelloWorldTest
    implements HasLogger {




  @Test
  void test001(TestInfo info) {
    final HelloWorld value = new HelloWorld();
    value.setValue("HelloWorld");
    final File                   tempFolder      = infoToCleanFolder().apply(info);
    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
    storageManagerA.setRoot(value);
    storageManagerA.storeRoot();
    storageManagerA.shutdown();

    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final Object                 root            = storageManagerB.root();
    Assertions.assertTrue(root instanceof HelloWorld);
    HelloWorld helloWorld = (HelloWorld) root;
    Assertions.assertEquals("HelloWorld", helloWorld.getValue());
    storageManagerB.shutdown();
  }

  @Test
  void test002(TestInfo info) {
    final HelloWorld value = new HelloWorld();
    value.setValue("HelloWorld");

    final File tempFolder = infoToCleanFolder().apply(info);

    final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
    storageManagerA.setRoot(value);
    storageManagerA.storeRoot();
    storageManagerA.shutdown();

    final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
    final Object                 root            = storageManagerB.root();
    Assertions.assertTrue(root instanceof HelloWorld);
    HelloWorld helloWorld = (HelloWorld) root;
    Assertions.assertEquals("HelloWorld", helloWorld.getValue());
    storageManagerB.shutdown();
  }


}
