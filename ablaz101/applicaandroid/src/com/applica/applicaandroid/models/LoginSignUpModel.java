package com.applica.applicaandroid.models;

import com.applica.applicaandroid.controllers.SignUpLoginController;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class LoginSignUpModel implements SignUpLoginController {
    
    @Override
    public SignUpResult onSignUpButtonClick(EditText editTextEmail, EditText editTextPassword) {
        SignUpResult status = SignUpResult.FAIL;
        
        // EditText to String
        String email = editTextEmail.getText().toString();
        // Validate the email

        // if bad email
        status = SignUpResult.BAD_EMAIL;
        
        // EditText to String
        String password = editTextPassword.getText().toString();
        // Validate the password for password requirements
        
        // if bad password
        status = SignUpResult.BAD_PASSWORD;
        
        // Hash the password
        // get the new password
        
        // connect to DB
        
        // confirm with DB if email exists
        
        // if email already exists
        status = SignUpResult.EXISTING_EMAIL;
        
        // write to DB
        
        // close DB
        
        // If all successful
        status = SignUpResult.SUCCESS;
        
        // Determine if the sign up is a success or a fail
        return status;
    }

    @Override
    public LoginResult onLoginButtonClick(EditText email, EditText password, EditText confirmPassword) {
        LoginResult loginResult = LoginResult.FAIL;
        
        // connect to DB
        // confirm with DB (username and password)
        // close to DB
        
        // if good username and password
        loginResult = LoginResult.SUCCESS;
        
        return loginResult;
    }

    @Override
    public void onForgotPasswordClick(View view) {
        // trigger swipe animation
        // update views
        // hide views
        
        // send email to <user's email> for forgot password function
    }

}
