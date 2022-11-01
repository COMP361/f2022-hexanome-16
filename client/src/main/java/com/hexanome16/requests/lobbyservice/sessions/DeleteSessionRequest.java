package com.hexanome16.requests.lobbyservice.sessions;

import com.hexanome16.requests.RequestClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class DeleteSessionRequest {
    public static void execute(long sessionId, String access_token) {
        HttpClient client = RequestClient.getClient();
        try {
            String url = "http://127.0.0.1:4242/api/sessions/" + sessionId + "?access_token=" + access_token;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.discarding()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
