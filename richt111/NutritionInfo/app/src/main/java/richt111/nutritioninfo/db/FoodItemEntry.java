package richt111.nutritioninfo.db;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import richt111.nutritioninfo.models.FoodItemModel;

public class FoodItemEntry implements BaseColumns {

    public static final String TABLE_NAME = "daily_food_items";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_CARBS = "carbs";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_SUGAR = "sugar";
    public static final String COLUMN_FIBER = "fiber";
    public static final String COLUMN_SODIUM = "sodium";
    public static final String COLUMN_CHOLESTEROL = "cholesterol";

    public static final String CREATE_COMMAND = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INT PRIMARY KEY, "
            + COLUMN_NAME + " REAL, "
            + COLUMN_CALORIES + " REAL, "
            + COLUMN_FAT + " REAL, "
            + COLUMN_CARBS + " REAL, "
            + COLUMN_PROTEIN + " REAL, "
            + COLUMN_SUGAR + " REAL, "
            + COLUMN_FIBER + " REAL, "
            + COLUMN_SODIUM + " REAL, "
            + COLUMN_CHOLESTEROL + " REAL);";

    public static ContentValues MakeContentValues(FoodItemModel model) {

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, model.getName());
        cv.put(COLUMN_CALORIES, model.getCalories());
        cv.put(COLUMN_FAT, model.getFat());
        cv.put(COLUMN_CARBS, model.getCarbs());
        cv.put(COLUMN_PROTEIN, model.getProtein());
        cv.put(COLUMN_SUGAR, model.getSugar());
        cv.put(COLUMN_FIBER, model.getFiber());
        cv.put(COLUMN_SODIUM, model.getSodium());
        cv.put(COLUMN_CHOLESTEROL, model.getCholesterol());

        return cv;
    }

    public static FoodItemModel MakeModel(Cursor c) {

        int id = c.getInt(c.getColumnIndex(COLUMN_ID));

        String name = c.getString(c.getColumnIndex(COLUMN_NAME));
        float calories = c.getFloat(c.getColumnIndex(COLUMN_CALORIES));
        float fat = c.getFloat(c.getColumnIndex(COLUMN_FAT));
        float carbs = c.getFloat(c.getColumnIndex(COLUMN_CARBS));
        float protein = c.getFloat(c.getColumnIndex(COLUMN_PROTEIN));
        float sugar = c.getFloat(c.getColumnIndex(COLUMN_SUGAR));
        float fiber = c.getFloat(c.getColumnIndex(COLUMN_FIBER));
        float sodium = c.getFloat(c.getColumnIndex(COLUMN_SODIUM));
        float cholesterol = c.getFloat(c.getColumnIndex(COLUMN_CHOLESTEROL));

        FoodItemModel model = new FoodItemModel(name, calories, fat, carbs, protein, sugar, fiber,
                sodium, cholesterol);

        model.setId(id);

        return model;
    }

}
