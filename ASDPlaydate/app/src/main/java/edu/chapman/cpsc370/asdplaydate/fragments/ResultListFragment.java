package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.ResultListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

public class ResultListFragment extends Fragment
{

    private RecyclerView recyclerView;
    public static TextView noResults;
    FindFragmentContainer parent;
    SessionManager sm;

    public ResultListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_result_list, container, false);
        parent = (FindFragmentContainer) getParentFragment();
        noResults = (TextView) rootView.findViewById(R.id.tv_no_results);
        noResults.setVisibility(View.GONE);
        sm = parent.sessionManager;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Set FAB on click listener
        FloatingActionButton fab1 = (FloatingActionButton) getActivity().findViewById(R.id.result_list_fab);
        fab1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FindFragmentContainer fragment = (FindFragmentContainer) getParentFragment();
                fragment.flipFragment();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) getActivity().findViewById(R.id.fab_refresh_map);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFragmentContainer parent = (FindFragmentContainer) getParentFragment();
                try
                {
                    // Get broadcasts here
                    parent.updateUI();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                parent.listAdapter.notifyDataSetChanged();
            }
        });

        // Set layout manager for recycler view
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.result_list_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        setRecyclerAdapter(parent.broadcasts);
    }

    private void setRecyclerAdapter(ArrayList<MarkerLabelInfo> data)
    {
        parent.listAdapter = new ResultListRecyclerAdapter(getActivity(), this);
        recyclerView.setAdapter(parent.listAdapter);
    }
}
