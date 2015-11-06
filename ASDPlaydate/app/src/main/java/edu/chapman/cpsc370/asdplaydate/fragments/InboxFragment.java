package edu.chapman.cpsc370.asdplaydate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.adapters.ChatRequestListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.SwipeableRecyclerViewTouchListener;

public class InboxFragment extends Fragment
{
    private List<ChatRequestListRecyclerItem> mItems = new ArrayList<ChatRequestListRecyclerItem>();
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

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_chatrequestlist);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        //TODO: Get data from somewhere here
        mItems.add(new ChatRequestListRecyclerItem("John Smith", "I have a red Angels hat on", true));
        mItems.add(new ChatRequestListRecyclerItem("Carry Johnson", "Would like to chat", false));
        mItems.add(new ChatRequestListRecyclerItem("Faia K", "That is awesome!", true));
        mItems.add(new ChatRequestListRecyclerItem("Chris Shirelis ", "Would like to chat", false));
        mItems.add(new ChatRequestListRecyclerItem("Terrence Fletcher", "Were you dragging or rushing?", true));

        mAdapter = new ChatRequestListRecyclerAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener()
                        {

                            @Override
                            public boolean canSwipeLeft(int position)
                            {
                                return false;
                            }

                            @Override
                            public boolean canSwipeRight(int position)
                            {
                                if (mItems.get(position).isHasAccepted())
                                {
                                    return false;
                                }
                                else
                                {
                                    return true;
                                }
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions)
                            {
                                for (int position : reverseSortedPositions)
                                {
                                    mItems.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions)
                            {
                                for (int position : reverseSortedPositions)
                                {
                                    mItems.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }
}
