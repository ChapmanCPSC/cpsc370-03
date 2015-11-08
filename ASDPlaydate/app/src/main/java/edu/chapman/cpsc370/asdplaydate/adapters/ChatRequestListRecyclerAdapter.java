package edu.chapman.cpsc370.asdplaydate.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.chapman.cpsc370.asdplaydate.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.activities.ChatActivity;

public class ChatRequestListRecyclerAdapter extends RecyclerView.Adapter<ChatRequestListRecyclerAdapter.ViewHolder>
{
    public static final int HAS_ACCEPTED= 0;
    public static final int NOT_ACCEPTED = 1;
    private List<ChatRequestListRecyclerItem> mItems;
    private RecyclerView mRecyclerView;
    private Context ctx;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener
    {
        public TextView parentName;
        public TextView lastMsg;
        public ViewHolderClicks mListener;

        public ViewHolder(View cardView, ViewHolderClicks listener)
        {
            super(cardView);

            this.parentName = (TextView) cardView.findViewById(R.id.tv_parent_name);
            this.lastMsg = (TextView) cardView.findViewById(R.id.tv_last_message);
            mListener = listener;

            // OnClickListeners set below for each child in the cardView
            cardView.setOnClickListener(this);

            View profileButton = cardView.findViewById(R.id.b_profile);
            profileButton.setClickable(true);
            profileButton.setOnClickListener(this);

            View denyButton = cardView.findViewById(R.id.b_deny_req);
            denyButton.setClickable(true);
            denyButton.setOnClickListener(this);

            View acceptButton = cardView.findViewById(R.id.b_accept_req);
            acceptButton.setClickable(true);
            acceptButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            int pos = getLayoutPosition();
            if (id == R.id.b_deny_req)
            {
                mListener.denyRequest(pos);
            }
            else if (id == R.id.b_accept_req)
            {
                mListener.acceptRequest(pos);
            }
            else if (id == R.id.b_profile)
            {
                mListener.showProfileDialog(pos);
            }
            else // Clicking the cardView body
            {
                mListener.showChat(pos);
            }
        }

        public interface ViewHolderClicks
        {
            void denyRequest(int position);
            void acceptRequest(int position);
            void showProfileDialog(int position);
            void showChat(int position);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatRequestListRecyclerAdapter(List<ChatRequestListRecyclerItem> mItems, Context ctx, RecyclerView mRecyclerView)
    {
        this.mItems = mItems;
        this.ctx = ctx;
        this.mRecyclerView = mRecyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatRequestListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType)
    {
        // create a new view
        final CardView vi = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_chatrequest_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        disableTouchTheft(vi);

        if (viewType == HAS_ACCEPTED)
        {
            vi.findViewById(R.id.ll_chatrequestlist_buttons).setVisibility(View.GONE);
        }

        ViewHolder vh = new ViewHolder(vi, new ViewHolder.ViewHolderClicks()
        {
            @Override
            public void denyRequest(int position)
            {
                ObjectAnimator slideOutRight = ObjectAnimator.ofFloat(vi, "translationX", 1000f);
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(vi, "alpha", 0);
                slideOutRight.setDuration(250);
                fadeOut.setDuration(250);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(slideOutRight, fadeOut);
                set.setInterpolator(new AccelerateInterpolator());
                set.start();
                mItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mItems.size());
            }

            @Override
            public void acceptRequest(int position)
            {
                ChatRequestListRecyclerItem thisItem = mItems.get(position);
                Intent i = new Intent(ctx, ChatActivity.class);
                i.putExtra("userID", thisItem.getUserID());
                i.putExtra("parentName", thisItem.getParentName());
                ctx.startActivity(i);
                thisItem.setAccepted(true);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void showProfileDialog(int position)
            {
                //TODO: Show profile dialog
                Toast.makeText(ctx, "Profile Shown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showChat(int position)
            {
                //TODO: Show chat window (not first-time), already accepted
                ChatRequestListRecyclerItem thisItem = mItems.get(position);

                if (thisItem.isAccepted())
                {
                    Intent i = new Intent(ctx, ChatActivity.class);
                    i.putExtra("userID", thisItem.getUserID());
                    i.putExtra("parentName", thisItem.getParentName());
                    ctx.startActivity(i);
                }
            }
        });
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
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (mItems.get(position).isAccepted())
        {
            return HAS_ACCEPTED;
        }
        else
        {
            return NOT_ACCEPTED;
        }
    }

}
