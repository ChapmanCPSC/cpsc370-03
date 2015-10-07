package richt111.nutritioninfo.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import richt111.nutritioninfo.R;
import richt111.nutritioninfo.db.FoodDataProvider;
import richt111.nutritioninfo.models.FoodItemModel;

public class TotalsFragment extends android.support.v4.app.Fragment {

    private List<FoodItemModel> foodItems;

    public TotalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_totals, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get initial data if there is any
        updateData();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.totals_fragment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show dialog window to confirm removing current data
                new MaterialDialog.Builder(getActivity())
                        .content("Clear today's food?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                FoodDataProvider.ClearDatabase(getContext());
                                updateData();
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Update data when the tab is visible
        if(this.isVisible()) {
            updateData();
        }
    }

    private void updateData() {

        // Get saved items from database
        foodItems = FoodDataProvider.GetFoodItems(getContext());

        TextView itemNames = (TextView) getActivity().findViewById(R.id.totals_names);
        TextView caloriesView = (TextView) getActivity().findViewById(R.id.totals_calories);
        TextView fatView = (TextView) getActivity().findViewById(R.id.totals_fat);
        TextView carbsView = (TextView) getActivity().findViewById(R.id.totals_carbs);
        TextView fiberView = (TextView) getActivity().findViewById(R.id.totals_fiber);
        TextView sugarView = (TextView) getActivity().findViewById(R.id.totals_sugar);
        TextView proteinView = (TextView) getActivity().findViewById(R.id.totals_protein);
        TextView sodiumView = (TextView) getActivity().findViewById(R.id.totals_sodium);
        TextView cholesterolView = (TextView) getActivity().findViewById(R.id.totals_cholesterol);

        String names = "";

        float calories = 0, fat = 0, carbs = 0, fiber = 0, sugar = 0, protein = 0, sodium = 0,
                cholesterol = 0;

        // Get data from saved food items
        for(int i = 0; i < foodItems.size(); i++) {
            names += i+1 + ".) " + foodItems.get(i).getName() + "\n";

            calories += Float.valueOf(foodItems.get(i).getCalories());
            fat += Float.valueOf(foodItems.get(i).getFat());
            carbs += Float.valueOf(foodItems.get(i).getCarbs());
            fiber += Float.valueOf(foodItems.get(i).getFiber());
            sugar += Float.valueOf(foodItems.get(i).getSugar());
            protein += Float.valueOf(foodItems.get(i).getProtein());
            sodium += Float.valueOf(foodItems.get(i).getSodium());
            cholesterol += Float.valueOf(foodItems.get(i).getCholesterol());
        }

        // Set values
        itemNames.setText(names);
        caloriesView.setText("Calories: " + calories);
        fatView.setText("Total Fat: " + fat + "g");
        carbsView.setText("Total Carbohydrate: " + carbs + "g");
        fiberView.setText("Dietary Fiber: " + fiber + "g");
        sugarView.setText("Sugars: " + sugar + "g");
        proteinView.setText("Protein: " + protein + "g");
        sodiumView.setText("Sodium: " + sodium + "mg");
        cholesterolView.setText("Cholesterol: " + cholesterol + "mg");
    }
}
