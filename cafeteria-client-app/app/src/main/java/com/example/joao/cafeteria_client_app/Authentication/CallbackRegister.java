package com.example.joao.cafeteria_client_app.Authentication;

import com.example.joao.cafeteria_client_app.Cafeteria.User;

public interface CallbackRegister {
    public void onRegisterCompleted(String pin, User user);

    public void onRegisterFailed(int code, String msg);
}
