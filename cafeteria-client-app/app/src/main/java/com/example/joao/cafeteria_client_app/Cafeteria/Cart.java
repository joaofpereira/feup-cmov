package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance = null;

    List<CartProduct> cart;
    float totalValue;

    public Cart() {
        this.cart = new ArrayList<CartProduct>();
        this.totalValue = 0;
    }

    public List<CartProduct> getCart() {
        return this.cart;
    }

    public void add(Product product, int amount) {

        if (!existsInCart(product.getID())) {
            CartProduct cartProduct = new CartProduct(product, amount);
            this.cart.add(cartProduct);
        } else {
            getCartProductByID(product.getID()).setAmount(amount);
        }

        updateCartTotalValue(product.getPrice());
    }

    public void remove(int id) {
        for (int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getID() == id) {
                updateCartTotalValue(-cart.get(i).getPrice());
                cart.remove(i);
            }
        }
    }

    private CartProduct getCartProductByID(int id) {
        for (int i = 0; i < cart.size(); i++)
            if (cart.get(i).getID() == id)
                return cart.get(i);

        return null;
    }

    private boolean existsInCart(int id) {
        if (!cart.isEmpty())
            for (CartProduct cp : cart)
                if (cp.getID() == id)
                    return true;

        return false;
    }

    public float getTotalValue() {
        return this.totalValue;
    }

    public void updateCartTotalValue(float value) {
        this.totalValue += value;
    }

    public void clearCart() {
        this.cart.clear();
        this.totalValue = 0;
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }
}
