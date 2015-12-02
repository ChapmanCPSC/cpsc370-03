package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.BaseApplication;
import edu.chapman.cpsc370.asdplaydate.adapters.ChatRequestListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.models.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.SwipeableRecyclerViewTouchListener;
import edu.chapman.cpsc370.asdplaydate.tasks.GetMessagesTask;

public class InboxFragment extends Fragment
{
    public List<ChatRequestListRecyclerItem> mItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout swipeLayout;

    public InboxFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_chat_request_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_chatrequestlist);
        //btn_refresh = (Button) getActivity().findViewById(R.id.btn_refresh);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                final GetMessagesTask task = new GetMessagesTask(getActivity());
                task.onFinish = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mItems.clear();

                        for (ChatRequestListRecyclerItem item : task.mItems)
                        {
                            mItems.add(item);
                        }

                        if (mItems.size() == 0)
                        {
                            Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.no_convos_found), Toast.LENGTH_SHORT).show();
                        } else
                        {
                            if (BaseApplication.inDEBUGMode())
                            {
                                Toast.makeText(getActivity(), mItems.size() + " convos in Inbox", Toast.LENGTH_SHORT).show();
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                        swipeLayout.setRefreshing(false);
                    }
                };

                task.execute();


            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ChatRequestListRecyclerAdapter(mItems, this.getActivity());
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
                                return !mItems.get(position).isAccepted();
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

    @Override
    public void onResume()
    {
        super.onResume();


    }
}
