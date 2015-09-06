package com.alecrichter.textalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;


public class MainActivity extends AppCompatActivity {

    private AlarmDatabase db;
    private Alarm currentSelection;
    private MainRecyclerFragment recyclerFragment;
    private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Define FAB grow animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

		setContentView(R.layout.activity_main);

        //Set the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setToolbarElevation(toolbar);

        //Show fragment
        recyclerFragment = new MainRecyclerFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, recyclerFragment)
                .commit();

        //Set default pref values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        db = new AlarmDatabase(this, AlarmDatabase.databaseName, null, AlarmDatabase.databaseVersion);

        setCurrentMmsId();

        //Set listener for FAB
        fab = (FloatingActionButton) findViewById(R.id.newAlarm_FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseAlarmType();
            }
        });

        //Set FAB grow animation
        fab.startAnimation(animation);

	}

    protected void onResume () {
        super.onResume();
        setList();
    }

    public void setList() {
        recyclerFragment.setList();
    }

    public void removeAlarm(Alarm alarm, int position) {
        recyclerFragment.removeAlarm(alarm, position);
    }

    public void addAlarm(Alarm alarm, int position) {
        recyclerFragment.addAlarm(alarm, position);
    }

    private void chooseAlarmType() {

        new MaterialDialog.Builder(this)
                .title("Choose alarm type")
                .items(R.array.alarm_types)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                        if(i == 0)
                            addContact();
                        else if(i == 1) {
                            Intent newAlarmIntent = new Intent(MainActivity.this, NewAlarmActivity.class);
                            startActivity(newAlarmIntent);
                        }

                        return true;
                    }
                })
                .positiveText("OK")
                .negativeText("Cancel")
                .show();
    }

    private void addContact() {
        //Open contact picker
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

            if(cursor.moveToFirst()) {

                //Get index for phone number
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                //Get phone number
                String phoneNum = cursor.getString(phoneIndex);

                //Format phone number
                String phoneNumF = phoneNum.replace(" ", "").replace("-", "").replace("+", "");

                //Get name
                String contactName = cursor.getString(nameIndex);

                Log.d("Main", contactName + ", " + phoneNum + ", " + phoneNumF);

                //Add alarm to db
                Alarm alarm = new Alarm(null, contactName, "contact", phoneNum, phoneNumF, "true", null);
                db.addAlarm(alarm);

                //Update list
                recyclerFragment.setList();

            }
            else {
                Log.d("Main", "Error adding number");
            }

            cursor.close();

        }

        if (resultCode == Activity.RESULT_OK && requestCode == 2) {

            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Alarm alarm = currentSelection;

            //Save selected alarm
            if (uri != null) {
                alarm.setUri(uri.toString());

                db.editAlarmTone(alarm);

                recyclerFragment.setList();
            }
        }

    }

    public void setCurrentSelection(Alarm alarm) {
        currentSelection = alarm;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if(id == R.id.action_settings) {
			Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

    private void setCurrentMmsId() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //If pref is not set, find the current top id
        if(prefs.getString("pref_current_mms_id", "0").equals("0")) {

            SharedPreferences.Editor edit = prefs.edit();

            //Query the MMS database
            Cursor mmsCursor = getContentResolver().query(Telephony.Mms.CONTENT_URI,
                    new String[] {"_id"}, null, null, null);

            //Go to newest message
            if(mmsCursor.moveToFirst())
                edit.putString("pref_current_mms_id", mmsCursor.getString(0));
            else
                edit.putString("pref_current_mms_id", "0");

            edit.apply();
        }

    }

    private void setToolbarElevation(Toolbar toolbar) {
        //If L, hide the fake shadow
        if(Build.VERSION.SDK_INT >= 21) {
            toolbar.setElevation(14);
            findViewById(R.id.main_shadow).setVisibility(View.GONE);
        }
    }

}
