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
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.BuildConfig;
import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.MarkerLabelAdapter;
import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

public class FindFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, LocationSource, android.location.LocationListener
{

    MapView mapView;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    SeekBar broadcastDuration;
    TextView progressValue;
    FloatingActionButton listFab, doBroadcastFab, broadcastFab;
    AlertDialog broadcastDialog;
    LinearLayout broadcastBar;
    CheckBox broadcastCheckBox;
    EditText broadcastMessage;
    FindFragmentContainer parent;
    ProgressDialog progressDialog;
    boolean broadcasted = false;

    LocationManager locationManager;
    OnLocationChangedListener locationChangedListener;
    private final Criteria criteria = new Criteria();
    String bestProvider;

    View rootView;
    boolean firstLoad = true;

    SessionManager sessionManager;

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
            // Skip the setup the next time onCreateView is called/
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
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }

        }

        sessionManager = new SessionManager(getActivity());
        parent = (FindFragmentContainer) getParentFragment();

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        {
            initGoogleClient();
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
        this.googleMap = googleMap;
    }

    private void setUpMap()
    {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void setUpMapIfNeeded()
    {
        if (googleMap == null)
        {
            googleMap = mapView.getMap();

            if (googleMap != null)
            {
                setUpMap();
                googleMap.setLocationSource(this);
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
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    private void inflateBroadcastDialog()
    {
        int defaultBroadcastDuration = sessionManager.getBroadcastDuration() + 1;

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View broadcast = inflater.inflate(R.layout.broadcast_dialog, null);

        broadcastMessage = (EditText) broadcast.findViewById(R.id.et_broadcast_message);
        if (sessionManager.getBroadcastMessage() == "")
        {
        }
        else
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(broadcast);
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
                ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
                ParseGeoPoint location = new ParseGeoPoint(parent.myLocation.getLatitude(), parent.myLocation.getLongitude());
                String message = sessionManager.getBroadcastMessage();
                DateTime expireDate = DateTime.now().plusMinutes(sessionManager.getBroadcastDuration());


                if (message == null)
                {
                    Broadcast b = new Broadcast(broadcaster, location, expireDate);//add broadcast to PARSE without message
                    b.saveInBackground();
                }
                else
                {
                    Broadcast b = new Broadcast(broadcaster, location, message, expireDate);//add broadcast to PARSE with message
                    b.saveInBackground();
                }

            }
            catch (Exception ex)
            {
            }

            //end lien103 code
            broadcasted = true;//leave this here

            //SET SHARED PREFS ISCHECKED - begin lien103 code

            boolean dialogueIsChecked = broadcastCheckBox.isChecked();
            sessionManager.storeFromDialog(dialogueIsChecked);//store if the the user checked the checkbox
            sessionManager.storePromptBroadcast(dialogueIsChecked);//store if the the user checked the checkbox

            //end lien103 code
            broadcasted = true;

            broadcastDialog.cancel();

            hideBroadcastBar();

            listFab.show();

            try
            {
                // Get broadcasts here
                parent.broadcasts = getBroadcasts();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            placeMarkers(parent.broadcasts);
            parent.mla = new MarkerLabelAdapter(FindFragment.this, getActivity());
            googleMap.setInfoWindowAdapter(parent.mla);
            googleMap.setOnInfoWindowClickListener(parent.mla);
        }
    };

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
        FindFragmentContainer parent = (FindFragmentContainer) getParentFragment();
        parent.flipFragment();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fab_broadcast:
                //Lien103 code starts here
                if (!sessionManager.getPromptBroadcast() && !sessionManager.getFromDialog())
                {
                    inflateBroadcastDialog();
                }
                else
                {
                    inflateBroadcastDialog();//not sure what to do here to start the broadcast, but not show the dialogue
                }
                break;
            case R.id.fab_list:
                openList();
                break;
            default:
                break;
        }
    }

    private ArrayList<MarkerLabelInfo> getBroadcasts() throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.become(sessionManager.getSessionToken());

        ParseQuery<Broadcast> q = new ParseQuery<Broadcast>(Broadcast.class);
        q.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now()))
                .whereWithinMiles(Broadcast.ATTR_LOCATION,
                        new ParseGeoPoint(parent.myLocation.getLatitude(), parent.myLocation.getLongitude()), sessionManager.getSearchRadius())
                .whereNotEqualTo(Broadcast.ATTR_BROADCASTER, user);

        List<Broadcast> list = q.find();
        ArrayList<MarkerLabelInfo> data = new ArrayList<MarkerLabelInfo>();
        int index = 0;
        for (Broadcast broadcast : list)
        {
            // .fetchIfNeeded() gets parent info, not just the parent objectId
            ASDPlaydateUser bcaster = (ASDPlaydateUser) broadcast.getBroadcaster().fetchIfNeeded();
            Child child = getChildWithParent(bcaster);
            LatLng latLng = LocationHelpers.toLatLng(broadcast.getLocation());
            MarkerLabelInfo markerLabelInfo = new MarkerLabelInfo(bcaster, child, latLng);
            markerLabelInfo.setIndex(index);
            data.add(markerLabelInfo);
        }

        return data;
    }

    private Child getChildWithParent(ASDPlaydateUser parent) throws Exception
    {
        ParseQuery<Child> q = new ParseQuery<Child>(Child.class);
        q.whereEqualTo(Child.ATTR_PARENT, parent);
        return q.find().get(0);
    }

    public void placeMarkers(ArrayList<MarkerLabelInfo> data)
    {
        googleMap.clear();
        for (int i = 0; i < data.size(); i++)
        {
            MarkerLabelInfo info = data.get(i);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(info.getLatLng()));
            info.setMarker(marker);
            data.set(i, info);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (locationChangedListener != null)
        {
            locationChangedListener.onLocationChanged(location);

            CameraPosition cp = new CameraPosition.Builder().target(LocationHelpers.toLatLng(location)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

            // Store location in field if location changes
            parent.myLocation = location;
        }

        if (!broadcasted)
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
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private Criteria setCriteriaFine()
    {
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        return criteria;
    }

    private Criteria setCriteriaCoarse()
    {
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        return criteria;
    }

    private String getBestProvider(Criteria criteria)
    {
        bestProvider = locationManager.getBestProvider(criteria, true);
        return bestProvider;
    }

    private void hideBroadcastBar()
    {
        ObjectAnimator slideDown = ObjectAnimator.ofFloat(broadcastBar, "translationY", 2000);
        slideDown.setDuration(500).start();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        locationChangedListener = onLocationChangedListener;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (getBestProvider(setCriteriaFine()) != null)
                locationManager.requestLocationUpdates(bestProvider, 7500, 10, this);
        }
        else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (getBestProvider(setCriteriaCoarse()) != null)
                locationManager.requestLocationUpdates(bestProvider, 7500, 10, this);
        }
    }

    @Override
    public void deactivate()
    {
        locationChangedListener = null;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.removeUpdates(this);
        }

    }
}
