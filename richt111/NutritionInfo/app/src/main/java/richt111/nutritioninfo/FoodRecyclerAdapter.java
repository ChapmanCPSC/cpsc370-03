package richt111.nutritioninfo;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import richt111.nutritioninfo.models.FoodItemModel;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder> {

    private List<FoodItemModel> foodItems;


    FoodRecyclerAdapter(List<FoodItemModel> foodItems) {
        this.foodItems = foodItems;
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

        FoodViewHolder(View itemView) {
            super(itemView);

            // Get objects from XML
            cv = (CardView) itemView.findViewById(R.id.cv_food_item);
            title = (TextView) itemView.findViewById(R.id.card_food_title);
            calories = (TextView) itemView.findViewById(R.id.card_food_calories);
            fat = (TextView) itemView.findViewById(R.id.card_food_fat);
            carbs = (TextView) itemView.findViewById(R.id.card_food_carbs);
            protein = (TextView) itemView.findViewById(R.id.card_food_protein);
        }

    }

}
