package cpsc370.chapman.edu.asdplaydate.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cpsc370.chapman.edu.asdplaydate.R;

public class TempSettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_settings);

        //Find Views By ID
        Button profileButton = (Button) findViewById( R.id.buttonEditProfile );//find list view
        Button logoutButton = (Button) findViewById( R.id.buttonLogout );//find list view

        //Click Listeners
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(TempSettingsActivity.this, ProfileActivity.class);
                TempSettingsActivity.this.startActivity(myIntent);
            }});

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You have been logged out",
                        Toast.LENGTH_LONG).show();
            }});

    }
}
