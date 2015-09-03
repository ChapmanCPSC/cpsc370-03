package com.applica.applicaandroid.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.models.LoginSignUpFormType;
import com.applica.applicaandroid.models.LoginSignUpModel;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class ForgotPasswordActivity extends Activity {
    private IconicsImageView applicaLogo;
    private IconicsImageView resetEmailIcon;
    private TextView resetPasswordTxt;
    private TextView enterEmailTxt;
    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Set theme
        this.setTheme(R.style.ApplicaTheme_NoActionBar);

        // Get views with text
        resetPasswordTxt = (TextView) findViewById(R.id.resetYourPassword);
        enterEmailTxt = (TextView) findViewById(R.id.pleaseEnterYourEmailBelow);
        emailInput = (EditText) findViewById(R.id.homeEmailInput);

        // Set their typeface to Helvetica Neue
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/helvetica_neue.ttf");
        TextView[] textViews = {resetPasswordTxt, enterEmailTxt, emailInput};
        for (int i=0; i<textViews.length; i++) {
            textViews[i].setTypeface(tf);
        }

        // Get and set icons and their colors
        applicaLogo = (IconicsImageView) findViewById(R.id.applicaLogo);
        applicaLogo.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.applica_logo)
                .color(Color.WHITE));

        resetEmailIcon = (IconicsImageView) findViewById(R.id.resetEmailIcon);
        resetEmailIcon.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.email_icon)
                .color(Color.parseColor("#157fc3")));
    }
    
}
