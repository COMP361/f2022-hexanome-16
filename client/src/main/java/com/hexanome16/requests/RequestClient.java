package com.hexanome16.requests;

import java.net.http.HttpClient;
import java.time.Duration;

public class RequestClient {
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static HttpClient getClient() {
        return client;
    }
}
