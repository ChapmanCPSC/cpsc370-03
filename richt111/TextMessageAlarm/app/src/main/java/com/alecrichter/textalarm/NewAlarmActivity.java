package com.alecrichter.textalarm;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class NewAlarmActivity extends ActionBarActivity {

    private EditText newAlarmTitle, newAlarmContent;
    private CheckBox newAlarmCaseSens;
    private AlarmDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        //Set the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.newAlarm_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarElevation(toolbar);

        //Initialize db
        db = new AlarmDatabase(this, AlarmDatabase.databaseName, null, AlarmDatabase.databaseVersion);

        //Get xml objects
        newAlarmTitle = (EditText) findViewById(R.id.newAlarm_title);
        newAlarmContent = (EditText) findViewById(R.id.newAlarm_content);
        newAlarmCaseSens = (CheckBox) findViewById(R.id.newAlarm_caseSens);
    }

    private void addPhrase(String title, String content) {

        //If title is left blank, fill it automatically
        if(title.equals(""))
            title = "Phrase Alarm";

        //Check if fields are blank before adding alarm
        if(content.equals(""))
            Toast.makeText(this, "Enter a word or a phrase", Toast.LENGTH_LONG).show();

        //Check if case sensitive
        else if(newAlarmCaseSens.isChecked()){
            Alarm alarm = new Alarm(null, title, "phrase", content, "case_sensitive", "true", null);
            db.addAlarm(alarm);
            finish();
        }
        else {
            Alarm alarm = new Alarm(null, title, "phrase", content, "not_case_sensitive", "true", null);
            db.addAlarm(alarm);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save) {
            //Add alarm to db and return
            addPhrase(newAlarmTitle.getText().toString(), newAlarmContent.getText().toString());
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbarElevation(Toolbar toolbar) {
        //If L, hide the fake shadow
        if(Build.VERSION.SDK_INT >= 21) {
            toolbar.setElevation(14);
            findViewById(R.id.newAlarm_shadow).setVisibility(View.GONE);
        }
    }

}
