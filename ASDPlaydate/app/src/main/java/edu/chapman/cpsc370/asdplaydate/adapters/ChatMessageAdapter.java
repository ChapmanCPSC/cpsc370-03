package edu.chapman.cpsc370.asdplaydate.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Collection;
import java.util.List;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Message;

public class ChatMessageAdapter extends ArrayAdapter<Message>
{
    Context context;
    ASDPlaydateUser sender;

    public ChatMessageAdapter(Context context, List<Message> messages, ASDPlaydateUser sender)
    {
        super(context, R.layout.row_message, messages);
        this.context = context;
        this.sender = sender;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Message message = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        View rootView = inflater.inflate(R.layout.row_message, parent, false);
        TextView tv_message = (TextView) rootView.findViewById(R.id.tv_Message);
        TextView tv_dateTime = (TextView) rootView.findViewById(R.id.tv_dateTime);
        LinearLayout ll_messageRowContainer = (LinearLayout) rootView.findViewById(R.id.ll_messageRowContainer);
        LinearLayout ll_bubbleContainer = (LinearLayout) rootView.findViewById(R.id.ll_bubbleContainer);

        tv_message.setText(message.getText());
        tv_dateTime.setText(message.getTimestamp().toString("hh:mm a, MM-dd-yyyy"));
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(10, 10, 10, 2);
        if(sender.equals(message.getAuthor()))
        {
            llParams.gravity = Gravity.END;
            ll_bubbleContainer.setBackgroundResource(R.drawable.msgbox_me);
            ll_messageRowContainer.setGravity(Gravity.END);
        }
        else
        {
            llParams.gravity = Gravity.START;
            ll_bubbleContainer.setBackgroundResource(R.drawable.msgbox_notme);
            ll_messageRowContainer.setGravity(Gravity.START);
        }

        ll_bubbleContainer.setLayoutParams(llParams);

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
