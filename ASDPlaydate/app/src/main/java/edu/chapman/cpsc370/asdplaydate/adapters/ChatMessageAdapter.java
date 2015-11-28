package edu.chapman.cpsc370.asdplaydate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.ChatMessage;
import edu.chapman.cpsc370.asdplaydate.models.Message;

/**
 * Created by Martin on 11/3/2015.
 */
public class ChatMessageAdapter extends ArrayAdapter<Message>
{
    Context context;
    LinearLayout ll_bubbleContainer;
    ASDPlaydateUser sender;

    public ChatMessageAdapter(Context context, List<Message> messages, ASDPlaydateUser sender)
    {
        super(context, R.layout.row_message_me, messages);
        this.context = context;
        this.sender = sender;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Message message = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView;

        if(sender.equals(message.getAuthor()))
        {
            rootView = inflater.inflate(R.layout.row_message_me, parent, false);
        }
        else
        {
            rootView = inflater.inflate(R.layout.row_message_notme, parent, false);
        }

        TextView tv_message = (TextView) rootView.findViewById(R.id.tv_Message);
        TextView tv_dateTime = (TextView) rootView.findViewById(R.id.tv_dateTime);
        ll_bubbleContainer = (LinearLayout) rootView.findViewById(R.id.ll_bubbleContainer);

        tv_message.setText(message.getText());
        tv_dateTime.setText(message.getTimestamp().toString("hh:mm a, MM-dd-yyyy"));

        return rootView;
    }

    @Override
    public void add(Message object)
    {
        super.add(object);
    }

    @Override
    public void addAll(Collection<? extends Message> collection) {
        super.addAll(collection);
    }
}
