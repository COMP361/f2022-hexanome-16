package com.hexanome16.types.lobby.sessions;

import java.util.Objects;

public record GameParams(String location, int maxSessionPlayers, int minSessionPlayers, String name,
                         String webSupport) {

    @Override
    public String toString() {
        return "GameParams{" +
                "location='" + location + '\'' +
                ", maxSessionPlayers=" + maxSessionPlayers +
                ", minSessionPlayers=" + minSessionPlayers +
                ", name='" + name + '\'' +
                ", webSupport='" + webSupport + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameParams that = (GameParams) o;
        return maxSessionPlayers() == that.maxSessionPlayers() && minSessionPlayers() == that.minSessionPlayers() && location().equals(that.location()) && name().equals(that.name()) && webSupport().equals(that.webSupport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(location(), maxSessionPlayers(), minSessionPlayers(), name(), webSupport());
    }
}
