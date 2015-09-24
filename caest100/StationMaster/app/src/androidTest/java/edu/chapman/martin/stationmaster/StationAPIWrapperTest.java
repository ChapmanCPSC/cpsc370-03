package edu.chapman.martin.stationmaster;

import junit.framework.TestCase;

import edu.chapman.martin.stationmaster.models.StationStatusResultModel;

/**
 * Created by Martin on 9/21/2015.
 */
public class StationAPIWrapperTest extends TestCase {

    public void testGetArrivals() throws Exception {
        StationStatusResultModel stationResult = StationAPIWrapper.GetArrivals("LAX", "PT");

    }
}