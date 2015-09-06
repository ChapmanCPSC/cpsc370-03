package com.alecrichter.textalarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.SeekBar;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences prefs;
    private Integer durations[] = {0, 1, 5, 10, 15, 30, 60};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        //Set summary for alarm tone
        prefs = getPreferenceScreen().getSharedPreferences();

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if(preference.getKey().equals("pref_alarm_volume"))
            showVolumeDialog();

        else if(preference.getKey().equals("pref_alarm_duration"))
            showDurationDialog();

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void showVolumeDialog() {

        //Inflate view
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_volume, null);

        //Get volume bar
        final SeekBar volumeBar = (SeekBar) dialogView.findViewById(R.id.dialog_volumeBar);
        volumeBar.setProgress(prefs.getInt("pref_alarm_volume", 75));

        //Show dialog window
        new MaterialDialog.Builder(getActivity())
                .title("Set alarm volume")
                .customView(dialogView, false)
                .positiveText("OK")
                .negativeText("Cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("pref_alarm_volume", volumeBar.getProgress());
                        editor.apply();
                    }
                })
                .show();
    }

    private void showDurationDialog() {

        int index = Arrays.asList(durations).indexOf(prefs.getInt("pref_alarm_duration", 0));

        new MaterialDialog.Builder(getActivity())
                .title("Set alarm duration")
                .items(R.array.alarm_durations)
                .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("pref_alarm_duration", durations[i]);
                        editor.apply();

                        return true;
                    }
                })
                .positiveText("OK")
                .negativeText("Cancel")
                .show();
    }

}
