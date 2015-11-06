package edu.chapman.cpsc370.asdplaydate.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.ResultListRecyclerAdapter;

public class ResultListFragment extends Fragment
{

    private RecyclerView recyclerView;
    private ResultListRecyclerAdapter adapter;

    public ResultListFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_result_list, container, false);
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
                Toast.makeText(getContext(), "This will go back to the map", Toast.LENGTH_SHORT).show();
            }
        });

        // Set layout manager for recycler view
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.result_list_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        setRecyclerAdapter();
    }

    private void setRecyclerAdapter()
    {
        adapter = new ResultListRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}
