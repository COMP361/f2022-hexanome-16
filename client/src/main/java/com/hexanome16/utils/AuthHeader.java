package com.hexanome16.utils;

import java.util.Base64;

public class AuthHeader {
    public static String getBearerHeader(String access_token) {
        return "Bearer " + access_token;
    }

    public static String getBasicHeader(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
