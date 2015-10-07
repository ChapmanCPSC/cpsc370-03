package richt111.nutritioninfo.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import richt111.nutritioninfo.models.FoodItemModel;

public class FoodDataProvider {

    private static SQLiteDatabase GetDB(boolean writable, Context context) {

        FoodDatabase helper = new FoodDatabase(context);

        if(writable)
            return helper.getWritableDatabase();
        else
            return helper.getReadableDatabase();

    }

    public static void InsertFoodItem(FoodItemModel model, Context context) {

        SQLiteDatabase db = GetDB(true, context);

        // Add item to table
        ContentValues cv = FoodItemEntry.MakeContentValues(model);
        db.insert(FoodItemEntry.TABLE_NAME, null, cv);

        db.close();

    }

    public static List<FoodItemModel> GetFoodItems(Context context) {

        SQLiteDatabase db = GetDB(false, context);
        List<FoodItemModel> foodItems = new ArrayList<>();
        Cursor c = db.query(FoodItemEntry.TABLE_NAME, null, null, null, null, null, null);

        // Get all items and add to list
        while(c.moveToNext()) {
            FoodItemModel foodItem = FoodItemEntry.MakeModel(c);
            foodItems.add(foodItem);
        }

        c.close();
        db.close();

        return foodItems;
    }

    public static void ClearDatabase(Context context) {

        // Clear table in database
        SQLiteDatabase db = GetDB(true, context);
        db.delete(FoodItemEntry.TABLE_NAME, null, null);
        db.close();
    }

}
