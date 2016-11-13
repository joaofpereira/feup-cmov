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
    }

    public void remove(int id) {
        for (int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getID() == id) {
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
            if (cartVouchers.get(i).getType().equals(voucher.getType()))
                return true;
        return false;
    }

    public float getTotalValue() {
        float totalValue = 0;

        for(int i = 0; i < cart.size(); i++)
            totalValue += cart.get(i).getPrice() * cart.get(i).getAmount();

        return totalValue;
    }

    public void clearCart() {
        this.cart.clear();
    }

    public int getProductAmountOfProduct(Product product) {
        if(cart.isEmpty())
            return 0;

        for(int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getID() == product.getID())
                return cart.get(i).getAmount();
        }

        return 0;
    }

    public void clearVouchers() {
        this.cartVouchers.clear();
    }

    public boolean voucherInUse(Voucher v){

        for (int i = 0 ; i < cartVouchers.size(); i++){
            if(cartVouchers.get(i).getSerial() == v.getSerial())
                return true;
        }

        return false;
    }

    public String getQRCodeData() {
        String result = new String();

        result += User.getInstance().getID() + "\n" + getTotalValue();
        result += "\n"+cartVouchers.size();


        for(int i = 0; i < cartVouchers.size(); i++) {
            int type = 0;

            if (cartVouchers.get(i).getType().equals("popcorn"))
                type=1;
            else if(cartVouchers.get(i).getType().equals("coffee"))
                type=2;
            else if (cartVouchers.get(i).getType().equals("discount"))
                type=3;

            result += "\n" + cartVouchers.get(i).getId() +"\n" +type + "\n" + cartVouchers.get(i).getSerial() + "\n" + cartVouchers.get(i).getSignature();
        }
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
