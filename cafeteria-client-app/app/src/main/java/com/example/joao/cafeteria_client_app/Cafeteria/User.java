package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.UUID;

public class User {
    private UUID uuid;
    private String name;
    private String username;
    private String email;
    private String hash_pin;
    private String creditCardInfo;

    public User(UUID uuid, String name, String username, String email, String hash_pin, String creditCardInfo) {
        this.uuid = uuid;
        this.name = name;
        this.username = username;
        this.email = email;
        this.hash_pin = hash_pin;
        this.creditCardInfo = creditCardInfo;
    }

    public UUID getID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getHash_pin() {
        return this.hash_pin;
    }

    public String toString() {
        return "User\nUUID: " + this.uuid.toString() +"\nName: " + this.name + "\nUsername: " + this.username + "\nEmail: " + this.email + "\nHash_pin: " + this.hash_pin + "\nCreditCardInfo: " + this.creditCardInfo + "\n";
    }
}
