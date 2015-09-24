package com.example.cpsc.lienapp;

import com.example.cpsc.lienapp.models.NameResultModel;
import com.google.gson.Gson;

public class NameAPIWrapper
{
    private static String baseUrl = "https://gender-api.com/get?name=";//static portion of web API url
    private static String apiKey = "&key=FxfclXCkKNtMnFCsFF";

    public static NameResultModel GetNameInfo(String name)//name is passed in here
    {
        String queryString = name+apiKey;
        String fullUrl = baseUrl+queryString;

        String response="";
        try
        {
            response = new WebRequest(fullUrl).get();
            NameResultModel model = new Gson().fromJson(response, NameResultModel.class);
            return model;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
