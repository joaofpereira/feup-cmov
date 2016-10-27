package com.example.joao.cafeteria_client_app.Authentication;

import com.example.joao.cafeteria_client_app.Cafeteria.User;

public interface CallbackLogin {
    public void onLoginCompleted(String pin, User user);

    public void onLoginFailed(int code, String msg);
}
