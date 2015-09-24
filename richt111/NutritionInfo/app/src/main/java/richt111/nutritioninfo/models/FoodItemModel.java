package richt111.nutritioninfo.models;


public class FoodItemModel {

    private String name;
    private float calories;
    private float fat;
    private float carbs;
    private float protein;

    public FoodItemModel(String name, float calories, float fat, float carbs, float protein) {
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return String.valueOf(calories);
    }

    public String getFat() {
        return String.valueOf(fat);
    }

    public String getCarbs() {
        return String.valueOf(carbs);
    }

    public String getProtein() {
        return String.valueOf(protein);
    }
}
