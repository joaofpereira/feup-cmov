package com.example.joao.cafeteria_client_app.Cafeteria;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.R;

import java.util.List;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.MyViewHolder> {

    List<CartProduct> cart;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, amount;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.product_name);
            price = (TextView) view.findViewById(R.id.product_price);
            amount = (TextView) view.findViewById(R.id.product_amount);
        }
    }

    public CartProductsAdapter() {
        this.cart = Cart.getInstance().getCart();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_products_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CartProduct cartProduct = cart.get(position);

        holder.name.setText(cartProduct.getName());
        holder.price.setText(Float.toString(cartProduct.getPrice() * cartProduct.getAmount()) + " €");
        holder.amount.setText(Integer.toString(cartProduct.getAmount()));
    }

    @Override
    public int getItemCount() {
        return Cart.getInstance().getCart().size();
    }
}
