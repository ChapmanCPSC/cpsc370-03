package edu.chapman.cpsc370.asdplaydate.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import org.joda.time.DateTime;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.helpers.RecyclerAdapterHelpers;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.activities.ChatActivity;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.Message;

public class ChatRequestListRecyclerAdapter extends RecyclerView.Adapter<ChatRequestListRecyclerAdapter.ViewHolder>
{
    public static final int HAS_ACCEPTED = 0;
    public static final int NOT_ACCEPTED = 1;
    private List<ChatRequestListRecyclerItem> mItems;
    private Context ctx;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener
    {
        public TextView parentName;
        public TextView lastMsg;
        public TextView expiration;
        public ViewHolderClicks mListener;

        public ViewHolder(View cardView, boolean accepted, ViewHolderClicks listener)
        {
            super(cardView);

            this.parentName = (TextView) cardView.findViewById(R.id.tv_parent_name);
            this.lastMsg = (TextView) cardView.findViewById(R.id.tv_last_message);
            this.expiration = (TextView) cardView.findViewById(R.id.tv_expiration_value);
            mListener = listener;

            // OnClickListeners set below for each child in the cardView
            if (accepted)
            {
                cardView.setOnClickListener(this);
            }

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
    public ChatRequestListRecyclerAdapter(List<ChatRequestListRecyclerItem> mItems, Context ctx)
    {
        this.mItems = mItems;
        this.ctx = ctx;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatRequestListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType)
    {
        Log.i("RecyclerAdapter", "onCreateViewHolder");
        // create a new view
        final CardView vi = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_chatrequest_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        disableTouchTheft(vi);

        boolean accepted = true;
        if (viewType == HAS_ACCEPTED)
        {
            vi.findViewById(R.id.ll_expiration_date).setVisibility(View.VISIBLE);
            vi.findViewById(R.id.tv_last_message).setVisibility(View.VISIBLE);
            vi.findViewById(R.id.ll_chatrequestlist_buttons).setVisibility(View.GONE);
        }
        else
        {
            vi.findViewById(R.id.ll_expiration_date).setVisibility(View.GONE);
            vi.findViewById(R.id.tv_last_message).setVisibility(View.GONE);
            accepted = false;
        }

        ViewHolder vh = new ViewHolder(vi, accepted, new ViewHolder.ViewHolderClicks()
        {
            @Override
            public void denyRequest(int position)
            {
                RecyclerAdapterHelpers.denyRequest(mItems.get(position));
                RecyclerAdapterHelpers.doSlideOutAnim(vi);

                mItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mItems.size());
            }

            @Override
            public void acceptRequest(int position)
            {
                Conversation convo = new Conversation();
                ChatRequestListRecyclerItem thisItem = mItems.get(position);
                String conversationID = thisItem.getConversationID();
                try
                {
                    convo = Conversation.getConversation(conversationID);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                convo.setStatus(Conversation.Status.ACCEPTED);
                convo.setExpireDate(DateTime.now().plusHours(24));
                convo.saveInBackground();

                Intent i = new Intent(ctx, ChatActivity.class);
                i.putExtra("conversationID", conversationID);
                i.putExtra("parentName", thisItem.getParentName());
                ctx.startActivity(i);
                thisItem.setAccepted(true);
                notifyItemChanged(position);
                vi.findViewById(R.id.ll_chatrequestlist_buttons).setVisibility(View.GONE);
            }

            @Override
            public void showProfileDialog(int position)
            {
                ChatRequestListRecyclerItem thisItem = mItems.get(position);
                String parentName = thisItem.getParentName();
                String childName = "";
                String childGender = "";
                int childAge = 0;
                String childDesc = "";

                try
                {
                    ASDPlaydateUser parent = ASDPlaydateUser.getUser(thisItem.getUserID());
                    Child child = Child.getChildFromParent(parent);
                    childName = child.fetchIfNeeded().getString("first_name");
                    childGender = child.fetchIfNeeded().getString("gender");
                    childAge = child.fetchIfNeeded().getInt("age");
                    childDesc = child.fetchIfNeeded().getString("description");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                String message = parentName + "\n\n" + ctx.getString(R.string.child) + ": " + childName + " (" + String.valueOf(childGender) + ")" + "\n" + ctx.getString(R.string.age) + " " + childAge
                        + "\n" + childDesc;

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                        .setTitle(R.string.view_profile_dialog_title)
                        .setPositiveButton(R.string.button_ok, null)
                        .setMessage(message);

                builder.show();
            }

            @Override
            public void showChat(int position)
            {
                //TODO: Show chat window (not first-time), already accepted
                ChatRequestListRecyclerItem thisItem = mItems.get(position);

                if (thisItem.isAccepted())
                {
                    Intent i = new Intent(ctx, ChatActivity.class);

                    // TODO: Send conversation ID to chat activity
                    i.putExtra("conversationID", thisItem.getConversationID());

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
        holder.parentName.setText(mItems.get(position).getParentName());

        try
        {
            Conversation convo = Conversation.getConversation(mItems.get(position).getConversationID());
            holder.expiration.setText(Conversation.printDate(convo.getExpireDate()));
            Message lastMessage = Message.getLastMessage(convo);
            holder.lastMsg.setText(lastMessage.getText());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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
