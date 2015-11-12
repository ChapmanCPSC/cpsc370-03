package edu.chapman.cpsc370.asdplaydate;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.Child;

public class BaseApplication extends Application
{
    public BaseApplication()
    {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public void setupParse()
    {
        ParseUser.registerSubclass(ASDPlaydateUser.class);
        ParseObject.registerSubclass(Child.class);
        ParseObject.registerSubclass(Broadcast.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    }
}

