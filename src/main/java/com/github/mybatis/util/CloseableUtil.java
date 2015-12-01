package com.github.mybatis.util;

/**
 * 工具类
 * Created by kevin on 2015-12-18.
 */
public class CloseableUtil {
  private CloseableUtil() {
  }

  public static void closeQuietly(AutoCloseable c) {
    if (c != null) {
      try {
        c.close();
      } catch (Exception ignored) {
      }
    }
  }
}
