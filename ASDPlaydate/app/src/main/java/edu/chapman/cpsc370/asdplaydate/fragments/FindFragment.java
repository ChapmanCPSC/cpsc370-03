package edu.chapman.cpsc370.asdplaydate.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;
import java.util.List;

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
    FindFragmentContainer parent;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (firstLoad)
        {
            // Skip the setup the next time onCreateView is called
            firstLoad = false;

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
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

        sessionManager = new SessionManager(getActivity());
        parent = (FindFragmentContainer) getParentFragment();

        return rootView;
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
            }

            googleMap.setLocationSource(this);
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View broadcast = inflater.inflate(R.layout.broadcast_dialog, null);
        broadcastCheckBox = (CheckBox) broadcast.findViewById(R.id.cb_dont_ask_again);
        doBroadcastFab = (FloatingActionButton) broadcast.findViewById(R.id.fab_go);
        doBroadcastFab.setOnClickListener(onClickListener);
        broadcastDuration = (SeekBar) broadcast.findViewById(R.id.sb_broadcast_duration);
        broadcastDuration.setOnSeekBarChangeListener(onSeekBarChangeListener);
        progressValue = (TextView) broadcast.findViewById(R.id.tv_duration_progress);
        progressValue.setText("60" + " " + getString(R.string.minutes));
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
            //TODO: Set SharedPrefs isChecked here

            broadcastDialog.cancel();

            ObjectAnimator slideDown = ObjectAnimator.ofFloat(broadcastBar, "translationY", 2000);
            slideDown.setDuration(500).start();

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
            MarkerLabelAdapter mla = new MarkerLabelAdapter(FindFragment.this, getActivity(), parent.broadcasts);
            googleMap.setInfoWindowAdapter(mla);
            googleMap.setOnInfoWindowClickListener(mla);
        }
    };

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
                // TODO: if isChecked == true, do not show broadcast dialog again
                inflateBroadcastDialog();
                break;
            case R.id.fab_list:
                openList();
                break;
            default:
                break;
        }
    }

    private HashMap<LatLng, MarkerLabelInfo> getBroadcasts() throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.become(sessionManager.getSessionToken());

        ParseQuery<Broadcast> q = new ParseQuery<Broadcast>(Broadcast.class);
        q.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now()))
                .whereWithinMiles(Broadcast.ATTR_LOCATION,
                        new ParseGeoPoint(parent.myLocation.getLatitude(), parent.myLocation.getLongitude()), sessionManager.getSearchRadius())
                .whereNotEqualTo(Broadcast.ATTR_BROADCASTER, user);

        List<Broadcast> list = q.find();
        HashMap<LatLng, MarkerLabelInfo> info = new HashMap<LatLng, MarkerLabelInfo>();
        for (Broadcast broadcast : list)
        {
            // .fetchIfNeeded() gets parent info, not just the parent objectId
            ASDPlaydateUser bcaster = (ASDPlaydateUser) broadcast.getBroadcaster().fetchIfNeeded();
            Child child = getChildWithParent(bcaster);
            LatLng location = LocationHelpers.toLatLng(broadcast.getLocation());
            MarkerLabelInfo markerLabelInfo = new MarkerLabelInfo(bcaster, child, location);
            info.put(location, markerLabelInfo);
        }

        return info;
    }

    private Child getChildWithParent(ASDPlaydateUser parent) throws Exception
    {
        ParseQuery<Child> q = new ParseQuery<Child>(Child.class);
        q.whereEqualTo(Child.ATTR_PARENT, parent);
        return q.find().get(0);
    }

    public void placeMarkers(HashMap<LatLng, MarkerLabelInfo> hash)
    {
        googleMap.clear();
        for (LatLng ll : hash.keySet())
        {
            googleMap.addMarker(new MarkerOptions().position(ll));
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
