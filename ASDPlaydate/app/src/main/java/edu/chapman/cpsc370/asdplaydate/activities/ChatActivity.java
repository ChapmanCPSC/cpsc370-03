package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.ChatMessageAdapter;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.ChatMessage;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.Message;

public class ChatActivity extends AppCompatActivity
{
    ListView lv_displayMessages;
    FloatingActionButton fab_sendMessage;
    ChatMessageAdapter messageAdapter;
    EditText et_message;
    TextView tv_chatInfoName;

    Conversation conversation;
    List<Message> messages = new ArrayList<>();

    ASDPlaydateUser currentUser;
    ASDPlaydateUser chatPartner;

    String parentName;

    private static final String TAG = "PushDebugChat";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        String conversationId = i.getStringExtra("conversationId");
        messages = new ArrayList<>();
        if(conversationId == null)
        {
            Log.d(TAG,"here");
            conversationId = "";
            Bundle bundle = i.getExtras();
            String data = bundle.get("com.parse.Data").toString();
            boolean nameFound = false;
            ArrayList id = new ArrayList();
            for (int j = 0; j < data.length(); j++)
            {
                if (data.charAt(j) == 'c' && data.charAt(j + 13) == 'd')
                {
                    j += 17;
                    nameFound = true;
                }
                if (nameFound)
                {
                    if (data.charAt(j) == '"')
                    {
                        break;
                    }
                    id.add(data.charAt(j));
                }
            }
            for (Object v : id)
            {
                conversationId += v.toString();
            }
            Log.d(TAG,conversationId);
        }
        // TODO: Get actual conversation from previous activity
        ParseQuery<Conversation> q = new ParseQuery<>(Conversation.class);
        try {
            if(conversationId.equals("0"))
            {
                conversation = q.get("RxaGgir69x");
            }
            else
            {
                conversation = q.get(conversationId);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        currentUser = (ASDPlaydateUser)ParseUser.getCurrentUser();

        if(!conversation.getInitiator().equals(currentUser))
        {
            chatPartner = conversation.getInitiator();
        }
        else
        {
            chatPartner = conversation.getReceiver();
        }

        parentName = null;
        try {
            parentName = chatPartner.fetchIfNeeded().getString(ASDPlaydateUser.ATTR_FIRST_NAME)
                    + " " + chatPartner.fetchIfNeeded().getString(ASDPlaydateUser.ATTR_LAST_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fab_sendMessage = (FloatingActionButton) findViewById(R.id.fab_sendMessage);
        lv_displayMessages = (ListView) findViewById(R.id.lv_displayMessages);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_chatInfoName = (TextView) findViewById(R.id.tv_chatInfoName);
        parentName = "Chat With " + parentName;
        tv_chatInfoName.setText(parentName);


        messageAdapter = new ChatMessageAdapter(this, messages, currentUser);
        lv_displayMessages.setAdapter(messageAdapter);

        // Get current messages
        ParseQuery<Message> convoMessageQuery = new ParseQuery<>(Message.class)
                .whereEqualTo(Message.ATTR_CONVERSATION, conversation);

        convoMessageQuery.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {

                // Update list with previous messages
                messages = objects;
                messageAdapter.addAll(messages);
                messageAdapter.notifyDataSetChanged();

                // Mark messages as read
                markAsRead();
            }
        });

        fab_sendMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendMessage();
                et_message.setText("");
            }
        });
    }

    public void viewProfile(View view)
    {

        // Get chat partner's child
        ParseQuery<Child> childQuery = new ParseQuery<>(Child.class);
        childQuery.whereEqualTo(Child.ATTR_PARENT, chatPartner);
        Child chatPartnerChild = null;

        try {
            chatPartnerChild = childQuery.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String message = "";

        if(chatPartnerChild != null)
        {
            String description = chatPartnerChild.getDescription();

            message = parentName + "\n\n" + chatPartnerChild.getAge() + " year old "
                    + chatPartnerChild.getFirstName() + "\n"
                    + (description == null ? "" : description);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.view_profile_dialog_title)
                .setPositiveButton(R.string.button_ok, null)
                .setMessage(message);

        builder.show();
    }

    public void displayMessage(Message message)
    {
        messages.add(message);
        messageAdapter.add(message);
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendMessage()
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ParseUser.getCurrentUser();
        final String text = et_message.getText().toString();

        // Check if message is empty before sending
        if(!text.trim().equals(""))
        {
            final Message message = new Message(conversation, user, text, false, DateTime.now());

            message.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    // Show message that was just sent
                    displayMessage(message);

                    // Send push message
                    ParsePush push = new ParsePush();
                    push.setChannel("c_" + chatPartner.getObjectId());
                    push.setMessage("Message from " + currentUser.getFirstName() + ": " + text);
                    /*try {
                        push.setData(new JSONObject().put("conversationId", conversation.getObjectId()));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }*/
                    push.sendInBackground();
                }
            });
        }
    }

    private void markAsRead() {

        // Mark messages as read
        for (Message message : messages)
        {
            if(!message.isRead() && !message.getAuthor().equals(currentUser))
            {
                message.setIsRead(true);
                message.saveInBackground();
            }
        }
    }

}
