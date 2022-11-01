package com.hexanome16.utils;

/**
 * This class provides methods to escape strings to put in request URLs.
 */
public class StringConverter {
  public static String escape(String str) {
    return str.replace("+", "%2B").replace("*", "%2A");
  }
}
