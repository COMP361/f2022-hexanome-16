package com.hexanome16.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreateSessionRequest {
    private static class Payload {
        String creator;
        String game;
        String savegame;

        public Payload(String creator, String game, String savegame) {
            this.creator = creator;
            this.game = game;
            this.savegame = savegame;
        }
    }

    public static String execute(String access_token, String creator, String game, String savegame) {
        if (savegame == null) {
            savegame = "";
        }
        HttpClient client = RequestClient.getClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:4242/api/sessions?access_token=" + access_token))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Payload(creator, game, savegame))))
                    .build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }
}
