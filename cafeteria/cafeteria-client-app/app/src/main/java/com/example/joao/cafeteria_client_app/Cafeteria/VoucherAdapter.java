package com.example.joao.cafeteria_client_app.Cafeteria;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.cafeteria_client_app.R;

import java.util.List;


public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder> {
    private List<Voucher> voucherList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView type, serial;
        public ImageButton plusButton, minusButton;

        public Voucher voucher;

        public MyViewHolder(View view, final Context context) {
            super(view);
            type = (TextView) view.findViewById(R.id.voucher_type);
            serial = (TextView) view.findViewById(R.id.voucher_serial);

            plusButton = (ImageButton) view.findViewById(R.id.plusButton);
            minusButton = (ImageButton) view.findViewById(R.id.minusButton);

            minusButton.setVisibility(View.GONE);

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Cart.getInstance().sameTypeOfVoucherInCart(voucher)) {
                        Cart.getInstance().addVoucherToCart(voucher);
                        plusButton.setVisibility(View.GONE);
                        minusButton.setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(context, "Already used that type of voucher", Toast.LENGTH_LONG).show();

                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cart.getInstance().removeVoucher(voucher.getId());
                    plusButton.setVisibility(View.VISIBLE);
                    minusButton.setVisibility(View.GONE);
                }
            });

        }
    }

    public VoucherAdapter(List<Voucher> voucherList, Context context) {
        this.voucherList = voucherList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voucher_list_row, parent, false);

        return new MyViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(VoucherAdapter.MyViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);

        holder.voucher = voucher;

        if (Cart.getInstance().voucherInUse(holder.voucher)) {
            holder.plusButton.setVisibility(View.GONE);
            holder.minusButton.setVisibility(View.VISIBLE);
        }
        else{
            holder.plusButton.setVisibility(View.VISIBLE);
            holder.minusButton.setVisibility(View.GONE);
        }

        holder.type.setText(voucher.getType());
        holder.serial.setText(String.valueOf(voucher.getSerial()));
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

}