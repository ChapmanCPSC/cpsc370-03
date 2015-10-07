package richt111.nutritioninfo.models;


public class FoodItemModel {

    private int id;

    private String name;
    private float calories;
    private float fat;
    private float carbs;
    private float protein;
    private float sugar;
    private float fiber;
    private float sodium;
    private float cholesterol;

    public FoodItemModel(String name, float calories, float fat, float carbs, float protein,
                         float sugar, float fiber, float sodium, float cholesterol) {
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
        this.sugar = sugar;
        this.fiber = fiber;
        this.sodium = sodium;
        this.cholesterol = cholesterol;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSugar() {
        return String.valueOf(sugar);
    }

    public String getFiber() {
        return String.valueOf(fiber);
    }

    public String getSodium() {
        return String.valueOf(sodium);
    }

    public String getCholesterol() {
        return String.valueOf(cholesterol);
    }

}
