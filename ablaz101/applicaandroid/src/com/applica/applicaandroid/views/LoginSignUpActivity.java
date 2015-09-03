package com.applica.applicaandroid.views;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.models.LoginSignUpFormType;
import com.applica.applicaandroid.models.LoginSignUpModel;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginSignUpActivity extends Activity {
    private EditText emailInput;
    private EditText passwordInput;
    private TextView forgotPasswordTxt;
    private EditText confirmPasswordInput;
    private LoginSignUpModel loginSignUpModel;
    private LoginSignUpFormType formType;
    private IconicsImageView applicaLogo;
    private ImageView signUpTab;
    private ImageView loginTab;
    private IconicsImageView emailIcon;
    private IconicsImageView lockIcon;
    private IconicsImageView checkIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        // Set theme
        this.setTheme(R.style.ApplicaTheme_NoActionBar);

        // Get views with text
        emailInput = (EditText) findViewById(R.id.homeEmailInput);
        passwordInput = (EditText) findViewById(R.id.homePasswordInput);
        forgotPasswordTxt = (TextView) findViewById(R.id.forgotPasswordTitle);
        confirmPasswordInput = (EditText) findViewById(R.id.homeConfirmPasswordInput);

        // Set their typeface to Helvetica Neue
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/helvetica_neue.ttf");
        TextView[] textViews = {emailInput, passwordInput, forgotPasswordTxt, confirmPasswordInput};
        for (int i=0; i<textViews.length; i++) {
            textViews[i].setTypeface(tf);
        }

        // Get and set icons and their colors
        final String APPLICA_BLUE = "#157fc3";
        applicaLogo = (IconicsImageView) findViewById(R.id.applicaLogo);
        applicaLogo.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.applica_logo)
                .color(Color.WHITE));
        signUpTab = (ImageView) findViewById(R.id.homeSignUpImage);
        signUpTab.setImageResource(R.drawable.gray_sign_up_btn);
        loginTab = (ImageView) findViewById(R.id.homeLoginImage);
        loginTab.setImageResource(R.drawable.blue_log_in_btn);
        emailIcon = (IconicsImageView) findViewById(R.id.homeEmailIcon);
        emailIcon.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.email_icon)
                .color(Color.parseColor(APPLICA_BLUE)));
        lockIcon = (IconicsImageView) findViewById(R.id.homePasswordIcon);
        lockIcon.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.lock_icon)
                .color(Color.parseColor(APPLICA_BLUE)));
        checkIcon = (IconicsImageView) findViewById(R.id.homeCheckIcon);
        checkIcon.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.check_icon)
                .color(Color.parseColor(APPLICA_BLUE)));

        // Instantiate new model for this activity
        loginSignUpModel = new LoginSignUpModel();

        // Change layout to login form elements
        formType = LoginSignUpFormType.LOGIN;
        changeToLoginLayout(loginTab);
    }

    public void onSignUpButtonClick(View button) {
        // Do logic with inputs
        EditText editTextEmail = (EditText) findViewById(R.id.homeEmailInput);
        EditText editTextPassword = (EditText) findViewById(R.id.homePasswordInput);
        loginSignUpModel.onSignUpButtonClick(editTextEmail, editTextPassword);

        // TODO Update views and/or go to another activity (Intent)
    }

    public void onLoginButtonClick(View button) {
        // Do logic with inputs
        EditText editTextEmail = (EditText) findViewById(R.id.homeEmailInput);
        EditText editTextPassword = (EditText) findViewById(R.id.homePasswordInput);
        loginSignUpModel.onSignUpButtonClick(editTextEmail, editTextPassword);

        // TODO Update views and/or go to another activity (Intent)
    }

    public void onForgotPasswordClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void changeToLoginLayout(View v) {
        // Change formtype
        formType = LoginSignUpFormType.LOGIN;

        // Change sign up tab to gray mode
        signUpTab.setImageResource(R.drawable.gray_sign_up_btn);

        // Change login tab to blue mode
        loginTab.setImageResource(R.drawable.blue_log_in_btn);

        // Hide check icon
        checkIcon.setVisibility(View.GONE);

        // Hide confirm password input
        confirmPasswordInput.setVisibility(View.GONE);

        // Show forgot textview
        forgotPasswordTxt.setVisibility(View.VISIBLE);
    }

    public void changeToSignUpLayout(View v) {
        // Change formtype
        formType = LoginSignUpFormType.SIGN_UP;

        // Change login up tab to gray mode
        loginTab.setImageResource(R.drawable.gray_log_in_btn);

        // Change sign up tab to blue mode
        signUpTab.setImageResource(R.drawable.blue_sign_up_btn);

        // Hide check icon
        checkIcon.setVisibility(View.VISIBLE);

        // Hide confirm password input
        confirmPasswordInput.setVisibility(View.VISIBLE);

        // Show forgot textview
        forgotPasswordTxt.setVisibility(View.GONE);
    }
    
}
