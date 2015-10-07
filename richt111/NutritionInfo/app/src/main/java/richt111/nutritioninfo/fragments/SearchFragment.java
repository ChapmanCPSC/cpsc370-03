package richt111.nutritioninfo.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import richt111.nutritioninfo.FoodRecyclerAdapter;
import richt111.nutritioninfo.R;
import richt111.nutritioninfo.models.FoodItemModel;
import richt111.nutritioninfo.models.NutritionResultModel;
import richt111.nutritioninfo.tasks.GetNutritionTask;


public class SearchFragment extends android.support.v4.app.Fragment {

    private MaterialDialog loadingDialog;
    private RecyclerView rv;

    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set FAB listener
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.search_fragment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchDialog();
            }
        });

        rv = (RecyclerView) getActivity().findViewById(R.id.search_fragment_rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    private void openSearchDialog() {

        // Open a window to input the search information
        new MaterialDialog.Builder(getActivity())
                .title("Search")
                .content("Enter a food product")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("e.g. apple", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        String foodInput = dialog.getInputEditText().getText().toString().trim();

                        if(!foodInput.isEmpty()) {
                            GetNutritionTask task = new GetNutritionTask(SearchFragment.this);
                            task.execute(foodInput);
                        }
                    }
                }).show();
    }


    public void loadingStarted() {

        // Create progress dialog
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title("Getting Data").content("Please wait...").progress(true, 0).cancelable(false);
        loadingDialog = builder.build();

        // Show progress dialog while loading
        loadingDialog.show();

    }

    public void loadingFinished(NutritionResultModel model) {

        // Hide loading dialog
        loadingDialog.dismiss();

        List<FoodItemModel> foodList = new ArrayList<>();

        // Show at most 10 results
        int maxResults = 10;
        if(model.hits.length < maxResults)
            maxResults = model.hits.length;

        // Populate the list
        for(int i = 0; i < maxResults; i++) {

            FoodItemModel food = new FoodItemModel(
                    model.hits[i].fields.item_name,
                    model.hits[i].fields.nf_calories,
                    model.hits[i].fields.nf_total_fat,
                    model.hits[i].fields.nf_total_carbohydrate,
                    model.hits[i].fields.nf_protein,
                    model.hits[i].fields.nf_sugars,
                    model.hits[i].fields.nf_dietary_fiber,
                    model.hits[i].fields.nf_sodium,
                    model.hits[i].fields.nf_cholesterol);

            foodList.add(food);
        }

        //Set adapter
        FoodRecyclerAdapter adapter = new FoodRecyclerAdapter(foodList, getActivity());
        rv.setAdapter(adapter);

    }
}
