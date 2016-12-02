package com.example.joao.cafeteriaterminal.Cafeteria;

import java.io.Serializable;
import java.util.UUID;

public class BlackListUser implements Serializable {
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

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return "ID: " + id + "\nUserID: " + userID.toString() + "\nMessage: " + message + "\n";
    }
}
