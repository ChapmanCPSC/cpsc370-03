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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
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

import java.util.HashMap;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.MarkerLabelAdapter;

public class FindFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, LocationSource, android.location.LocationListener
{

    MapView map;
    GoogleMap googleMap;
    Location myLocation;
    GoogleApiClient googleApiClient;
    SeekBar broadcastDuration;
    TextView progressValue;
    FloatingActionButton list, broadcastGo, broadcast;
    AlertDialog broadcastDialog;
    FrameLayout broadcastBar;
    LocationRequest mLocationRequest;
    CheckBox broadcastCheckBox;
    HashMap<LatLng, String> hash;

    LocationManager locationManager;
    OnLocationChangedListener locationChangedListener;
    private final Criteria criteria = new Criteria();
    String bestProvider;

    View rootView;
    boolean firstLoad = true;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

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
            map = (MapView) rootView.findViewById(R.id.mapView);
            broadcastBar = (FrameLayout) rootView.findViewById(R.id.fl_broadcast_bar);
            broadcast = (FloatingActionButton) rootView.findViewById(R.id.fab_broadcast);
            broadcast.setOnClickListener(this);
            list = (FloatingActionButton) rootView.findViewById(R.id.fab_list);
            list.hide();
            list.setOnClickListener(this);
            broadcastDuration = (SeekBar) rootView.findViewById(R.id.sb_broadcast_duration);

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            map.onCreate(savedInstanceState);
            map.onResume();
            setUpMapIfNeeded();
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }

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
            googleMap = map.getMap();

            if (googleMap != null)
            {
                setUpMap();
            }

            googleMap.setLocationSource(this);
        }
    }

    public LatLng locationToLatLng(Location location)
    {
        return new LatLng(location.getLatitude(), location.getLongitude());
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
        broadcastCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
        broadcastGo = (FloatingActionButton) broadcast.findViewById(R.id.fab_go);
        broadcastGo.setOnClickListener(onClickListener);
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
            broadcastDialog.cancel();

            ObjectAnimator slideDown = ObjectAnimator.ofFloat(broadcastBar, "translationY", 2000);
            slideDown.setDuration(500).start();

            list.show();

            // TODO: constructor for MarkerLabelAdapter will pass a model of the data
            createHashMap();
            placeMarkers(hash);
            MarkerLabelAdapter mla = new MarkerLabelAdapter(getActivity(), hash);
            googleMap.setInfoWindowAdapter(mla);
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

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            // TODO: if isChecked == true, do not show broadcast dialog again
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
                inflateBroadcastDialog();
                break;
            case R.id.fab_list:
                openList();
                break;
            default:
                break;
        }
    }

    public void createHashMap()
    {
        hash = new HashMap<LatLng, String>();
        hash.put(new LatLng(33.804, -117.85), "John Doe");
        hash.put(new LatLng(33.7929, -117.80), "Jane Doe");
        hash.put(new LatLng(33.8134, -117.85266), "Bob Smith");
    }

    public void placeMarkers(HashMap<LatLng, String> hash)
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

            CameraPosition cp = new CameraPosition.Builder().target(locationToLatLng(location)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private void setCriteria()
    {
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
    }

    private String getBestProvider()
    {
        setCriteria();
        bestProvider = locationManager.getBestProvider(criteria, true);
        return bestProvider;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        locationChangedListener = onLocationChangedListener;

        if (getBestProvider() != null)
        {
            if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(bestProvider, 7500, 10, this);
            }

        }
    }

    @Override
    public void deactivate()
    {
        locationChangedListener = null;
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.removeUpdates(this);
        }

    }
}
