package com.example.cpsc.demoapplication.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cpsc.demoapplication.activities.MainActivity;
import com.example.cpsc.demoapplication.R;
import com.example.cpsc.demoapplication.db.WeatherDataProvider;
import com.example.cpsc.demoapplication.models.RecentLocationModel;
import com.example.cpsc.demoapplication.models.WeatherResultModel;
import com.example.cpsc.demoapplication.tasks.GetWeatherTask;

/**
 * Created by cpsc on 9/14/15.
 */
public class WeatherFragment extends Fragment
{
    private final String ZIP_KEY = "zip";

    EditText zipField;
    TextView areaView;
    TextView tempView;
    TextView hiView;
    TextView lowView;
    ProgressBar loadingView;

    SharedPreferences prefs;

    public WeatherFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        zipField = (EditText) rootView.findViewById(R.id.et_zip);

        String storedZip = prefs.getString(ZIP_KEY, "");
        zipField.setText(storedZip);

        areaView = (TextView) rootView.findViewById(R.id.tv_area);
        tempView = (TextView) rootView.findViewById(R.id.tv_temp);
        hiView = (TextView) rootView.findViewById(R.id.tv_high);
        lowView = (TextView) rootView.findViewById(R.id.tv_low);
        loadingView = (ProgressBar) rootView.findViewById(R.id.pb_loading);

        Button submitButton = (Button) rootView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String zip = zipField.getText().toString();
                if (!zip.isEmpty())
                {
                    //store it in sharedprefs
                    storeZip(zip);

                    //store a recent location in DB
                    RecentLocationModel location = new RecentLocationModel(zip);
                    WeatherDataProvider.InsertRecentLocation(location, getActivity());

                    final GetWeatherTask weatherTask = new GetWeatherTask(getActivity());
                    weatherTask.onFinish = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            loadingFinished(weatherTask._result);
                        }
                    };
                    loadingStarted();
                    weatherTask.execute(zip);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    public void loadingStarted()
    {
        loadingView.setVisibility(View.VISIBLE);
    }

    public void loadingFinished(WeatherResultModel result)
    {
        loadingView.setVisibility(View.INVISIBLE);

        areaView.setText(result.name);
        tempView.setText(String.valueOf(result.main.temp));
        hiView.setText(String.valueOf(result.main.temp_max));
        lowView.setText(String.valueOf(result.main.temp_min));
    }

    private void storeZip(String zip)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ZIP_KEY, zip);
        editor.commit();
    }
}
