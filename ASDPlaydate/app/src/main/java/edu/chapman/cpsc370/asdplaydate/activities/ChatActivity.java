package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.ChatMessageAdapter;
import edu.chapman.cpsc370.asdplaydate.fragments.CreateAccountFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.LoginFragment;
import edu.chapman.cpsc370.asdplaydate.models.ChatMessage;

public class ChatActivity extends AppCompatActivity
{
    ListView lv_displayMessages;
    FloatingActionButton fab_sendMessage;
    ChatMessageAdapter messageAdapter;
    EditText et_message;
    TextView tv_chatInfoName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        long userID = i.getLongExtra("userID", 0);
        String parentName = i.getStringExtra("parentName");

        fab_sendMessage = (FloatingActionButton) findViewById(R.id.fab_sendMessage);
        lv_displayMessages = (ListView) findViewById(R.id.lv_displayMessages);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_chatInfoName = (TextView) findViewById(R.id.tv_chatInfoName);
        parentName = "Chat With " + parentName;
        tv_chatInfoName.setText(parentName);

        messageAdapter = new ChatMessageAdapter(this, null);


        //for testing only
        ChatMessage message1 = new ChatMessage();
        message1.setDateTime("11/4/15 7:15pm");
        message1.setIsMe(false);
        message1.setMessageText("Hello!");
        message1.setMessageID(121);
        message1.setUserID(1235);
        displayMessage(message1);

        ChatMessage message2 = new ChatMessage();
        message2.setDateTime("11/4/15 7:16pm");
        message2.setIsMe(false);
        message2.setMessageText("How are you?");
        message2.setMessageID(122);
        message2.setUserID(1235);
        displayMessage(message2);

        lv_displayMessages.setAdapter(messageAdapter);

        fab_sendMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChatMessage message = new ChatMessage();

                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();

                message.setDateTime(Integer.toString(today.month + 1) + "/" + Integer.toString(today.monthDay) + "/" + Integer.toString(today.year) + " " + Integer.toString(today.hour) + ":" + Integer.toString(today.minute));
                message.setIsMe(true);
                message.setMessageText(String.valueOf(et_message.getText()));
                message.setMessageID(123);
                message.setUserID(1234);
                displayMessage(message);

                et_message.setText("");
            }
        });
    }

    public void viewProfile(View view)
    {
        Intent i = getIntent();
        long userID = i.getLongExtra("userID", 0);
        String parentName = i.getStringExtra("parentName");

        //TODO get user profile info from database
        //for now, fill artificial data

        String message = parentName + "\n\n8 yr old Johnnie\nHigh functioning Autism";

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.view_profile_dialog_title)
                .setPositiveButton(R.string.button_ok, null)
                .setMessage(message);

        builder.show();
    }

    public void displayMessage(ChatMessage message)
    {
        messageAdapter.add(message);
        messageAdapter.notifyDataSetChanged();
    }
}
