package com.example.joao.cafeteria_client_app.Cafeteria;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private UUID uuid;
    private String name;
    private String username;
    private String email;
    private String hash_pin;
    private CreditCard creditCard;

    public User(UUID uuid, String name, String username, String email, String hash_pin) {
        this.uuid = uuid;
        this.name = name;
        this.username = username;
        this.email = email;
        this.hash_pin = hash_pin;
    }

    public UUID getID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHash_pin() {
        return this.hash_pin;
    }

    public String toString() {
        return "User\nUUID: " + this.uuid.toString() +"\nName: " + this.name + "\nUsername: " + this.username + "\nEmail: " + this.email + "\nHash_pin: " + this.hash_pin + "\nCreditCard:\n" + this.creditCard.toString();
    }

    public void createCreditCard(int id, String cardNumber, String expMonth, String expYear) {
        CreditCard creditCard = new CreditCard(id, cardNumber, expMonth, expYear);
        this.creditCard = creditCard;
    }

    public CreditCard getCreditCard() {
        return this.creditCard;
    }

    private class CreditCard implements Serializable {
        private int id;
        private String cardNumber;
        private String expMonth;
        private String expYear;

        public CreditCard(int id, String cardNumber, String expMonth, String expYear) {
            this.id = id;
            this.cardNumber = cardNumber;
            this.expMonth = expMonth;
            this.expYear = expYear;
        }

        public String getCardNumber() {
            return this.cardNumber;
        }

        public String toString() {
            return "ID: " + this.id + "\nNumber: " + this.cardNumber + "\nExpiration Date: " + this.expMonth + "/" + this.expMonth + "\n";
        }
    }
}
