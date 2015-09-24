package com.example.kelly.gasmeup.wrappers;

import com.example.kelly.gasmeup.WebRequest;
import com.example.kelly.gasmeup.models.DirectionModel;
import com.google.gson.Gson;

/**
 * Created by Kelly on 9/20/15.
 */
public class DirectionsAPIWrapper {

    private static String baseUrl = "https://maps.googleapis.com/maps/api/directions/json?";

    public static DirectionModel GetDistance(String origin, String destination){

        String queryString = "origin="+origin+"&destination="+destination+"&key=AIzaSyBSTCRmJLx88qGyXe9elDOQScB6doPaEb0";
        String fullUrl = baseUrl+queryString;

        String response="";
        try{
            response = new WebRequest(fullUrl).get();
            DirectionModel model = new Gson().fromJson(response,DirectionModel.class);
            //String test = model.routes.legs.distance.text;
            return model;

        } catch (Exception e ){
            e.printStackTrace();
            return null;
        }
    }
}
