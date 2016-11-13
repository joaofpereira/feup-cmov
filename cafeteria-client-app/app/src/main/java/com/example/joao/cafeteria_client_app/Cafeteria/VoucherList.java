package com.example.joao.cafeteria_client_app.Cafeteria;

import android.util.Log;

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

    public  void deleteVoucher(Voucher v){
        Log.i("Number of vouchers In:", ""+ vouchers.size());
        Log.i("Voucher id:", ""+ v.getId());
        for(int i = 0 ; i < vouchers.size(); i++){
            if(vouchers.get(i).getId() == v.getId())
                vouchers.remove(i);
        }
    }
}
