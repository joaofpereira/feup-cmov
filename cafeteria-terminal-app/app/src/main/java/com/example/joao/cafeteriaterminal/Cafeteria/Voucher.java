package com.example.joao.cafeteriaterminal.Cafeteria;

public class Voucher {

    private int id;
    private int type;
    private int serial;
    private String signature;

    public Voucher(int id, int type, int serial, String signature) {
        this.id = id;
        this.type = type;
        this.serial = serial;
        this.signature = signature;
    }

    public int getId() {
        return id;
    }

    public int getSerial() {
        return serial;
    }

    public int getType() {
        return type;
    }

    public String getSignature() {
        return signature;
    }

    public String convertType() {
        if (type == 1)
            return "Popcorn";
        else if(type == 2)
            return "Coffee";
        else
            return "Discount";
    }

    public String toString() {
        return "\nVoucherID: " + id + "\nType: " + convertType() + "\nSerial Number: " + serial + "\nSignature: " + signature + "\n";
    }
}
