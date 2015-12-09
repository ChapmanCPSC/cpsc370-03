package edu.chapman.cpsc370.asdplaydate.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.joda.time.DateTime;

import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;

public class FindFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, LocationSource, android.location.LocationListener
{

    MapView mapView;
    GoogleApiClient googleApiClient;
    SeekBar broadcastDuration;
    TextView progressValue;
    FloatingActionButton listFab, doBroadcastFab, broadcastFab, refreshFab;
    AlertDialog broadcastDialog;
    LinearLayout broadcastBar;
    CheckBox broadcastCheckBox;
    EditText broadcastMessage;
    FindFragmentContainer parent;
    ProgressDialog progressDialog;
    boolean connected = false;

    LocationManager locationManager;
    OnLocationChangedListener locationChangedListener;
    private final Criteria criteria = new Criteria();

    View rootView;
    boolean firstLoad = true;

    private SessionManager sessionManager;

    public FindFragment()
    {
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (firstLoad)
        {
            parent = (FindFragmentContainer) getParentFragment();
            sessionManager = parent.sessionManager;

            // Skip the setup the next time onCreateView is called
            firstLoad = false;
            progressDialog = ProgressDialog.show(getActivity(), "Finding Your Location", "Please wait...", true);

            rootView = inflater.inflate(R.layout.fragment_map, container, false);
            mapView = (MapView) rootView.findViewById(R.id.mapView);
            broadcastBar = (LinearLayout) rootView.findViewById(R.id.ll_broadcast_bar);
            broadcastFab = (FloatingActionButton) rootView.findViewById(R.id.fab_broadcast);
            broadcastFab.setOnClickListener(this);
            listFab = (FloatingActionButton) rootView.findViewById(R.id.fab_list);
            listFab.hide();
            listFab.setOnClickListener(this);
            refreshFab = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh_map);
            refreshFab.hide();
            refreshFab.setOnClickListener(this);
            broadcastDuration = (SeekBar) rootView.findViewById(R.id.sb_broadcast_duration);

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            setUpMapIfNeeded();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || hasRequiredPermissions())
            {
                initGoogleClient();
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }

        }

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();

        // Only check for ongoing broadcasts here if already connected to Google Play services
        if(connected)
            checkBroadcast();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            initGoogleClient();
            if(locationChangedListener != null)
                activate(locationChangedListener);
        }
        else
        {
            hideBroadcastBar();
            Toast.makeText(getActivity(), R.string.location_permission_needed, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        parent.googleMap = googleMap;
    }

    private void setUpMap()
    {
        parent.googleMap.setMyLocationEnabled(true);
        parent.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        parent.googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void setUpMapIfNeeded()
    {
        if (parent.googleMap == null)
        {
            parent.googleMap = mapView.getMap();

            if (parent.googleMap != null)
            {
                setUpMap();
                parent.googleMap.setLocationSource(this);
            }
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (googleApiClient != null)
            googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        // Get location once connected to Play Services
        parent.myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        // Check for any ongoing broadcasts
        if(!connected)
            checkBroadcast();
        else
            connected = true;
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        connected = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
    }

    private void inflateBroadcastDialog()
    {
        int defaultBroadcastDuration = sessionManager.getBroadcastDuration();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View broadcast = inflater.inflate(R.layout.broadcast_dialog, null);

        broadcastMessage = (EditText) broadcast.findViewById(R.id.et_broadcast_message);
        if (!sessionManager.getBroadcastMessage().equals(""))
        {
            broadcastMessage.setText(sessionManager.getBroadcastMessage());
        }
        broadcastCheckBox = (CheckBox) broadcast.findViewById(R.id.cb_dont_ask_again);
        doBroadcastFab = (FloatingActionButton) broadcast.findViewById(R.id.fab_go);
        doBroadcastFab.setOnClickListener(onClickListener);
        broadcastDuration = (SeekBar) broadcast.findViewById(R.id.sb_broadcast_duration);
        broadcastDuration.setProgress(defaultBroadcastDuration);
        broadcastDuration.setOnSeekBarChangeListener(onSeekBarChangeListener);
        progressValue = (TextView) broadcast.findViewById(R.id.tv_duration_progress);
        progressValue.setText(defaultBroadcastDuration + " " + getString(R.string.minutes));//Lien changed this to set it to user default preference
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(broadcast);
        broadcastDialog = builder.create();
        broadcastDialog.show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            try
            {
                sessionManager.storeFromDialog(broadcastCheckBox.isChecked());
                ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
                ParseGeoPoint location = new ParseGeoPoint(parent.myLocation.getLatitude(), parent.myLocation.getLongitude());
                String message = broadcastMessage.getText().toString();
                DateTime expireDate = DateTime.now().plusMinutes(broadcastDuration.getProgress()+1);
                Broadcast b = new Broadcast(broadcaster, location, message, expireDate);//add broadcast to PARSE with message
                b.saveInBackground();

            }
            catch (Exception ex)
            {
                Toast.makeText(getActivity(),"Oops, something went wrong with your broadcast. Please try again!",Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            //end lien103 code
            parent.broadcasted = true;//leave this here
            broadcastDialog.cancel();
            showBroadcastResults();
        }
    };

    public void startNonDialogueBroadcast()
    {
        startBroadcast();
        parent.broadcasted = true;//leave this here
    }

    public void updateMap()
    {
        try
        {
            // Update the UI of both fragments here
            parent.updateUI();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initGoogleClient()
    {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            progressValue.setText((progress + 1) + " " + getString(R.string.minutes));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
        }
    };

    private void openList()
    {
        parent.flipFragment();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fab_broadcast:
                //Lien103 code starts here
                if (sessionManager.getPromptBroadcast())
                {
                    inflateBroadcastDialog();
                }
                else
                {
                    //inflateBroadcastDialog();//not sure what to do here to start the broadcast, but not show the dialogue test
                    this.startNonDialogueBroadcast();
                }
                break;
            case R.id.fab_list:
                openList();
                break;
            case R.id.fab_refresh_map:
                updateMap();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (locationChangedListener != null)
        {
            locationChangedListener.onLocationChanged(location);

            CameraPosition cp = new CameraPosition.Builder().target(LocationHelpers.toLatLng(location)).zoom(12).build();
            parent.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

            // Store location in field if location changes
            parent.myLocation = location;
        }

        if (!parent.broadcasted)
        {
            progressDialog.dismiss();
            broadcastBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    private boolean hasRequiredPermissions()
    {
        return ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void hideBroadcastBar()
    {
        ObjectAnimator slideDown = ObjectAnimator.ofFloat(broadcastBar, "translationY", 2000);
        slideDown.setDuration(500).start();
    }

    private void showBroadcastBar()
    {
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(broadcastBar, "translationY", 0);
        slideUp.setDuration(500).start();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        locationChangedListener = onLocationChangedListener;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7500, 10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 7500, 10, this);
        }
    }

    @Override
    public void deactivate()
    {
        locationChangedListener = null;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.removeUpdates(this);
        }
    }

    private void checkBroadcast()
    {
        ParseQuery<Broadcast> q = new ParseQuery<>(Broadcast.class);
        q.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now()))
                .whereEqualTo(Broadcast.ATTR_BROADCASTER, ASDPlaydateUser.getCurrentUser());

        q.findInBackground(new FindCallback<Broadcast>()
        {
            @Override
            public void done(List<Broadcast> objects, ParseException e)
            {
                if (objects.size() == 0)
                {
                    // Clear results
                    parent.googleMap.clear();

                    // Prompt before broadcast is on, show broadcast bar again
                    if (sessionManager.getPromptBroadcast())
                    {
                        showBroadcastBar();
                        listFab.hide();
                        refreshFab.hide();
                    }

                    // Prompt is off, start broadcasting again automatically
                    else
                    {
                        startBroadcast();
                    }
                }

                // User is already broadcasting
                else
                {
                    showBroadcastResults();
                }

            }
        });
    }

    private void startBroadcast()
    {
        ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
        ParseGeoPoint location = new ParseGeoPoint(parent.myLocation.getLatitude(), parent.myLocation.getLongitude());
        String message = sessionManager.getBroadcastMessage();
        DateTime expireDate = DateTime.now().plusMinutes(sessionManager.getBroadcastDuration());
        Broadcast b = new Broadcast(broadcaster, location, message, expireDate);

        b.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    showBroadcastResults();
                }
                else
                {
                    Toast.makeText(getActivity(),"Something went wrong with your broadcast. Please try again.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showBroadcastResults()
    {
        hideBroadcastBar();
        listFab.show();
        refreshFab.show();
        updateMap();
    }

}
