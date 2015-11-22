package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.ResultListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

public class ResultListFragment extends Fragment
{

    private RecyclerView recyclerView;
    private ResultListRecyclerAdapter adapter;
    FindFragmentContainer parent;

    public ResultListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_result_list, container, false);
        parent = (FindFragmentContainer) getParentFragment();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Set FAB on click listener
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.result_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFragmentContainer fragment = (FindFragmentContainer) getParentFragment();
                fragment.flipFragment();
            }
        });

        // Set layout manager for recycler view
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.result_list_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        setRecyclerAdapter(parent.broadcasts);
    }

    private void setRecyclerAdapter(HashMap<LatLng, MarkerLabelInfo> data)
    {
        adapter = new ResultListRecyclerAdapter(getActivity(), parent.myLocation, data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
