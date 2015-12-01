package edu.chapman.cpsc370.asdplaydate.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParsePush;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragmentContainer;
import edu.chapman.cpsc370.asdplaydate.fragments.ResultListFragment;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.RecyclerAdapterHelpers;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

/**
 * Created by Alec Richter on 11/3/2015.
 */
public class ResultListRecyclerAdapter extends RecyclerView.Adapter<ResultListRecyclerAdapter.ResultItemViewHolder>
{

    private Context ctx;
    private ResultListFragment fragment;
    private SessionManager sessionManager;

    public ResultListRecyclerAdapter(Context ctx, ResultListFragment fragment)
    {
        this.ctx = ctx;
        this.fragment = fragment;
        sessionManager = new SessionManager(ctx);
    }

    @Override
    public ResultListRecyclerAdapter.ResultItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View vi = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_result_list_item, viewGroup, false);
        ResultListRecyclerAdapter.ResultItemViewHolder vh = new ResultItemViewHolder(vi, new ResultItemViewHolder.ViewHolderClicks()
        {
            @Override
            public void sendChatRequest(int position)
            {
                FindFragmentContainer container = (FindFragmentContainer) fragment.getParentFragment();
                ASDPlaydateUser receiver = container.broadcasts.get(position).getParent();
                RecyclerAdapterHelpers.sendChatRequest(sessionManager, receiver, ctx, container.broadcasts.get(position).getMarker());
                RecyclerAdapterHelpers.doSlideOutAnim(vi);
                container.broadcasts.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, container.broadcasts.size());

                // Send push to receiver
                ParsePush push = new ParsePush();
                push.setChannel("c_" + receiver.getObjectId());
                push.setMessage("New chat request from " + receiver.getFirstName() + " "
                        + receiver.getLastName());
                push.sendInBackground();
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ResultListRecyclerAdapter.ResultItemViewHolder holder, int i)
    {
        FindFragmentContainer container = (FindFragmentContainer) fragment.getParentFragment();
        MarkerLabelInfo info = container.broadcasts.get(i);
        ASDPlaydateUser bcaster = info.getParent();
        Child child = info.getChild();
        holder.parentName.setText(bcaster.getFirstName() + " " + bcaster.getLastName());
        holder.childAge.setText(child.getAge() + " years old");
        holder.childName.setText("Child: " + child.getFirstName() + " (" + child.getGender().name().substring(0, 1) + ")");
        holder.childCondition.setText(child.getDescription());

        ParseGeoPoint myPgp = LocationHelpers.toParseGeoPoint(container.myLocation);
        ParseGeoPoint broadcastPgp = LocationHelpers.toParseGeoPoint(info.getLatLng());
        double dist = myPgp.distanceInMilesTo(broadcastPgp);
        String roundedDist = new BigDecimal(String.valueOf(dist)).setScale(1, RoundingMode.HALF_UP).toPlainString();
        holder.distance.setText(roundedDist + " miles from you");
    }

    @Override
    public int getItemCount()
    {
        FindFragmentContainer container = (FindFragmentContainer) fragment.getParentFragment();
        return container.broadcasts.size();
    }

    public static class ResultItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView parentName;
        TextView childName;
        TextView childAge;
        TextView childCondition;
        TextView distance;
        ImageView requestChat;
        public ViewHolderClicks mListener;

        ResultItemViewHolder(View itemView, ViewHolderClicks listener)
        {
            super(itemView);

            parentName = (TextView) itemView.findViewById(R.id.result_list_parent_name);
            childName = (TextView) itemView.findViewById(R.id.result_list_child_name);
            childAge = (TextView) itemView.findViewById(R.id.result_list_child_age);
            childCondition = (TextView) itemView.findViewById(R.id.result_list_child_condition);
            distance = (TextView) itemView.findViewById(R.id.result_list_distance);
            requestChat = (ImageView) itemView.findViewById(R.id.result_list_request_chat_button);
            mListener = listener;

            requestChat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            int pos = getLayoutPosition();
            if (id == R.id.result_list_request_chat_button)
            {
                mListener.sendChatRequest(pos);
            }
        }

        public interface ViewHolderClicks
        {
            void sendChatRequest(int position);
        }
    }
}
