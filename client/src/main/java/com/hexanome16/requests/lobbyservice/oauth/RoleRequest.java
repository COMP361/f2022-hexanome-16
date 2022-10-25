package com.hexanome16.requests.lobbyservice.oauth;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class RoleRequest {
    private static class Response {
        String authority;
    }

    public static String execute(String access_token) {
        String url = "http://localhost:4242/oauth/role?access_token=" + access_token;
        HttpClient client = RequestClient.getClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
            return new Gson().fromJson(response, Response[].class)[0].authority;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(RoleRequest.execute("CtOq1Zt62iOclaikOcXU7zdfuV8="));
    }
}
