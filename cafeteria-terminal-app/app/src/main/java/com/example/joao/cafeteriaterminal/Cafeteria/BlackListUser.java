package com.example.joao.cafeteriaterminal.Cafeteria;

import java.util.UUID;

public class BlackListUser {
    private int id;
    private UUID userID;
    private String message;

    public BlackListUser(int id, UUID userID, String message) {
        this.id = id;
        this.userID = userID;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public UUID getUserID () {
        return userID;
    }
}
