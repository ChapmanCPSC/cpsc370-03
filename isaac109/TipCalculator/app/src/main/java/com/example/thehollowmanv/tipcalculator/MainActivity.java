package com.example.thehollowmanv.tipcalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    int rating = 0;
    TextView total;
    TextView tip;
    EditText cost;
    double costNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        cost = (EditText)findViewById(R.id.costAnswer);
        tip = (TextView)findViewById(R.id.tip);
        total = (TextView)findViewById(R.id.total);
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                        + " : "
                        + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT)
                .show();
                rating = childPosition;
                expListView.collapseGroup(groupPosition);

                return false;
            }
        });

        cost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(!cost.getText().toString().equals("")) {
                        costNumber = Double.parseDouble(cost.getText().toString());
                        getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        calculate();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please enter a valid cost",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }

    private void calculate()
    {
        double tipPer = 0.0;
        switch(rating){
            case 0:
                tipPer = 0.0;
                break;
            case 1:
                tipPer = .075;
                break;
            case 2:
                tipPer = .15;
                break;
            case 3:
                tipPer = .20;
                break;
            case 4:
                tipPer = .25;
                break;
        }
        double tipNum = tipPer * costNumber;
        double totalnum = tipNum + costNumber;
        StringBuilder sb = new StringBuilder();
        sb.append(tipNum);
        tip.setText(" $" + sb.toString());
        sb = new StringBuilder();
        sb.append(totalnum);
        total.setText(" $" + sb.toString());
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("How was the Service?");

        // Adding child data
        List<String> rating = new ArrayList<String>();
        rating.add("Terrible");
        rating.add("Poor");
        rating.add("Okay");
        rating.add("Great");
        rating.add("Fantastic");


        listDataChild.put(listDataHeader.get(0), rating); // Header, Child data
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
