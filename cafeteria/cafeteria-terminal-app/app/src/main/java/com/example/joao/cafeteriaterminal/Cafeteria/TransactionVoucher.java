package com.example.joao.cafeteriaterminal.Cafeteria;

public class TransactionVoucher {

    private int id;
    private int serial;
    private String signature;
    private int type;

    public TransactionVoucher(int id, int serial, String signature, int type) {
        this.id = id;
        this.serial = serial;
        this.signature = signature;
        this.type = type;
    }

    public int getID() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public int getSerial() {
        return this.serial;
    }

    public String getSignature() {
        return this.signature;
    }

    public String toString() {
        return "\nID: " + id + "\nSerial: " + serial + "\nType: " + type + "\nSignature: " + signature + "\n";
    }
}
