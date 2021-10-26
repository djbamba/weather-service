package com.github.djbamba.service.weather.test.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFileReader {
  private static final Path TEST_RESOURCE_PATH = Paths.get("src", "test", "resources");

  private TestFileReader() {
    /*no hablo instance*/
  }

  public static File readResourceFile(Path testFilePath) {
    return TEST_RESOURCE_PATH.resolve(testFilePath).toFile();
  }

  public static String readResourceFileAsString(Path testFilePath) {
    try {
      return String.join("\n", Files.readAllLines(TEST_RESOURCE_PATH.resolve(testFilePath)));
    } catch (IOException e) {
      log.error("Error Reading Test File: {}", testFilePath, e);
    }
    return "ERR";
  }
}
