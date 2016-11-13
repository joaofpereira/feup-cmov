package com.example.joao.cafeteriaterminal.Cafeteria;

public class TransactionVoucher {

    private int serial;
    private String signature;
    private int type;

    public TransactionVoucher(int serial, String signature, int type) {
        this.serial = serial;
        this.signature = signature;
        this.type = type;
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
        return "\nSerial: " + serial + "\nType: " + type + "\nSignature: " + signature + "\n";
    }
}
