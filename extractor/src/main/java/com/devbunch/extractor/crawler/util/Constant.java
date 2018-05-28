package com.devbunch.extractor.crawler.util;

import java.nio.charset.Charset;

/**
 * Utility methods.
 */
public final class Constant {

  private static final String UTF_8 = "UTF-8";
  private static final String ISO_8859_1 = "ISO-8859-1";
  public static final Charset UTF8_CHARSET = Charset.forName(UTF_8);
  public static final Charset LATIN1_CHARSET = Charset.forName(ISO_8859_1);
  public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

}
