package com.example.joao.cafeteria_client_app.Cafeteria;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance = null;

    List<CartProduct> cart;
    List<Voucher> cartVouchers;
    float totalValue;

    public Cart() {
        this.cart = new ArrayList<CartProduct>();
        this.cartVouchers = new ArrayList<Voucher>();
        this.totalValue = 0;
    }

    public List<CartProduct> getCart() {
        return this.cart;
    }

    public List<Voucher> getCartVouchers() {return this.cartVouchers;}

    public void addVoucherToCart(Voucher voucher){
        this.cartVouchers.add(voucher);
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

    public void removeVoucher(int id) {
        for (int i = 0; i < cartVouchers.size(); i++)
            if(cartVouchers.get(i).getId() == id)
                cartVouchers.remove(i);
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

    public boolean sameTypeOfVoucherInCart(Voucher voucher) {
        for (int i = 0; i < cartVouchers.size(); i++)
            if (cartVouchers.get(i).getType() == voucher.getType()) {
                Log.i("RESULTADO: ", Boolean.toString(cartVouchers.get(i).getType() == voucher.getType()));

                return true;
            }

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
        this.cartVouchers.clear();
    }

    public void clearVouchers() {
        this.cartVouchers.clear();
    }

    public String getQRCodeData() {
        String result = new String();

        result += User.getInstance().getID() + "\n" + getTotalValue();

        for(int i = 0; i < cart.size(); i++)
            result += "\n" + cart.get(i).getID() + ":" + cart.get(i).getAmount();

        return result;
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public String getStringOfVouchers(){

        Log.i("TESTE: ", "Entrei");
        Log.i("NumVouchers: ", "" + cartVouchers.size());

        String temp = new String();

        if(!cartVouchers.isEmpty())
            temp += cartVouchers.get(0).getType();

        for (int i = 1 ; i < cartVouchers.size(); i++)
            temp += ", " + cartVouchers.get(i).getType();

        Log.i("STRING TEMP: ", temp);
        Log.i("STRING: ", "ksnjnfsm");

        return temp;
    }
}
