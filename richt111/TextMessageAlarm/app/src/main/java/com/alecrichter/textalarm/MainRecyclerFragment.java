package com.alecrichter.textalarm;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.alecrichter.textalarm.util.RecyclerScroll;

import java.util.List;

public class MainRecyclerFragment extends Fragment{

    private AlarmRecyclerAdapter adapter;
    private AlarmDatabase db;
    private RecyclerView rv;
    private List<Alarm> currentAlarms;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new AlarmDatabase(getActivity(), AlarmDatabase.databaseName, null, AlarmDatabase.databaseVersion);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.newAlarm_FAB);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_recycler, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setList();
        toggleEmptyMessage();
    }

    public void setList() {

        setAdapter();

        //Update data set
        adapter.notifyDataSetChanged();
    }

    public void addAlarm(Alarm alarm, int position) {

        //setAdapter();

        adapter.add(alarm, position);
    }

    public void removeAlarm(Alarm alarm, int position) {

        //setAdapter();

        adapter.remove(alarm, position);
    }

    private void setAdapter() {
        rv = (RecyclerView) getActivity().findViewById(R.id.rv);

        // Add show and hide animation to FAB
        rv.setOnScrollListener(new RecyclerScroll() {
            @Override
            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                fab.animate().translationY(fab.getHeight() + 50).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        //Get list of alarms
        currentAlarms = db.getAllAlarms();

        //Set adapter
        adapter = new AlarmRecyclerAdapter(currentAlarms, (MainActivity) getActivity(), getActivity());
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    private void toggleEmptyMessage() {

        //Get empty message
        TextView emptyMessage = (TextView) getActivity().findViewById(R.id.main_empty_message);

        //If list is empty, show empty message
        if(currentAlarms.isEmpty())
            emptyMessage.setVisibility(View.VISIBLE);
        else
            emptyMessage.setVisibility(View.INVISIBLE);
    }

}
