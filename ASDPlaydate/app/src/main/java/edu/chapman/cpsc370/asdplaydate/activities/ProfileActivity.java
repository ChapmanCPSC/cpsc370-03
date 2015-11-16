package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.HashMap;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;

public class ProfileActivity extends AppCompatActivity
{

    private ASDPlaydateUser user;
    private Child child;
    private ProgressDialog progressDialog;
    private HashMap<String, String> profileInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        //Find Views By ID
        FloatingActionButton saveProfileButton = (FloatingActionButton) findViewById(R.id.fab_saveProfile);//find list view
        final TextView defaultSearchRadius = (TextView) findViewById(R.id.textViewSearchRadius);

        //Click Listeners
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                // Save currently entered info
                getCurrentInfo();

                // Check if a new account is being created
                if (getIntent().getStringExtra("accountName") != null)
                {
                    createAccount();
                }
                else
                {
                    updateProfile();
                }
            }
        });
    }

    private void getCurrentInfo()
    {

        EditText parentFirstName = (EditText) findViewById(R.id.editParentFname);
        EditText parentLastName = (EditText) findViewById(R.id.editParentLname);
        EditText city = (EditText) findViewById(R.id.editCity);
        EditText childName = (EditText) findViewById(R.id.editChildFname);
        EditText childAge = (EditText) findViewById(R.id.editAge);
        EditText childCondition = (EditText) findViewById(R.id.editConditionInformation);
        RadioGroup childGenderGroup = (RadioGroup) findViewById(R.id.radioGroupGender);
        String gender;

        // Get the selected gender
        Integer selectedId = childGenderGroup.getCheckedRadioButtonId();
        if(selectedId != -1)
        {
            RadioButton childGenderButton = (RadioButton) findViewById(selectedId);
            gender = childGenderButton.getText().toString().toUpperCase();
        }
        else
        {
            gender = "NONE";
        }

        // Set age to 0 if it is left blank
        String age = childAge.getText().toString();
        if(age.equals("")) {
            age = "0";
        }

        // Save current info
        profileInfo.put("parentFirst", parentFirstName.getText().toString());
        profileInfo.put("parentLast", parentLastName.getText().toString());
        profileInfo.put("city", city.getText().toString());
        profileInfo.put("childName", childName.getText().toString());
        profileInfo.put("childAge", age);
        profileInfo.put("childCondition", childCondition.getText().toString());
        profileInfo.put("childGender", gender);
    }

    private void updateProfile()
    {
        // TODO: Implement the updateProfile method

        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void createAccount()
    {
        String accountName = getIntent().getStringExtra("accountName");
        String accountPass = getIntent().getStringExtra("accountPass");

        // Create a new user
        user = new ASDPlaydateUser(accountName, accountPass, profileInfo.get("parentFirst"),
                profileInfo.get("parentLast"), profileInfo.get("city"));

        // Create a new child
        if(profileInfo.get("childCondition").equals(""))
        {
            child = new Child(user, profileInfo.get("childName"),
                    Integer.parseInt(profileInfo.get("childAge")),
                    Child.Gender.valueOf(profileInfo.get("childGender")));
        }
        else
        {
            child = new Child(user, profileInfo.get("childName"),
                    Integer.parseInt(profileInfo.get("childAge")),
                    Child.Gender.valueOf(profileInfo.get("childGender")),
                    profileInfo.get("childCondition"));
        }

        // Show progress dialog
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

        // Attempt to save user
        user.signUpInBackground(new UserSignUpCallback());
    }


    private class UserSignUpCallback implements SignUpCallback
    {
        @Override
        public void done(ParseException e)
        {
            if(e == null)
            {
                // If user saves successfully, save child
                child.saveInBackground(new ChildSaveCallback());
            }
            else
            {
                // Show an error message
                Toast.makeText(ProfileActivity.this,
                        "An error occurred. This email may already be in use.",
                        Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        }
    }

    private class ChildSaveCallback implements SaveCallback
    {
        @Override
        public void done(ParseException e)
        {
            if(e == null)
            {
                progressDialog.dismiss();

                // Load main activity if there are no errors
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                // Remove user if child save fails so that it doesn't appear as being already registered on another attempt
                user.deleteInBackground();

                // Show an error message
                Toast.makeText(ProfileActivity.this, "An error occurred", Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        }
    }

}
