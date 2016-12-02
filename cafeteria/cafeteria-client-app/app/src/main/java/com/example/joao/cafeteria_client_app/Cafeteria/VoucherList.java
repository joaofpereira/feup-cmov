package com.example.joao.cafeteria_client_app.Cafeteria;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    public  void deleteVoucher(int id){
        for(int i = 0 ; i < vouchers.size(); i++){
            if(vouchers.get(i).getId() == id)
                vouchers.remove(i);
        }
    }

    public String toString() {
        String res = new String();

        for(Voucher v : vouchers)
            res += "serial number: " + v.getSerial() + "\n";

        return res;
    }
}
