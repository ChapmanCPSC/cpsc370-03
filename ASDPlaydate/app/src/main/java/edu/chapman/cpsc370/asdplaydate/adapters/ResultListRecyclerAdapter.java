package edu.chapman.cpsc370.asdplaydate.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragment;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

/**
 * Created by Alec Richter on 11/3/2015.
 */

public class ResultListRecyclerAdapter extends RecyclerView.Adapter<ResultListRecyclerAdapter.ResultItemViewHolder> {

    private Context context;
    private Location myLocation;
    private ArrayList<MarkerLabelInfo> data;


    public ResultListRecyclerAdapter(Context context, Location myLocation, HashMap<LatLng, MarkerLabelInfo> data)
    {
        this.context = context;
        this.myLocation = myLocation;
        this.data = new ArrayList<>(data.values());
    }

    @Override
    public ResultListRecyclerAdapter.ResultItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_result_list_item, viewGroup, false);
        return new ResultItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ResultListRecyclerAdapter.ResultItemViewHolder holder, int i)
    {

        //TODO: Pass in data model here
        MarkerLabelInfo info = data.get(i);
        // Set info here
        ASDPlaydateUser bcaster = info.getParent();
        Child child = info.getChild();
        holder.parentName.setText(bcaster.getFirstName() + " " + bcaster.getLastName());
        holder.childAge.setText(child.getAge() + " years old");
        holder.childName.setText(child.getFirstName() + " (" + child.getGender().name().substring(0,1) + ")");
        holder.childCondition.setText(child.getDescription());

        ParseGeoPoint myPgp = toParseGeoPoint(myLocation);
        ParseGeoPoint broadcastPgp = toParseGeoPoint(info.getLocation());
        //TODO: Check rounding
        holder.distance.setText(Math.round(myPgp.distanceInMilesTo(broadcastPgp)) + " miles from you");


        //TODO: parse request to send a chat
        holder.requestChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chat request sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ParseGeoPoint toParseGeoPoint(Location location)
    {
        return new ParseGeoPoint(location.getLatitude(), location.getLongitude());
    }

    public ParseGeoPoint toParseGeoPoint(LatLng latLng)
    {
        return new ParseGeoPoint(latLng.latitude, latLng.longitude);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ResultItemViewHolder extends RecyclerView.ViewHolder {

        TextView parentName;
        TextView childName;
        TextView childAge;
        TextView childCondition;
        TextView distance;
        ImageView requestChat;

        ResultItemViewHolder(View itemView) {
            super(itemView);

            parentName = (TextView) itemView.findViewById(R.id.result_list_parent_name);
            childName = (TextView) itemView.findViewById(R.id.result_list_child_name);
            childAge = (TextView) itemView.findViewById(R.id.result_list_child_age);
            childCondition = (TextView) itemView.findViewById(R.id.result_list_child_condition);
            distance = (TextView) itemView.findViewById(R.id.result_list_distance);
            requestChat = (ImageView) itemView.findViewById(R.id.result_list_request_chat_button);
        }
    }
}
