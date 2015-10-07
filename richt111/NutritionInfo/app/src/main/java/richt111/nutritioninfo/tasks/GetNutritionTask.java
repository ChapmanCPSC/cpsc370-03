package richt111.nutritioninfo.tasks;


import android.os.AsyncTask;

import richt111.nutritioninfo.NutritionApiWrapper;
import richt111.nutritioninfo.fragments.SearchFragment;
import richt111.nutritioninfo.models.NutritionResultModel;

public class GetNutritionTask extends AsyncTask<String, Void, NutritionResultModel> {

    private SearchFragment fragment;

    public GetNutritionTask(SearchFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        fragment.loadingStarted();
    }

    @Override
    protected NutritionResultModel doInBackground(String... params) {

        NutritionResultModel model = NutritionApiWrapper.GetNutritionInfo(params[0]);
        return model;
    }

    @Override
    protected void onPostExecute(NutritionResultModel nutritionResultModel) {
        super.onPostExecute(nutritionResultModel);

        fragment.loadingFinished(nutritionResultModel);
    }
}
