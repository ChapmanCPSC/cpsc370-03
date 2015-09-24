package com.example.cpsc.lienapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cpsc.lienapp.MainActivity;
import com.example.cpsc.lienapp.R;
import com.example.cpsc.lienapp.models.NameResultModel;
import com.example.cpsc.lienapp.tasks.GetNameInfoTask;

public class NameSearchFragment extends Fragment
{
    EditText nameField;
    TextView nameView;
    TextView genderView;
    TextView accuracyView;
    TextView sampleView;
    ProgressBar loadingView;

    public NameSearchFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_namesearch, container, false);

        nameField = (EditText) rootView.findViewById(R.id.et_name);
        nameView = (TextView) rootView.findViewById(R.id.tv_name);
        genderView = (TextView) rootView.findViewById(R.id.tv_gender);
        accuracyView = (TextView) rootView.findViewById(R.id.tv_accuracy);
        sampleView = (TextView) rootView.findViewById(R.id.tv_sample);
        loadingView = (ProgressBar) rootView.findViewById(R.id.pb_loading);

        Button submitButton = (Button) rootView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String zip = nameField.getText().toString();
                if (!zip.isEmpty())
                {
                    GetNameInfoTask weatherTask = new GetNameInfoTask(NameSearchFragment.this);
                    weatherTask.execute(zip);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    public void loadingStarted()
    {
        loadingView.setVisibility(View.VISIBLE);
    }

    public void loadingFinished(NameResultModel result)
    {
        loadingView.setVisibility(View.INVISIBLE);

        nameView.setText(result.name);//display the name that was searched
        genderView.setText(String.valueOf(result.gender));//diplay the estimated gender
        accuracyView.setText(String.valueOf(result.accuracy));//display the accuracy of the gender estimate
        sampleView.setText(String.valueOf(result.samples));//display the size of the gender sample
    }
}
