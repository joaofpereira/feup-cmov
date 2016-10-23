package com.example.joao.cafeteria_client_app.Authentication;

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

    public String toString() {
        return "User\nUuid: " + this.uuid +"\nName: " + this.name + "\nUsername: " + this.username + "\nEmail: " + this.email + "\nHash_pin: " + this.hash_pin + "\nCreditCardInfo: " + this.creditCardInfo + "\n";
    }

    public UUID getID(){
        return uuid;
    }
    public String getHash_pin(){
        return hash_pin;
    }
}
