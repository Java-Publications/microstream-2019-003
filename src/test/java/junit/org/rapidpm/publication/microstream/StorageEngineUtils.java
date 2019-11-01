package junit.org.rapidpm.publication.microstream;

import org.junit.jupiter.api.TestInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.function.Function;

import static java.nio.file.Files.walk;
import static java.util.Comparator.reverseOrder;

public interface StorageEngineUtils {

  String TARGET_PATH = "./target/storage/";

  static Function<TestInfo, File> infoToCleanFolder() {
    return (info) -> {
      final Class<?> aClass     = info.getTestClass()
                                      .get();
      final Method   method     = info.getTestMethod()
                                      .get();
      final File     tempFolder = new File(TARGET_PATH, aClass.getSimpleName() + "_" + method.getName());
      recreateTempFolder(tempFolder);
      return tempFolder;
    };
  }

  static void recreateTempFolder(File tempFolder) {
    if (tempFolder.exists()) {
      try {
        walk(tempFolder.toPath()).sorted(reverseOrder())
                                 .map(Path::toFile)
                                 .forEach(File::delete);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    tempFolder.mkdirs();
  }

  static Function<TestInfo, File> infoToCleanExportFolder(String postFix) {
    return (info) -> {
      final Class<?> aClass = info.getTestClass()
                                  .get();
      final Method method = info.getTestMethod()
                                .get();
      final File tempFolder = new File(TARGET_PATH, aClass.getSimpleName() + "_" + method.getName() + "_" + postFix);
      recreateTempFolder(tempFolder);
      return tempFolder;
    };
  }


}
