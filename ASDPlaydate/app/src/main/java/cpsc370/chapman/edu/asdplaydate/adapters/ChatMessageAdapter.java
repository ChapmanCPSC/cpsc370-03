package cpsc370.chapman.edu.asdplaydate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cpsc370.chapman.edu.asdplaydate.R;
import cpsc370.chapman.edu.asdplaydate.models.ChatMessage;

/**
 * Created by Martin on 11/3/2015.
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage>
{
    Context context;
    List<ChatMessage> messages;
    LinearLayout ll_bubbleContainer;

    public ChatMessageAdapter(Context context, ArrayList<ChatMessage> messages)
    {
        super(context, R.layout.row_message_me);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ChatMessage message = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView;

        if (message.isMe())
        {
            rootView = inflater.inflate(R.layout.row_message_me, parent, false);
        } else
        {
            rootView = inflater.inflate(R.layout.row_message_notme, parent, false);
        }

        TextView tv_message = (TextView) rootView.findViewById(R.id.tv_Message);
        TextView tv_dateTime = (TextView) rootView.findViewById(R.id.tv_dateTime);
        ll_bubbleContainer = (LinearLayout) rootView.findViewById(R.id.ll_bubbleContainer);

        tv_message.setText(message.getMessageText());
        tv_dateTime.setText(message.getDateTime());

        return rootView;
    }

    @Override
    public void add(ChatMessage object)
    {
        super.add(object);
    }
}
