package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;

public class SettingsActivity extends AppCompatActivity
{

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Find Views By ID
        LinearLayout logoutLinearLayout = (LinearLayout) findViewById(R.id.ll_logout);
        LinearLayout editProfileLinearLayout = (LinearLayout) findViewById(R.id.ll_editProfile);
        final SeekBar searchRadiusSeekBar = (SeekBar) findViewById(R.id.seekBarSearchRadius);
        final TextView mileUpdateTextView = (TextView) findViewById(R.id.textViewMileUpdate);//for updating the text view for the broadcast duration
        final SeekBar broadcastDurationSeekBar = (SeekBar) findViewById(R.id.seekBarBroadcastDuration);
        final TextView broadcastUpdateTextView = (TextView) findViewById(R.id.textViewDurationUpdate);//for updating the text view for the broadcast duration


        //Click Listeners
        logoutLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Show progress dialog
                progressDialog = ProgressDialog.show(SettingsActivity.this, "Loading",
                        "Please wait...", true);

                // Attempt to log out
                ASDPlaydateUser.logOutInBackground(new UserLogOutCallback());
            }
        });

        editProfileLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(SettingsActivity.this, ProfileActivity.class);
                SettingsActivity.this.startActivity(myIntent);
            }
        });

        searchRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()//default seach radius seekbar
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                mileUpdateTextView.setText((progress + 1) + " " + getString(R.string.miles));
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

        broadcastDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()//default Broadcast Duration  seekbar
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                broadcastUpdateTextView.setText((progress + 1) + " " + getString(R.string.minutes));
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
    }

    private class UserLogOutCallback implements LogOutCallback
    {
        @Override
        public void done(ParseException e)
        {
            if(e == null)
            {
                progressDialog.dismiss();

                Toast.makeText(SettingsActivity.this, "You have been logged out",
                        Toast.LENGTH_LONG).show();

                // If there are no errors, go back to login activity
                Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                // Show an error message
                Toast.makeText(SettingsActivity.this, "An error occurred while logging out",
                        Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        }
    }

}
