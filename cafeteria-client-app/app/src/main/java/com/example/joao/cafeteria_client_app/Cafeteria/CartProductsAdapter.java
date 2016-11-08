package com.example.joao.cafeteria_client_app.Cafeteria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.R;

import java.util.List;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.MyViewHolder> {

    List<CartProduct> cart;
    Context context;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, amount;
        public Button checkout_btn;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.product_name);
            price = (TextView) view.findViewById(R.id.product_price);
            amount = (TextView) view.findViewById(R.id.product_amount);

            checkout_btn = (Button) view.findViewById(R.id.checkout_btn);
        }
    }

    public CartProductsAdapter(Context context, Activity activity, List<CartProduct> cart) {
        this.cart = cart;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(viewType == R.layout.cart_products_list_row) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_products_list_row, parent, false);
        } else {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_btn, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(position == cart.size()) {
            if(cart.isEmpty())
                holder.checkout_btn.setVisibility(View.INVISIBLE);
            else
                holder.checkout_btn.setVisibility(View.VISIBLE);

            holder.checkout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, CheckoutActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
        else {
            CartProduct cartProduct = cart.get(position);

            holder.name.setText(cartProduct.getName());
            holder.price.setText(Float.toString(cartProduct.getPrice() * cartProduct.getAmount()) + " €");
            holder.amount.setText(Integer.toString(cartProduct.getAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return Cart.getInstance().getCart().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == cart.size()) ? R.layout.checkout_btn : R.layout.cart_products_list_row;
    }
}
