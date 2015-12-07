package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.BaseApplication;
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

    ProgressDialog progressDialog;

    String parentName;

    public static ChatActivity chatActivity;

    private static final String TAG = "PushDebugChat";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatActivity = this;
        messages = new ArrayList<>();

        currentUser = (ASDPlaydateUser)ParseUser.getCurrentUser();

        fab_sendMessage = (FloatingActionButton) findViewById(R.id.fab_sendMessage);
        lv_displayMessages = (ListView) findViewById(R.id.lv_displayMessages);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_chatInfoName = (TextView) findViewById(R.id.tv_chatInfoName);

        messageAdapter = new ChatMessageAdapter(this, messages, currentUser);
        lv_displayMessages.setAdapter(messageAdapter);

        final Intent i = getIntent();
        String conversationID = i.getStringExtra("conversationID");
        if (conversationID == null)
        {
            Log.d(TAG,"here");
            conversationID = "";
            Bundle bundle = i.getExtras();
            String data = bundle.get("com.parse.Data").toString();
            Log.d(TAG + "HI",data);
            try
            {
                JSONObject mainObject = new JSONObject(data);
                conversationID = mainObject.getString("conversationID");
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            Log.d(TAG, conversationID);
        }

        if (BaseApplication.inDEBUGMode())
        {
            tv_chatInfoName.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    ParseQuery<Conversation> q = new ParseQuery<>(Conversation.class);
                    q.whereEqualTo(Conversation.ATTR_ID, i.getStringExtra("conversationID"));
                    try
                    {
                        Conversation result = q.find().get(0);
                        result.setExpireDate(DateTime.now(DateTimeZone.UTC).minusHours(1));
                        result.saveInBackground();
                        chatActivity.finish();

                        Toast.makeText(ChatActivity.this, "Conversation set as expired.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    return false;
                }
            });
        }

        ConversationCallback conversationCallback = new ConversationCallback();
        ParseQuery<Conversation> conversationQuery = new ParseQuery<>(Conversation.class);

        // Show progress dialog
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

        // Get the correct conversation
        conversationQuery.getInBackground(conversationID, conversationCallback);

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
        // Show progress dialog
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true);

        // Get chat partner's child
        ParseQuery<Child> childQuery = new ParseQuery<>(Child.class);
        childQuery.whereEqualTo(Child.ATTR_PARENT, chatPartner);

        childQuery.findInBackground(new FindCallback<Child>() {
            @Override
            public void done(List<Child> objects, ParseException e) {

                Child chatPartnerChild = objects.get(0);

                String message = "";

                if (chatPartnerChild != null) {
                    String description = chatPartnerChild.getDescription();

                    message = parentName + "\n\n" + chatPartnerChild.getAge() + " year old "
                            + chatPartnerChild.getFirstName() + "\n"
                            + (description == null ? "" : description);
                }

                // Hide progress dialog
                progressDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this)
                        .setTitle(R.string.view_profile_dialog_title)
                        .setPositiveButton(R.string.button_ok, null)
                        .setMessage(message);

                builder.show();
            }
        });



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
                    String temp = "Message from " + currentUser.getFirstName() + ": " + text;
                    push.setMessage(temp);
                    JSONObject data = new JSONObject();
                    try
                    {
                        data.put("alert", temp);
                        data.put("conversationID", conversation.getObjectId());
                    }
                    catch (JSONException e1)
                    {
                        e1.printStackTrace();
                    }
                    push.setData(data);
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

    private class ConversationCallback implements GetCallback<Conversation>
    {

        @Override
        public void done(Conversation object, ParseException e) {

            conversation = object;

            // Get the conversation partner
            if(!conversation.getInitiator().equals(currentUser))
            {
                chatPartner = conversation.getInitiator();
            }
            else
            {
                chatPartner = conversation.getReceiver();
            }

            chatPartner.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    parentName = chatPartner.getFirstName() + " " + chatPartner.getLastName();
                    tv_chatInfoName.setText(chatActivity.getText(R.string.chat_with) + " " + parentName);
                }
            });

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

                    // Hide progress dialog
                    progressDialog.dismiss();
                }
            });

        }
    }

}
