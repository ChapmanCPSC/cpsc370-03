package cpsc370.chapman.edu.asdplaydate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatRequestListRecyclerAdapter extends RecyclerView.Adapter<ChatRequestListRecyclerAdapter.ViewHolder>
{
    private ChatRequestListRecyclerItem[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView parentName;
        public TextView lastMsg;
        public LinearLayout linearLayout;

        public ViewHolder(View v)
        {
            super(v);

            this.parentName = (TextView) v.findViewById(R.id.parent_name);
            this.lastMsg = (TextView) v.findViewById(R.id.last_message);
            this.linearLayout = (LinearLayout) v.findViewById(R.id.part2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatRequestListRecyclerAdapter(ChatRequestListRecyclerItem[] myDataset)
    {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatRequestListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType)
    {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_chatrequest_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mCardView.setText(mDataset[position]);

        //TODO: fill data here
        holder.parentName.setText(mDataset[position].getParentName());
        holder.lastMsg.setText(mDataset[position].getLastMsg());

        if (mDataset[position].isHasAccepted()) {
            holder.linearLayout.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.length;
    }
}
