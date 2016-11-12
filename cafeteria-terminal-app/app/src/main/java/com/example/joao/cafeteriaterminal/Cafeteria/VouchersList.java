package com.example.joao.cafeteriaterminal.Cafeteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VouchersList implements Serializable {
    private static VouchersList instance = null;

    private List<Voucher> vouchers;

    protected VouchersList() {
        this.vouchers = new ArrayList<>();
    }

    public List<Voucher> getVouchers() {
        return this.vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public void add(Voucher v) {
        this.vouchers.add(v);
    }

    public static VouchersList getInstance() {
        if(instance == null) {
            instance = new VouchersList();
        }
        return instance;
    }

    public String toString() {
        String result = new String();

        for (Voucher v : vouchers)
            result += v.toString();

        return result;
    }
}
