package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;

public class SettingsActivity extends AppCompatActivity
{

    private ProgressDialog progressDialog;
    private ASDPlaydateUser user;
    SessionManager sessionManager;

    EditText editMessage;
    SeekBar searchRadiusSeekBar, broadcastDurationSeekBar;
    TextView mileUpdateTextView, broadcastUpdateTextView;
    Switch promptBroadcast;
    int searchRadius, broadcastDuration;
    String broadcastMessage;
    boolean promptValue;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton prompt, boolean isChecked)
        {

            promptValue = isChecked;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sessionManager = new SessionManager(getApplicationContext());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchRadiusSeekBar = (SeekBar) findViewById(R.id.seekBarSearchRadius);
        mileUpdateTextView = (TextView) findViewById(R.id.textViewMileUpdate);//for updating the text view for the broadcast duration
        broadcastDurationSeekBar = (SeekBar) findViewById(R.id.seekBarBroadcastDuration);
        broadcastUpdateTextView = (TextView) findViewById(R.id.textViewDurationUpdate);//for updating the text view for the broadcast duration
        editMessage = (EditText) findViewById(R.id.editTextBroadcastMessage);
        promptBroadcast = (Switch) findViewById(R.id.switchPromptBroadcast);
        promptBroadcast.setOnCheckedChangeListener(onCheckedChangeListener);

        // Save user to unsubscribe from push notifications if they log out
        user = (ASDPlaydateUser) ParseUser.getCurrentUser();

        getCurrentInfo();


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

    public void logout(View v)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this)
                .setTitle(getString(R.string.logout_confirm))
                .setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Show progress dialog
                        progressDialog = ProgressDialog.show(SettingsActivity.this, "Loading",
                                "Please wait...", true);

                        // Attempt to log out
                        ASDPlaydateUser.logOutInBackground(new UserLogOutCallback());
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
    }

    public void editProfile(View v)
    {
        Intent myIntent = new Intent(SettingsActivity.this, ProfileActivity.class);
        SettingsActivity.this.startActivity(myIntent);
    }

    public void getCurrentInfo()
    {
        searchRadius = sessionManager.getSearchRadius();
        mileUpdateTextView.setText(searchRadius+1 + " " + getString(R.string.miles));
        broadcastDuration = sessionManager.getBroadcastDuration();
        broadcastUpdateTextView.setText(broadcastDuration+1 + " " + getString(R.string.minutes));

        searchRadiusSeekBar.setProgress(searchRadius);
        broadcastDurationSeekBar.setProgress(broadcastDuration);
        editMessage.setText(sessionManager.getBroadcastMessage());
        promptBroadcast.setChecked(sessionManager.getPromptBroadcast());
    }

    public void storeCurrentInfo()
    {
        searchRadius = searchRadiusSeekBar.getProgress();
        broadcastDuration = broadcastDurationSeekBar.getProgress();
        broadcastMessage = editMessage.getText().toString();
        promptValue = promptBroadcast.isChecked();

        sessionManager.storeSearchRadius(searchRadius);
        sessionManager.storeBroadcastDuration(broadcastDuration);
        sessionManager.storeBroadcastMessage(broadcastMessage);
        sessionManager.storePromptBroadcast(promptValue);
        sessionManager.storeFromDialog(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                storeCurrentInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        storeCurrentInfo();
        super.onBackPressed();
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

                // Clear SharedPreferences
                sessionManager.clearSessionToken();

                // Unsubscribe user from their push notification channel
                ParsePush.unsubscribeInBackground("c_" + user.getObjectId());

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
