package me.ablaz101.cpsc370.assignment1;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.TestCase;

import me.ablaz101.cpsc370.assignment1.models.LookupResultModel;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class StockInfoAPIWrapperTest extends TestCase
{
    public void test() {
        LookupResultModel[] lookupResults = StockInfoAPIWrapper.GetLookupResults("Apple");
        System.out.println(lookupResults[0].symbol);
    }
}