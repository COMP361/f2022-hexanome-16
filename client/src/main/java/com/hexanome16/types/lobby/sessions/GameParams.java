package com.hexanome16.types.lobby.sessions;

import java.util.Objects;

public final class GameParams {
    private String location;
    private int maxSessionPlayers;
    private int minSessionPlayers;
    private String name;
    private String displayName;
    private String webSupport;

    public GameParams(String location, int maxSessionPlayers, int minSessionPlayers, String name, String displayName,
                      String webSupport) {
        this.location = location;
        this.maxSessionPlayers = maxSessionPlayers;
        this.minSessionPlayers = minSessionPlayers;
        this.name = name;
        this.displayName = displayName;
        this.webSupport = webSupport;
    }

    @Override
    public String toString() {
        return "GameParams{" +
                "location='" + location + '\'' +
                ", maxSessionPlayers=" + maxSessionPlayers +
                ", minSessionPlayers=" + minSessionPlayers +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
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

    public String location() {
        return location;
    }

    public int maxSessionPlayers() {
        return maxSessionPlayers;
    }

    public int minSessionPlayers() {
        return minSessionPlayers;
    }

    public String name() {
        return name;
    }

    public String displayName() {
        return displayName;
    }

    public String webSupport() {
        return webSupport;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxSessionPlayers(int maxSessionPlayers) {
        this.maxSessionPlayers = maxSessionPlayers;
    }

    public void setMinSessionPlayers(int minSessionPlayers) {
        this.minSessionPlayers = minSessionPlayers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setWebSupport(String webSupport) {
        this.webSupport = webSupport;
    }
}
