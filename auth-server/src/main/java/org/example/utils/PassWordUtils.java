package org.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

public class PassWordUtils {

  /**
   * Generate salt required for password encryption
   * @return salt
   * @throws NoSuchProviderException no such provider
   * @throws NoSuchAlgorithmException no such algorithm
   */
  public static String generateSalt() throws NoSuchProviderException, NoSuchAlgorithmException {
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);

    return Base64.getEncoder().encodeToString(salt);
  }

  /**
   * Encrypt the original password with a salt
   * @param password original password
   * @param salt salt
   * @return encrypted password
   * @throws NoSuchAlgorithmException no such algorithm
   */
  public static String encodePassword(String password, String salt)
          throws NoSuchAlgorithmException {
    byte[] saltData = Base64.getDecoder().decode(salt);

    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(saltData);
    byte[] data = md.digest(password.getBytes());
    return Base64.getEncoder().encodeToString(data);
  }
}
