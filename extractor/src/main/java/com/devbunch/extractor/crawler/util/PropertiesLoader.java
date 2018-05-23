package com.devbunch.extractor.crawler.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

  public static Properties load(String fileName) {

    Properties prop = new Properties();
    InputStream im = null;
    try {
      im = findFile(fileName);
      prop.load(im);
    } catch (IOException ignore) {
    } finally {
      if (im != null) {
        try {
          im.close();
        } catch (IOException ignore) {
        }
      }
    }
    return prop;
  }

  private static InputStream findFile(String fileName) throws FileNotFoundException {
    InputStream im = findInWorkingDirectory(fileName);
    if (im == null)
      im = findInClasspath(fileName);
    if (im == null)
      im = findInSourceDirectory(fileName);
    if (im == null)
      throw new FileNotFoundException(String.format("File %s not found", fileName));
    return im;
  }

  private static InputStream findInSourceDirectory(String fileName) throws FileNotFoundException {
    return new FileInputStream("src/main/resources/" + fileName);
  }

  private static InputStream findInClasspath(String fileName) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
  }

  private static InputStream findInWorkingDirectory(String fileName) {
    try {
      return new FileInputStream(System.getProperty("user.dir") + fileName);
    } catch (FileNotFoundException e) {
      return null;
    }
  }
}
