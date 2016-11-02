package com.example.joao.cafeteria_client_app.Cafeteria;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance = null;

    List<CartProduct> cart;

    public Cart() {
        this.cart = new ArrayList<CartProduct>();
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
    }

    public void remove(int id) {
        for (int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getID() == id)
                cart.remove(i);
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

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }
}
