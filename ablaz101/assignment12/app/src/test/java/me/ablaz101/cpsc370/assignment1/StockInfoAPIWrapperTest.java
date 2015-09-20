package com.example.cpsc.assignment1;

import me.ablaz101.cpsc.assignment1.StockInfoAPIWrapper;
import me.ablaz101.cpsc.assignment1.models.LookupResultModel;
import me.ablaz101.cpsc.assignment1.models.QuoteModel;

import junit.framework.TestCase;

/**
 * Created by cpsc on 9/9/15.
 */
public class WeatherAPIWrapperTest extends TestCase
{
    public void testGetCurrentWeather() throws Exception
    {
        LookupResultModel[] model = StockInfoAPIWrapper.GetLookupResults("Apple");
        System.out.println(model[0].name);
    }
}