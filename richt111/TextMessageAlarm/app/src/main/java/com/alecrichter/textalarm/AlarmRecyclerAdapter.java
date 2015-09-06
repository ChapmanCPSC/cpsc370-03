package com.alecrichter.textalarm;


import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.AlarmViewHolder> {

    private List<Alarm> alarms;
    private MainActivity activity;
    private Context context;
    private AlarmTonePicker picker;
    private static Alarm removedAlarm;
    private int removedPos;
    private AlarmDatabase db;

    AlarmRecyclerAdapter(List<Alarm> alarms, MainActivity activity, Context context) {
        this.alarms = alarms;
        this.activity = activity;
        this.context = context;

        db = new AlarmDatabase(context, AlarmDatabase.databaseName, null, AlarmDatabase.databaseVersion);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_alarm, viewGroup, false);
        AlarmViewHolder avh = new AlarmViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(final AlarmViewHolder alarmViewHolder, final int i) {

        final Alarm alarm = alarms.get(i);

        alarmViewHolder.title.setText(alarm.getTitle());
        alarmViewHolder.content.setText(alarm.getContentO());
        alarmViewHolder.alarmSwitch.setChecked(Boolean.parseBoolean(alarm.getActivated()));
        alarmViewHolder.alarmToneTitle.setText(getAlarmToneTitle(alarm.getUri()));

        final AlarmDatabase db = new AlarmDatabase(context, AlarmDatabase.databaseName, null, AlarmDatabase.databaseVersion);

        //Switch listener
        alarmViewHolder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Enable or disable alarm
                if (alarmViewHolder.alarmSwitch.isChecked())
                    alarm.toggleAlarm("true");
                else
                    alarm.toggleAlarm("false");

                //Save change in db
                db.editAlarmToggle(alarm);
            }
        });

        //Remove alarm listener
        alarmViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save object in case of an undo
                removedAlarm = alarm;
                removedPos = alarms.indexOf(removedAlarm);

                //Snackbar removed until FAB bug is fixed

                //Snackbar.make(v, "Alarm deleted", Snackbar.LENGTH_LONG).show();
                //        .setAction("Undo", undoListener).show(); // Undo action

                //Delete, show message, reset list
                Toast.makeText(context, "Alarm deleted", Toast.LENGTH_SHORT).show();

                activity.removeAlarm(removedAlarm, removedPos);
            }
        });

        //Change alarm tone listener
        alarmViewHolder.alarmToneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alarm tone picker
                picker = new AlarmTonePicker(alarm, activity);
                picker.pickAlarmTone();
            }
        });

    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    private String getAlarmToneTitle(String alarmToneUri) {

        try {
            Ringtone alarmTone = RingtoneManager.getRingtone(activity, Uri.parse(alarmToneUri));
            return alarmTone.getTitle(activity);
        } catch (Exception e) {
            return "Default alarm tone";
        }

    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView content;
        SwitchCompat alarmSwitch;
        TextView alarmToneTitle;
        ImageButton removeBtn;
        RelativeLayout alarmToneBtn;

        AlarmViewHolder(View itemView) {
            super(itemView);

            // Get objects from XML
            cv = (CardView) itemView.findViewById(R.id.cardView_alarm);
            title = (TextView) itemView.findViewById(R.id.card_alarmTitle);
            content = (TextView) itemView.findViewById(R.id.card_alarmContent);
            alarmSwitch = (SwitchCompat) itemView.findViewById(R.id.card_alarmSwitch);
            alarmToneTitle = (TextView) itemView.findViewById(R.id.card_alarmToneTitle);
            removeBtn = (ImageButton) itemView.findViewById(R.id.card_removeBtn);
            alarmToneBtn = (RelativeLayout) itemView.findViewById(R.id.card_alarmToneBtn);
        }

    }

    private final View.OnClickListener undoListener = new View.OnClickListener() {
        public void onClick(View v) {
            //Re-add to database and reset the list
            if(removedAlarm != null) {
                activity.addAlarm(removedAlarm, removedPos);
            }
        }
    };

    public void add(Alarm alarm, int position) {

        //alarms.add(position, alarm);
        //notifyItemInserted(position);
        //db.addAlarm(alarm);
    }

    public void remove(Alarm alarm, int position) {
        alarms.remove(position);
        notifyItemRemoved(position);
        db.deleteAlarm(alarm);
    }

}
