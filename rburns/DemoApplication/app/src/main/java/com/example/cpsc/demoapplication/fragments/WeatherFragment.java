package com.example.cpsc.demoapplication.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cpsc.demoapplication.MainActivity;
import com.example.cpsc.demoapplication.R;
import com.example.cpsc.demoapplication.WeatherAPIWrapper;
import com.example.cpsc.demoapplication.models.WeatherResultModel;

/**
 * Created by cpsc on 9/14/15.
 */
public class WeatherFragment extends Fragment
{
    EditText zipField;
    TextView areaView;
    TextView tempView;
    TextView hiView;
    TextView lowView;

    public WeatherFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        zipField = (EditText) rootView.findViewById(R.id.et_zip);
        areaView = (TextView) rootView.findViewById(R.id.tv_area);
        tempView = (TextView) rootView.findViewById(R.id.tv_temp);
        hiView = (TextView) rootView.findViewById(R.id.tv_high);
        lowView = (TextView) rootView.findViewById(R.id.tv_low);

        Button submitButton = (Button) rootView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String zip = zipField.getText().toString();
                if (!zip.isEmpty())
                {
                    WeatherResultModel weatherResult = WeatherAPIWrapper.GetCurrentWeather(zip);
                    tempView.setText(String.valueOf(weatherResult.main.temp));
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
}
