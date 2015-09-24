package com.example.kelly.gasmeup.models;

/**
 * Created by Kelly on 9/20/15.
 */
public class DirectionModel {

    /*DirectionGeocoderModel[] geocoder_status;

    public class DirectionGeocoderModel {



    }*/


    public  DirectionRoutesModel[] routes;

    public class DirectionRoutesModel{

        public DirectionLegsModel[] legs;

        public class DirectionLegsModel {

            public DirectionDistanceModel distance;
            public DirectionDurationModel duration;
            public DirectionEndLocationModel end_location;
            public String end_address;

            public class DirectionDistanceModel {

                public String text;
            }

            public class DirectionDurationModel {

                public String text;
            }

            public class DirectionEndLocationModel {

                public float lat;
                public float lng;
            }

        }




    }

    /*public class DirectionRoutesModel {

        public String copyrights;
    }*/


}
