package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josec on 12/11/2016.
 */

public class VoucherList{
    private static VoucherList instance = null;

    private List<Voucher> vouchers;

    public VoucherList() {
        this.vouchers = new ArrayList<>();
    }

    public List<Voucher> getVouchers() {
        return this.vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public Voucher getVoucherByID(int id) {
        for (Voucher v : vouchers)
            if(v.getId() == id)
                return v;

        return null;
    }

    public static VoucherList getInstance() {
        if (instance == null) {
            instance = new VoucherList();
        }
        return instance;
    }
}
