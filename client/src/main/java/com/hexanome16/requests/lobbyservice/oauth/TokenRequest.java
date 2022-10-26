package com.hexanome16.requests.lobbyservice.oauth;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;
import com.hexanome16.types.lobby.auth.TokensInfo;
import com.hexanome16.utils.AuthHeader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class is responsible for sending the request to Lobby Service to get the OAuth tokens.
 */
public class TokenRequest {

    /**
     * Sends the request to the Lobby Service to get the OAuth tokens.
     *
     * @param username      The username (can be empty if refresh_token is not empty).
     * @param password      The password (can be empty if refresh_token is not empty).
     * @param refresh_token The refresh token (can be empty if logging in with username/password).
     * @return The response from the Lobby Service (see <a href="https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/api.md#authentication">Lobby Service docs</a>).
     */
    public static TokensInfo execute(String username, String password, String refresh_token) {
        HttpClient client = RequestClient.getClient();
        try {
            StringBuilder url = new StringBuilder("http://localhost:4242/oauth/token?");
            if (refresh_token == null || refresh_token.isBlank()) {
                url.append("grant_type=password&username=").append(username).append("&password=").append(password);
            } else {
                url.append("grant_type=refresh_token&refresh_token=").append(refresh_token);
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url.toString()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", AuthHeader.getBasicHeader("bgp-client-name", "bgp-client-pw"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
            return new Gson().fromJson(response, TokensInfo.class);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        TokensInfo response = TokenRequest.execute("testservice", "testpass", null);
        System.out.println(response);
    }
}
