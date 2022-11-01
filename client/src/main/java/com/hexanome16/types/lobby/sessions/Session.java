package com.hexanome16.types.lobby.sessions;

import java.util.Arrays;
import java.util.Objects;

public final class Session {
    private Long id;
    private String creator;
    private GameParams gameParameters;
    private boolean launched;
    private String[] players;
    private String savegameid;

    public Session(String creator, GameParams gameParameters, boolean launched, String[] players,
                   String savegameid) {
        this.creator = creator;
        this.gameParameters = gameParameters;
        this.launched = launched;
        this.players = players;
        this.savegameid = savegameid;
    }

    public String toString() {
        return "Session{" +
                "creator='" + creator + '\'' +
                ", gameParameters=" + gameParameters +
                ", launched=" + (launched ? "yes" : "no") +
                ", players=" + Arrays.toString(players) +
                ", savegameid='" + savegameid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return launched() == session.launched() && creator().equals(session.creator()) && gameParameters().equals(session.gameParameters()) && Arrays.equals(players(), session.players()) && Objects.equals(savegameid(), session.savegameid());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(creator(), gameParameters(), launched(), savegameid());
        result = 31 * result + Arrays.hashCode(players());
        return result;
    }

    public String creator() {
        return creator;
    }

    public GameParams gameParameters() {
        return gameParameters;
    }

    public boolean launched() {
        return launched;
    }

    public String[] players() {
        return players;
    }

    public String savegameid() {
        return savegameid;
    }

    public Long id() {
        return id;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setGameParameters(GameParams gameParameters) {
        this.gameParameters = gameParameters;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    public void setSavegameid(String savegameid) {
        this.savegameid = savegameid;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
