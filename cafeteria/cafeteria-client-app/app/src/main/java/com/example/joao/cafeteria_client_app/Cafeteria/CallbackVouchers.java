package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.List;


public interface CallbackVouchers {

    public void onGetVouchersCompleted(List<Voucher> vouchers);

    public void onGetVouchersError(Throwable throwable);
}
