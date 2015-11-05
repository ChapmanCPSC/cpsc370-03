package edu.chapman.cpsc370.asdplaydate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.chapman.cpsc370.asdplaydate.ChatRequestListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;

public class InboxFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public InboxFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_chat_request_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        //TODO: Data set

        ChatRequestListRecyclerItem[] myDataSet= {
                new ChatRequestListRecyclerItem("John Smith", "I have a red Angels hat on", true),
                new ChatRequestListRecyclerItem("Carry Johnson", "Would like to chat", false)
        };

        mAdapter = new ChatRequestListRecyclerAdapter(myDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }
}
