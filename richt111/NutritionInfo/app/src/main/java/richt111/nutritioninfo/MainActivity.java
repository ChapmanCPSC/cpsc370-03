package richt111.nutritioninfo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import richt111.nutritioninfo.models.FoodItemModel;
import richt111.nutritioninfo.models.NutritionResultModel;
import richt111.nutritioninfo.tasks.GetNutritionTask;

public class MainActivity extends AppCompatActivity {

    private MaterialDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Set FAB listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchDialog();
            }
        });

    }

    private void openSearchDialog() {

        // Open a window to input the search information
        new MaterialDialog.Builder(this)
                .title("Search")
                .content("Enter a food product")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("e.g. apple", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        String foodInput = dialog.getInputEditText().getText().toString().trim();

                        if(!foodInput.isEmpty()) {
                            GetNutritionTask task = new GetNutritionTask(MainActivity.this);
                            task.execute(foodInput);
                        }
                    }
                }).show();
    }


    public void loadingStarted() {

        // Create progress dialog
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title("Getting Data").content("Please wait...").progress(true, 0).cancelable(false);
        loadingDialog = builder.build();

        // Show progress dialog while loading
        loadingDialog.show();

    }

    public void loadingFinished(NutritionResultModel model) {

        // Hide loading dialog
        loadingDialog.dismiss();

        List<FoodItemModel> foodList = new ArrayList<>();

        // Show at most 5 results
        int maxResults = 5;
        if(model.hits.length < 5)
            maxResults = model.hits.length;

        // Populate the list
        for(int i = 0; i < maxResults; i++) {

            FoodItemModel food = new FoodItemModel(
                model.hits[i].fields.item_name,
                model.hits[i].fields.nf_calories,
                model.hits[i].fields.nf_total_fat,
                model.hits[i].fields.nf_total_carbohydrate,
                model.hits[i].fields.nf_total_protein);

            foodList.add(food);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.main_rv);

        //Set adapter
        FoodRecyclerAdapter adapter = new FoodRecyclerAdapter(foodList);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

    }

}
