package org.example.utils;

import cn.hutool.core.util.RandomUtil;
import org.example.data.DataStore;

public class TokenUtils {

  /**
   * generate unique token for each Authenticate
   *
   * @return unique token
   */
  public static String generateToken() {
    // token = timestamp + incrementId + threeRandomNumbers
    return System.currentTimeMillis()
        + "_"
        + DataStore.getIncrementTokenId()
        + "_"
        + RandomUtil.randomInt(100, 999);
  }
}
