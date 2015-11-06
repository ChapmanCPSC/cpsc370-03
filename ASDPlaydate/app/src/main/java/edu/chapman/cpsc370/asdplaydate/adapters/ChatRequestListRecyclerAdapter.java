package edu.chapman.cpsc370.asdplaydate.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.chapman.cpsc370.asdplaydate.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;

public class ChatRequestListRecyclerAdapter extends RecyclerView.Adapter<ChatRequestListRecyclerAdapter.ViewHolder>
{
    private List<ChatRequestListRecyclerItem> mItems;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView parentName;
        public TextView lastMsg;
        public LinearLayout buttons;

        public ViewHolder(View v)
        {
            super(v);

            this.parentName = (TextView) v.findViewById(R.id.tv_parent_name);
            this.lastMsg = (TextView) v.findViewById(R.id.tv_last_message);
            this.buttons = (LinearLayout) v.findViewById(R.id.ll_chatrequestlist_buttons);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatRequestListRecyclerAdapter(List<ChatRequestListRecyclerItem> mItems)
    {
        this.mItems = mItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatRequestListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType)
    {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_chatrequest_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        disableTouchTheft(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static void disableTouchTheft(View view)
    {
        view.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mCardView.setText(mDataset[position]);

        //TODO: Fill data here
        holder.parentName.setText(mItems.get(position).getParentName());
        holder.lastMsg.setText(mItems.get(position).getLastMsg());

        if (mItems.get(position).isHasAccepted())
        {
            holder.buttons.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mItems.size();
    }
}
