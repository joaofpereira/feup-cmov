package com.example.joao.cafeteria_client_app.Cafeteria;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.R;

import java.util.List;


public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder> {
    private List<Voucher> voucherList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView type, serial;
        public ImageButton plusButton, minusButton;

        public int product_amount;
        public Voucher voucher;

        public MyViewHolder(View view) {
            super(view);
            type = (TextView) view.findViewById(R.id.voucher_type);
            serial = (TextView) view.findViewById(R.id.voucher_serial);

            plusButton = (ImageButton) view.findViewById(R.id.plusButton);
            minusButton = (ImageButton) view.findViewById(R.id.minusButton);


            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

    public VoucherAdapter( List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voucher_list_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(VoucherAdapter.MyViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);

        holder.voucher = voucher;

        holder.type.setText(voucher.getType());
        holder.serial.setText(voucher.getSerial());
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }
}