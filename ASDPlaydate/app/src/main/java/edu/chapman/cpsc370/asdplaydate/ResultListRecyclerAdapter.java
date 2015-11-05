package edu.chapman.cpsc370.asdplaydate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alec Richter on 11/3/2015.
 */

public class ResultListRecyclerAdapter extends RecyclerView.Adapter<ResultListRecyclerAdapter.ResultItemViewHolder> {

    private Context context;

    public ResultListRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ResultListRecyclerAdapter.ResultItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_result_list_item, viewGroup, false);
        return new ResultItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ResultListRecyclerAdapter.ResultItemViewHolder holder, int i) {

        // TEMPORARY TEST CODE
        holder.parentName.setText("John Smith");
        holder.childName.setText("Johnnie (M)");
        holder.childAge.setText("8 years old");
        holder.childCondition.setText("High Functioning Autism");
        holder.distance.setText("1 mile from you");

        holder.requestChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chat request sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ResultItemViewHolder extends RecyclerView.ViewHolder {

        TextView parentName;
        TextView childName;
        TextView childAge;
        TextView childCondition;
        TextView distance;
        Button requestChat;

        ResultItemViewHolder(View itemView) {
            super(itemView);

            parentName = (TextView) itemView.findViewById(R.id.result_list_parent_name);
            childName = (TextView) itemView.findViewById(R.id.result_list_child_name);
            childAge = (TextView) itemView.findViewById(R.id.result_list_child_age);
            childCondition = (TextView) itemView.findViewById(R.id.result_list_child_condition);
            distance = (TextView) itemView.findViewById(R.id.result_list_distance);
            requestChat = (Button) itemView.findViewById(R.id.result_list_request_chat_button);
        }
    }
}
