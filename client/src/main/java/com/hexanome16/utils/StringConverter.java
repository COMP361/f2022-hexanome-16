package com.hexanome16.utils;

public class StringConverter {
    //taken from https://stackoverflow.com/a/66432094
    public static String escape(String str) {
        return str.replace("+", "%2B").replace("*", "%2A");
    }
}
