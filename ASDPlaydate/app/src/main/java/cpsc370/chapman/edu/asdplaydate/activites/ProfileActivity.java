package cpsc370.chapman.edu.asdplaydate.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cpsc370.chapman.edu.asdplaydate.R;

public class ProfileActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Find Views By ID
        Button saveProfileButton = (Button) findViewById( R.id.buttonSaveProfile );//find list view

        //Click Listeners
        saveProfileButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(ProfileActivity.this, TempSettingsActivity.class);
                ProfileActivity.this.startActivity(myIntent);
            }
        });
    }


}
