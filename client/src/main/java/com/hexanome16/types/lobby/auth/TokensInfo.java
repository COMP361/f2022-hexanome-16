package com.hexanome16.types.lobby.auth;

/**
 * The response from the Lobby Service (see <a href="https://github.com/kartoffelquadrat/LobbyService/blob/master/markdown/api.md#authentication">Lobby Service docs</a>).
 */
public record TokensInfo(String access_token, String refresh_token, String token_type, int expires_in, String scope) {

    @Override
    public String toString() {
        return "TokensInfo{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                ", scope='" + scope + '\'' +
                '}';
    }
}
