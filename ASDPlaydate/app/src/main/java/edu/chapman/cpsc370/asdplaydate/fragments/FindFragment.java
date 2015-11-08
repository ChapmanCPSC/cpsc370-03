package edu.chapman.cpsc370.asdplaydate.fragments;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.MarkerLabelAdapter;

public class FindFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, GoogleMap.OnMarkerClickListener
{

    MapView map;
    GoogleMap googleMap;
    Location myLocation;
    GoogleApiClient googleApiClient;
    Button broadcast, list;
    AlertDialog broadcastDialog;
    FrameLayout broadcastBar;
    LocationRequest mLocationRequest;
    CheckBox broadcastCheckBox;
    TextView tv;
    HashMap<LatLng, String> hash;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    public FindFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        map = (MapView) rootView.findViewById(R.id.mapView);
        broadcastBar = (FrameLayout) rootView.findViewById(R.id.fl_broadcast_bar);
        broadcast = (Button) rootView.findViewById(R.id.btn_broadcast);
        broadcast.setOnClickListener(this);
        list = (Button) rootView.findViewById(R.id.btn_list);
        list.setOnClickListener(this);
        map.onCreate(savedInstanceState);
        map.onResume();
        googleMap = map.getMap();
        setUpMap();

        createLocationRequest();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        tv = (TextView) rootView.findViewById(R.id.tv_broadcast_bar);

        //LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        return rootView;
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    private LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            myLocation = location;
        }

    };


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
    }

    private void setUpMap()
    {
        if(googleMap != null)
        {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
            {
                @Override
                public void onMyLocationChange(Location location)
                {
                    myLocation = location;
                    zoomToMyLocation(myLocation);
                }
            });

        }
    }

    public Location getMyLocation()
    {
        if(myLocation != null)
            return myLocation;
        return null;
    }

    private void zoomToMyLocation(Location myLocation)
    {
        CameraPosition cp = new CameraPosition.Builder().target(locationToLatLng(myLocation)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    public LatLng locationToLatLng(Location location)
    {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if(googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(googleApiClient != null)
            googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        //PendingResult<Status> result = LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, );
        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, (LocationListener) this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(broadcast)
                .setNeutralButton(R.string.go, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();

                        ObjectAnimator slideDown = ObjectAnimator.ofFloat(broadcastBar, "translationY", 200);
                        slideDown.setDuration(500).start();

                        // Fake data
                        // TODO: constructor for MarkerLabelAdapter will pass a model of the data
                        createHashMap();
                        placeMarkers(hash);
                        MarkerLabelAdapter mla = new MarkerLabelAdapter(getContext(), hash);
                        googleMap.setInfoWindowAdapter(mla);

                    }
                });
        broadcastDialog = builder.create();
        broadcastDialog.show();
    }

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

        /*ResultListFragment list = new ResultListFragment();
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.pager, list);
        ft.hide(this);
        ft.show(list);
        ft.addToBackStack(null);
        ft.commit();
        */
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_broadcast:
                inflateBroadcastDialog();
                break;
            case R.id.btn_list:
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
        for(LatLng ll : hash.keySet())
        {
            googleMap.addMarker(new MarkerOptions().position(ll));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        return false;
    }
}
