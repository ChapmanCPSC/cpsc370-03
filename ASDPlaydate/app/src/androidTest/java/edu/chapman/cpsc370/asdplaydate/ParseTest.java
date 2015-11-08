package edu.chapman.cpsc370.asdplaydate;

import android.test.AndroidTestCase;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;

/**
 * Created by ryanburns on 11/7/15.
 */
public class ParseTest extends AndroidTestCase
{
    private final String LOGTAG = "ParseTest";
    private final String TEST_SESSION = "LMwzxUCed5";

    @Test
    public void testAddUser() throws Exception
    {
        setup();

        final CountDownLatch signal = new CountDownLatch(1);

        final ASDPlaydateUser user = new ASDPlaydateUser("rburns4@chapman.edu", "test", "Ryan", "Burns", "Santa Ana");
        final Child c = new Child(user, "Lilly3",10, Child.Gender.FEMALE, "High Functioning");
        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if (e!=null)
                {
                    Assert.fail(e.getMessage());
                }
                else
                {
                    Log.i(LOGTAG, "Logged In");

                    c.saveInBackground(new SaveCallback()
                    {
                        @Override
                        public void done(ParseException e)
                        {
                            if (e!=null)
                            {
                                Assert.fail(e.getMessage());
                            }
                            else
                            {
                                Log.i(LOGTAG, "Child saved");

                                List<Child> children = new ArrayList<Child>();

                                try
                                {
                                    //assert that children exist
                                    children = getChildren();
                                }
                                catch (ParseException pe)
                                {
                                    Assert.fail(pe.getMessage());
                                }

                                assertTrue("No child was created", children.size()>0);

                            }
                            signal.countDown();
                        }
                    });

                }
            }
        });

        signal.await();
    }

    @Test
    public void testGetChildren() throws Exception
    {
        final CountDownLatch signal = new CountDownLatch(1);

        ASDPlaydateUser.becomeInBackground(TEST_SESSION, new LogInCallback()
        {
            @Override
            public void done(ParseUser user, ParseException e)
            {
                if (e != null)
                {
                    Log.e(LOGTAG, "Error getting children", e);
                }
                else
                {
                }
                signal.countDown();
            }
        });

        signal.await();
    }

    private List<Child> getChildren() throws ParseException
    {
        ParseQuery<Child> q = new ParseQuery<Child>(Child.class);
        return q.find();
    }

    private void setup()
    {
        BaseApplication app = (BaseApplication) getContext().getApplicationContext();
        app.setupParse();
    }
}
