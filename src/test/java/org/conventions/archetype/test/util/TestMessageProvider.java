package org.conventions.archetype.test.util;

import java.io.IOException;

public class TestMessageProvider {

  private static TestResourceBundle currentBundle;

  public static String getMessage(String key) {
    return getCurrentBundle().getString(key);
  }

  private static TestResourceBundle getCurrentBundle() {
    if (currentBundle == null) {
      try {
        currentBundle = new TestResourceBundle();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return currentBundle;
  }

}
