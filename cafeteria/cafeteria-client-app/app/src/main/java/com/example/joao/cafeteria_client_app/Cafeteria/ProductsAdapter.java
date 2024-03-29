package com.example.joao.cafeteria_client_app.Cafeteria;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private List<Product> productsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, amount;
        public ImageButton plusButton, minusButton;

        public Product product;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.product_name);
            price = (TextView) view.findViewById(R.id.product_price);
            amount = (TextView) view.findViewById(R.id.product_amount);
            plusButton = (ImageButton) view.findViewById(R.id.plusButton);
            minusButton = (ImageButton) view.findViewById(R.id.minusButton);

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int product_amount = Cart.getInstance().getProductAmountOfProduct(product) + 1;
                    Cart.getInstance().add(product, product_amount);
                    amount.setText(Integer.toString(product_amount));
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int product_amount = Cart.getInstance().getProductAmountOfProduct(product);
                    if (product_amount > 0) {
                        product_amount -= 1;
                        if (product_amount > 1) {
                            Cart.getInstance().add(product, product_amount);
                        } else {
                            Cart.getInstance().remove(product.getID());
                        }
                    }
                    amount.setText(Integer.toString(product_amount));
                }
            });
        }
    }

    public ProductsAdapter(List<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = productsList.get(position);

        holder.product = product;

        holder.name.setText(product.getName());
        holder.price.setText(Float.toString(product.getPrice()) + " €");
        holder.amount.setText(Integer.toString(Cart.getInstance().getProductAmountOfProduct(product)));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
