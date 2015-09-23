package richt111.nutritioninfo;

import com.google.gson.Gson;

import java.io.IOException;

import richt111.nutritioninfo.models.NutritionResultModel;


public class NutritionApiWrapper {

    private static String baseUrl = "https://nutritionix-api.p.mashape.com/v1_1/search/";

    public static NutritionResultModel GetNutritionInfo(String food) {

        String parameters[] = {"item_name", "nf_calories", "nf_total_fat", "nf_total_carbohydrate",
            "nf_protein"};

        String queryString = "?fields=";
        String response = "";

        // Separate each parameter with %2C
        for(int i = 0; i < parameters.length; i++)
            queryString += parameters[i] + "%2C";

        // Replace any spaces in food string with %20
        food = food.replace(" ", "%20");

        String fullUrl = baseUrl + food + queryString;

        try {
            // Set request url and add API key
            WebRequest request = new WebRequest(fullUrl);
            request.setHeader(new String[] {"X-Mashape-Key", "Accept"},
                    new String[] {"Ttrxle4TAcmshlSNl97BbjR5n9uop1oQ6kFjsnvwzKvSHCXd2i", "application/json"});

            response = request.get();

            NutritionResultModel model = new Gson().fromJson(response, NutritionResultModel.class);

            return model;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
