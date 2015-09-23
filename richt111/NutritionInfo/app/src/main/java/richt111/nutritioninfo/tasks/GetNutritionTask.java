package richt111.nutritioninfo.tasks;


import android.os.AsyncTask;

import richt111.nutritioninfo.MainActivity;
import richt111.nutritioninfo.NutritionApiWrapper;
import richt111.nutritioninfo.models.NutritionResultModel;

public class GetNutritionTask extends AsyncTask<String, Void, NutritionResultModel> {

    private MainActivity activity;

    public GetNutritionTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        activity.loadingStarted();
    }

    @Override
    protected NutritionResultModel doInBackground(String... params) {

        NutritionResultModel model = NutritionApiWrapper.GetNutritionInfo(params[0]);
        return model;
    }

    @Override
    protected void onPostExecute(NutritionResultModel nutritionResultModel) {
        super.onPostExecute(nutritionResultModel);

        activity.loadingFinished(nutritionResultModel);
    }
}
