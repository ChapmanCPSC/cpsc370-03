package edu.chapman.cpsc370.asdplaydate;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.chapman.cpsc370.asdplaydate.fragments.FindFragment;
import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;

public class LocationUpdateService extends Service
{

    // 5 minutes
    public static final int INTERVAL = 60000 * 5;

    public static FindFragment findFragment;

    public static void StartLocationUpdater(final Context context)
    {
        context.startService(new Intent(context, LocationUpdateService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Timer timer = new Timer();
        UpdateTimer updateTimer = new UpdateTimer();

        // Repeat update after the given interval
        timer.scheduleAtFixedRate(updateTimer, 60000, INTERVAL);

        return super.onStartCommand(intent, flags, startId);
    }

    private class UpdateTimer extends TimerTask
    {
        @Override
        public void run()
        {
            // Get current broadcast
            ParseQuery<Broadcast> q = new ParseQuery<>(Broadcast.class);
            q.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now()))
                    .whereEqualTo(Broadcast.ATTR_BROADCASTER, ASDPlaydateUser.getCurrentUser());

            q.findInBackground(new FindCallback<Broadcast>() {
                @Override
                public void done(List<Broadcast> objects, ParseException e)
                {
                    if(e == null)
                    {
                        if(objects.size() != 0)
                        {
                            // Only update if main window is open
                            try
                            {
                                // Get the new location
                                findFragment.parent.myLocation = LocationServices.FusedLocationApi
                                        .getLastLocation(findFragment.googleApiClient);

                                // Update broadcast with new location
                                Broadcast updatedBroadcast = objects.get(0);
                                updatedBroadcast.setLocation(new ParseGeoPoint(
                                        findFragment.parent.myLocation.getLatitude(),
                                        findFragment.parent.myLocation.getLongitude()));

                                updatedBroadcast.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        findFragment.updateMap();
                                    }
                                });
                            }
                            catch(Exception exception)
                            {
                                //exception.printStackTrace();
                            }

                        }
                        else
                        {
                            // Broadcast has finished, show broadcast bar
                            findFragment.parent.googleMap.clear();
                            findFragment.resetBroadcastBar();

                            // Stop service
                            cancel();
                            LocationUpdateService.this.stopSelf();
                        }
                    }
                }
            });

        }
    }

}
