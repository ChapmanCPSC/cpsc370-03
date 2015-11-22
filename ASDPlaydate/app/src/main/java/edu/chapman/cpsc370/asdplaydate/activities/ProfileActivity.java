package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.CreateAccountFragment;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;

public class ProfileActivity extends AppCompatActivity
{

    private ASDPlaydateUser user;
    private Child child;
    private ProgressDialog progressDialog;
    String parentFirst, parentLast, city, childName, age, condition, gender;
    EditText parentFirstName, parentLastName, cityName, childAge, childFirst, childCondition;
    RadioButton childGenderButton;
    RadioGroup childGenderGroup;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        sessionManager = new SessionManager(getApplicationContext());

        parentFirstName = (EditText) findViewById(R.id.editParentFname);
        parentLastName = (EditText) findViewById(R.id.editParentLname);
        cityName = (EditText) findViewById(R.id.editCity);
        childFirst = (EditText) findViewById(R.id.editChildFname);
        childAge = (EditText) findViewById(R.id.editAge);
        childCondition = (EditText) findViewById(R.id.editConditionInformation);
        childGenderGroup = (RadioGroup) findViewById(R.id.radioGroupGender);


        FloatingActionButton saveProfileButton = (FloatingActionButton) findViewById(R.id.fab_saveProfile);
        saveProfileButton.setOnClickListener(onClickListener);

        if(ASDPlaydateUser.getCurrentUser() != null)
        {
            try
            {
                getProfileInfo();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            if(getCurrentInfo())
            {
                // Check if a new account is being created
                if (getIntent().getStringExtra(CreateAccountFragment.ACCOUNT_EMAIL) != null)
                {
                    createAccount();
                }
                else
                {
                    try
                    {
                        updateProfile();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }


        }
    };

    private void getProfileInfo() throws Exception
    {
        ASDPlaydateUser currentUser = (ASDPlaydateUser)ASDPlaydateUser.getCurrentUser();
        if(currentUser != null)
        {
            parentFirstName.setText(currentUser.getFirstName());
            parentLastName.setText(currentUser.getLastName());
            cityName.setText(currentUser.getCityName());

            Child currentChild = getChildWithParent(currentUser);
            if(currentChild != null)
            {
                childFirst.setText(currentChild.getFirstName());
                childAge.setText(String.valueOf(currentChild.getAge()));
                childCondition.setText(currentChild.getDescription());
                if(currentChild.getGender().name().equalsIgnoreCase(getString(R.string.female)))
                {
                    childGenderButton = (RadioButton)findViewById(R.id.radioButtonFemale);
                    childGenderButton.setChecked(true);
                }
                else if (currentChild.getGender().name().equalsIgnoreCase(getString(R.string.male)))
                {
                    childGenderButton = (RadioButton)findViewById(R.id.radioButtonMale);
                    childGenderButton.setChecked(true);
                }


            }
        }


    }

    private Child getChildWithParent(ASDPlaydateUser parent) throws Exception
    {
        ParseQuery<Child> q = new ParseQuery<Child>(Child.class);
        q.whereEqualTo(Child.ATTR_PARENT, parent);
        return q.find().get(0);
    }


    private boolean getCurrentInfo()
    {

        parentFirst = parentFirstName.getText().toString();
        parentLast = parentLastName.getText().toString();
        city = cityName.getText().toString();
        childName = childFirst.getText().toString();
        age = childAge.getText().toString();
        condition = childCondition.getText().toString();

        // Get the selected gender
        Integer selectedId = childGenderGroup.getCheckedRadioButtonId();
        if(selectedId != -1)
        {
            childGenderButton = (RadioButton) findViewById(selectedId);
            gender = childGenderButton.getText().toString().toUpperCase();
        }
        else
        {
            gender = "NONE";
        }

        if(parentFirst.equals("") || parentFirst == null)
        {
            Toast.makeText(this, getString(R.string.missing_parent_first), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(parentLast.equals("") || parentLast == null)
        {
            Toast.makeText(this, getString(R.string.missing_parent_last), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(city.equals("") || city == null)
        {
            Toast.makeText(this, getString(R.string.missing_city), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(childName.equals("") || childName == null)
        {
            Toast.makeText(this, getString(R.string.missing_child_name), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(age.equals("") || age == null)
        {
            Toast.makeText(this, getString(R.string.missing_age), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(gender.equals("NONE") || gender == null)
        {
            Toast.makeText(this, getString(R.string.missing_gender), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private void updateProfile() throws  Exception
    {
        ASDPlaydateUser updatedUser = (ASDPlaydateUser)ASDPlaydateUser.getCurrentUser();
        updatedUser.setFirstName(parentFirst);
        updatedUser.setLastName(parentLast);
        updatedUser.setCityName(city);
        updatedUser.save();

        Child updatedChild = getChildWithParent(updatedUser);
        updatedChild.setFirstName(childName);
        updatedChild.setAge(Integer.valueOf(age));
        updatedChild.setDescription(condition);
        updatedChild.setGender(Child.Gender.valueOf(gender));
        updatedChild.save();

        Toast.makeText(getApplicationContext(), getString(R.string.updated_profile), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void createAccount()
    {
        String accountName = getIntent().getStringExtra(CreateAccountFragment.ACCOUNT_EMAIL);
        String accountPass = getIntent().getStringExtra(CreateAccountFragment.ACCOUNT_PASSWORD);

        // Create a new user
        user = new ASDPlaydateUser(accountName, accountPass, parentFirst,
                parentLast, city);

        // Create a new child
        if(condition.equals(""))
        {
            child = new Child(user, childName,
                    Integer.parseInt(age),
                    Child.Gender.valueOf(gender));
        }
        else
        {
            child = new Child(user, childName,
                    Integer.parseInt(age),
                    Child.Gender.valueOf(gender),
                    condition);
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

                // Store session token
                if(user != null)
                {
                    sessionManager.storeSessionToken(user.getSessionToken());

                    // Load main activity if there are no errors
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);

                }


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
