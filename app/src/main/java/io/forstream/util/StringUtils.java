package io.forstream.util;

public class StringUtils {

  private StringUtils() {

  }

  public static boolean isEmpty(String value) {
    return value == null || value.isEmpty();
  }
}
