package com.hexanome16.types.lobby.user;

public record User(String name, String password, String preferredColour, String role) {

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", preferredColour='" + preferredColour + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
