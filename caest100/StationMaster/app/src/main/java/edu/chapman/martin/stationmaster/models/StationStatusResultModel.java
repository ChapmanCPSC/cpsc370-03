package edu.chapman.martin.stationmaster.models;

/**
 * Created by Martin on 9/21/2015.
 */
public class StationStatusResultModel {
    public StationResponseModel response;

    public class StationResponseModel{
        public StationResultsModel results[];

        public class StationResultsModel{
            public TrainData data[];
        }
    }

}
