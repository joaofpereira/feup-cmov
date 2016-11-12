package com.example.joao.cafeteria_client_app.Cafeteria;

public class Voucher {

    private int id;
    private String type;

    private int serial;
    private String signature;

    public Voucher(int id, String type, int serial, String signature) {
        this.id = id;
        this.type = type;
        this.serial = serial;
        this.signature = signature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}