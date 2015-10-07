package richt111.nutritioninfo;


import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import richt111.nutritioninfo.db.FoodDataProvider;
import richt111.nutritioninfo.models.FoodItemModel;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder> {

    private List<FoodItemModel> foodItems;
    private Activity activity;


    public FoodRecyclerAdapter(List<FoodItemModel> foodItems, Activity activity) {
        this.foodItems = foodItems;
        this.activity = activity;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_food_item, viewGroup, false);
        FoodViewHolder vh = new FoodViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder foodViewHolder, final int i) {

        // Set values for each text field
        foodViewHolder.title.setText(foodItems.get(i).getName());
        foodViewHolder.calories.setText("Calories: " + foodItems.get(i).getCalories());
        foodViewHolder.fat.setText("Fat: " + foodItems.get(i).getFat() + "g");
        foodViewHolder.carbs.setText("Carbohydrates: " + foodItems.get(i).getCarbs() + "g");
        foodViewHolder.protein.setText("Protein: " + foodItems.get(i).getProtein() + "g");

        foodViewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodDataProvider.InsertFoodItem(foodItems.get(i), activity);
                Toast.makeText(activity, "Saved to daily total", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }


    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView calories;
        TextView fat;
        TextView carbs;
        TextView protein;
        ImageView saveButton;

        FoodViewHolder(View itemView) {
            super(itemView);

            // Get objects from XML
            cv = (CardView) itemView.findViewById(R.id.cv_food_item);
            title = (TextView) itemView.findViewById(R.id.card_food_title);
            calories = (TextView) itemView.findViewById(R.id.card_food_calories);
            fat = (TextView) itemView.findViewById(R.id.card_food_fat);
            carbs = (TextView) itemView.findViewById(R.id.card_food_carbs);
            protein = (TextView) itemView.findViewById(R.id.card_food_protein);
            saveButton = (ImageView) itemView.findViewById(R.id.card_food_save);
        }

    }

}
