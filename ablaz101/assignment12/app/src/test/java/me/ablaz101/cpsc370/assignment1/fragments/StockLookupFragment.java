package com.example.cpsc.assignment1.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cpsc.assignment1.R;

import me.ablaz101.cpsc.assignment1.LookupBackgroundTask;
import me.ablaz101.cpsc.assignment1.MainActivity;
import me.ablaz101.cpsc.assignment1.QuoteBackgroundTask;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class StockLookupFragment extends Fragment
{
    EditText companyField;
    ListView companyList;
    TextView hiView;
    TextView lowView;
    TextView openView;

    public StockLookupFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock_lookup, container, false);

        companyField = (EditText) rootView.findViewById(R.id.et_company_name);
        hiView = (TextView) rootView.findViewById(R.id.tv_high);
        lowView = (TextView) rootView.findViewById(R.id.tv_low);
        openView = (TextView) rootView.findViewById(R.id.tv_open);

        companyList = (ListView) rootView.findViewById(R.id.lv_lookups);
        companyList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_symbol = (TextView)view.findViewById(R.id.tv_symbol);
                String symbol = tv_symbol.getText().toString();


                // TODO background task
                new QuoteBackgroundTask(hiView, lowView, openView).execute(symbol);
            }
        });

        Button submitButton = (Button) rootView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String companyName = companyField.getText().toString();
                if (!companyName.isEmpty())
                {
                    // TODO background task
                     new LookupBackgroundTask(v.getContext(), companyList).execute(companyName);
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
