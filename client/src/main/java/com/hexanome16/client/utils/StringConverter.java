package com.hexanome16.client.utils;

/**
 * This class provides methods to escape strings to put in request URLs.
 */
public class StringConverter {
  public static String escape(String str) {
    return str == null ? "" : str.replace("+", "%2B").replace("*", "%2A");
  }
}
