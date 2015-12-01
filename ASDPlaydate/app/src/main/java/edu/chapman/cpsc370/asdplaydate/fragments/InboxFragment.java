package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.adapters.ChatRequestListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.SwipeableRecyclerViewTouchListener;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;

public class InboxFragment extends Fragment
{
    public List<ChatRequestListRecyclerItem> mItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btn_refresh;

    public InboxFragment()
    {
        /*ASDPlaydateUser currentUser = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();

        if (!currentUser.equals(null)){
            //Toast.makeText(getActivity(), "current user is ok", Toast.LENGTH_SHORT).show();
            try
            {
                getConversations();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_chat_request_list, container, false);
    }

    public void getConversations() throws Exception
    {
        //receiver from testSendChatInvitation()
        SessionManager sessionManager = new SessionManager(getActivity());
        ASDPlaydateUser me = (ASDPlaydateUser) ASDPlaydateUser.become(sessionManager.getSessionToken());

        ParseQuery<Conversation> meReceive = new ParseQuery<>(Conversation.class);
        meReceive.whereEqualTo(Conversation.ATTR_RECEIVER, me);

        ParseQuery<Conversation> meSend = new ParseQuery<>(Conversation.class);
        meSend.whereEqualTo(Conversation.ATTR_INITIATOR, me);

        List<ParseQuery<Conversation>> both = new ArrayList<>();
        both.add(meReceive);
        both.add(meSend);

        ParseQuery<Conversation> meSendOrMeReceive = ParseQuery.or(both);
        meSendOrMeReceive.whereNotEqualTo(Conversation.ATTR_STATUS, Conversation.Status.DENIED.name());
        meSendOrMeReceive.whereGreaterThan(Conversation.ATTR_EXPIRE_DATE, DateTime.now(DateTimeZone.UTC).toDate());

        List<Conversation> displayConvos = meSendOrMeReceive.find();

        mItems.clear();
        for (Conversation c : displayConvos)
        {
            boolean accepted = false;
            if (c.getStatus().toString().equals(Conversation.Status.ACCEPTED.name()))
                accepted = true;

            ASDPlaydateUser initiator = c.getInitiator();
            ASDPlaydateUser receiver = c.getReceiver();

            if (initiator.equals(me))
            {
                String firstName = "";
                String lastName = "";
                try
                {
                    firstName = receiver.fetchIfNeeded().getString("first_name");
                    lastName = receiver.fetchIfNeeded().getString("last_name");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                mItems.add(new ChatRequestListRecyclerItem(c.getObjectId(), firstName + " " + lastName, null, accepted));
            } else
            {
                String firstName = "";
                String lastName = "";
                try
                {
                    firstName = initiator.fetchIfNeeded().getString("first_name");
                    lastName = initiator.fetchIfNeeded().getString("last_name");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                mItems.add(new ChatRequestListRecyclerItem(c.getObjectId(), firstName + " " + lastName, null, accepted));
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_chatrequestlist);
        btn_refresh = (Button) getActivity().findViewById(R.id.btn_refresh);

        btn_refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    getConversations();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        //TODO: Get data from somewhere here


        //Martin 11/8/15 added userID to recycler item for future use by chat activity
        /*mItems.add(new ChatRequestListRecyclerItem(1234, "John Smith", "I have a red Angels hat on", true));
        mItems.add(new ChatRequestListRecyclerItem(1235, "Carry Johnson", "Would like to chat", false));
        mItems.add(new ChatRequestListRecyclerItem(1236, "Faia Raige", "That is awesome!", true));
        mItems.add(new ChatRequestListRecyclerItem(1237, "Chris Shiherlis ", "Would like to chat", false));
        mItems.add(new ChatRequestListRecyclerItem(1238, "Terrence Fletcher", "Were you dragging or rushing?", true));*/

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
