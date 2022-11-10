package com.hexanome16.utils;

import java.util.Base64;

/**
 * This class provides methods to create authorization headers for requests.
 */
public class AuthHeader {
  public static String getBearerHeader(String accessToken) {
    return "Bearer " + accessToken;
  }

  public static String getBasicHeader(String username, String password) {
    return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
  }
}
