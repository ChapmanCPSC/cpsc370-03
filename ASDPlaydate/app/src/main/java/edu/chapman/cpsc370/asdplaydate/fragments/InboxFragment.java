package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import java.util.ArrayList;
import java.util.List;
import edu.chapman.cpsc370.asdplaydate.adapters.ChatRequestListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.helpers.RecyclerAdapterHelpers;
import edu.chapman.cpsc370.asdplaydate.models.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.SwipeableRecyclerViewTouchListener;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.tasks.GetMessagesTask;

public class InboxFragment extends Fragment
{
    public List<ChatRequestListRecyclerItem> mItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout swipeLayout;
    private TextView noConvos;

    public static InboxFragment inboxFragment;

    public InboxFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_chat_request_list, container, false);
    }

    public void refresh()
    {
        final GetMessagesTask task = new GetMessagesTask(getActivity());
        task.onFinish = new Runnable()
        {
            @Override
            public void run()
            {
                mItems.clear();

                if (task.mItems.size() > 0)
                {
                    noConvos.setVisibility(View.INVISIBLE);
                    for (ChatRequestListRecyclerItem item : task.mItems)
                    {
                        try
                        {
                            Conversation conversation = Conversation.getConversation(item.getConversationID());
                            if (conversation.getExpireDate().isAfter(DateTime.now().toDateTime(DateTimeZone.UTC)))
                            {
                                mItems.add(item);
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                } else
                {
                    noConvos.setVisibility(View.VISIBLE);
                }

                mAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        };

        task.execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        inboxFragment = this;
        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        noConvos = (TextView) getActivity().findViewById(R.id.tv_noConvos);
        RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_chatrequestlist);
        //btn_refresh = (Button) getActivity().findViewById(R.id.btn_refresh);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refresh();
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
                                    RecyclerAdapterHelpers.denyRequest(mItems.get(position));
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
                                    RecyclerAdapterHelpers.denyRequest(mItems.get(position));
                                    mItems.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
        refresh();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        refresh();
    }

    public void onStart()
    {
        super.onStart();

        refresh();
    }
}
