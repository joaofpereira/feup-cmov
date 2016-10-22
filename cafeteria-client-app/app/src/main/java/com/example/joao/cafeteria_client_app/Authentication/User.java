package com.example.joao.cafeteria_client_app.Authentication;

public class User {
    private String name;
    private String username;
    private String email;
    private String hash_pin;
    private String creditCardInfo;

    public User(String name, String username, String email, String hash_pin, String creditCardInfo) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.hash_pin = hash_pin;
        this.creditCardInfo = creditCardInfo;
    }

    public String toString() {
        return "User\nName: " + this.name + "\nUsername: " + this.username + "\nEmail: " + this.email + "\nHash_pin: " + this.hash_pin + "\nCreditCardInfo: " + this.creditCardInfo + "\n";
    }
}
