package com.hexanome16.requests.lobbyservice.user;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ChangePasswordRequest {
    private static class Payload {
        String oldPassword;
        String nextPassword;

        public Payload(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.nextPassword = newPassword;
        }
    }

    public static void execute(String access_token, String user, String oldPassword, String newPassword) {
        HttpClient client = RequestClient.getClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:4242/api/users/" + user + "?access_token=" + access_token))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Payload(oldPassword, newPassword))))
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
