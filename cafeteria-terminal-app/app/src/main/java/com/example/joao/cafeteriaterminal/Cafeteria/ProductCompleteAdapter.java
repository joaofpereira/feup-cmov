package com.example.joao.cafeteriaterminal.Cafeteria;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joao.cafeteriaterminal.R;

import java.util.List;

public class ProductCompleteAdapter extends RecyclerView.Adapter<ProductCompleteAdapter.MyViewHolder> {
    private List<ProductComplete> productsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, amount;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.product_name);
            price = (TextView) view.findViewById(R.id.product_price);
            amount = (TextView) view.findViewById(R.id.product_amount);
        }
    }

    public ProductCompleteAdapter(List<ProductComplete> productsList) {
        this.productsList = productsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_complete_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductComplete product = productsList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(Float.toString(product.getTotalPrice()) + " €");
        holder.amount.setText(Integer.toString(product.getAmount()));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
