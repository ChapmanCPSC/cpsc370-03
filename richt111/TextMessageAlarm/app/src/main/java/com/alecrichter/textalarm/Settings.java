package com.alecrichter.textalarm;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class Settings extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        //Set the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarElevation(toolbar);

        //Show settings fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

    }

    private void setToolbarElevation(Toolbar toolbar) {
        //If L, hide the fake shadow
        if(Build.VERSION.SDK_INT >= 21) {
            toolbar.setElevation(14);
            findViewById(R.id.settings_shadow).setVisibility(View.GONE);
        }
    }

}
