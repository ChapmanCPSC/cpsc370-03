package edu.chapman.cpsc370.asdplaydate.parse;

import android.test.AndroidTestCase;

import edu.chapman.cpsc370.asdplaydate.BaseApplication;

/**
 * Created by ryanburns on 11/7/15.
 */
public class ParseTest extends AndroidTestCase
{
    protected final String LOGTAG = "ParseTest";
    protected final String TEST_SESSION = "r:ZRl9rxwXytLShOWg7j8euzw2t";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        BaseApplication app = (BaseApplication) getContext().getApplicationContext();
        app.setupParse();
    }
}
