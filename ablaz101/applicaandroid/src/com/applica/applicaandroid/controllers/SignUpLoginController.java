package com.applica.applicaandroid.controllers;

import com.applica.applicaandroid.models.LoginResult;
import com.applica.applicaandroid.models.SignUpResult;

import android.view.View;
import android.widget.EditText;

public interface SignUpLoginController {
    public SignUpResult onSignUpButtonClick(EditText email, EditText password);
    public LoginResult onLoginButtonClick(EditText email, EditText password, EditText confirmPassword);
    public void onForgotPasswordClick(View view);

}
