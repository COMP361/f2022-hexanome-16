package com.hexanome16.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;
import com.hexanome16.types.lobby.sessions.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ListSessionsRequest {
    private static class Response {
        public Map<String, Session> sessions;
    }

    public static Map<String, Session> execute(int hash) {
        HttpClient client = RequestClient.getClient();
        try {
            String url = "http://127.0.0.1:4242/api/sessions";
            if (hash > 0) {
                url += "?hash=" + hash;
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();
            return new Gson().fromJson(response, Response.class).sessions;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}