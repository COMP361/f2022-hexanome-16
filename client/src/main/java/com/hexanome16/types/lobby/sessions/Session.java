package com.hexanome16.types.lobby.sessions;

import java.util.Arrays;
import java.util.Objects;

public record Session(String creator, GameParams gameParameters, boolean launched, String[] players,
                      String savegameid) {

    public String toString() {
        return "Session{" +
                "creator='" + creator + '\'' +
                ", gameParameters=" + gameParameters +
                ", launched=" + launched +
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
}
