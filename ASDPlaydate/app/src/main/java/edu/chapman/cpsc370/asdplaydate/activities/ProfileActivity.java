package edu.chapman.cpsc370.asdplaydate.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.chapman.cpsc370.asdplaydate.R;

public class ProfileActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Find Views By ID
        FloatingActionButton saveProfileButton = (FloatingActionButton) findViewById(R.id.fab_saveProfile);//find list view
        final TextView defaultSearchRadius = (TextView) findViewById(R.id.textViewSearchRadius);


      //  searchRadiusSeekBar.setOnSeekBarChangeListener(customSeekBarListener); //seek bar test code

        //Click Listeners


        saveProfileButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(ProfileActivity.this, SettingsActivity.class);
                ProfileActivity.this.startActivity(myIntent);
            }
        });

        //seek bar test code

        /*
        final TextView mileUpdateTextView = (TextView) findViewById(R.id.textViewMileUpdate);//for updating the text view for the broadcast duration
        final TextView broadcastMessageTextView = (TextView) findViewById(R.id.textViewBroadcastMessage);//for updating the text view for the broadcast duration
        mileUpdateTextView.setText("1");
        final SeekBar searchRadiusSeekBar = (SeekBar) findViewById(R.id.seekBarSearchRadius); //seek bar test code

        searchRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                mileUpdateTextView.setText(progress);
                Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });
    */
    }

}
