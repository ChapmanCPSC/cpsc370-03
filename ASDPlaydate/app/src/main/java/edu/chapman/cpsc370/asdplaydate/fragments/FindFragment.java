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
import com.parse.ParseGeoPoint;

import org.joda.time.DateTime;

import edu.chapman.cpsc370.asdplaydate.R;
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
    boolean broadcasted = false;

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
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }

        }

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
                sessionManager.storePromptBroadcast(broadcastCheckBox.isChecked());
                ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
                ParseGeoPoint location = new ParseGeoPoint(parent.myLocation.getLatitude(), parent.myLocation.getLongitude());
                String message = broadcastMessage.getText().toString();
                DateTime expireDate = DateTime.now().plusMinutes(sessionManager.getBroadcastDuration());
                Broadcast b = new Broadcast(broadcaster, location, message, expireDate);//add broadcast to PARSE with message
                b.saveInBackground();

            }
            catch (Exception ex)
            {
                Toast.makeText(getActivity(),"Oops, something went wrong with your broadcast. Please try again!",Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
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
            refreshFab.show();

            updateMap();
        }
    };

    public void startNonDialogueBroadcast()
    {
        try
        {
            ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
            ParseGeoPoint location = new ParseGeoPoint(parent.myLocation.getLatitude(), parent.myLocation.getLongitude());
            String message = sessionManager.getBroadcastMessage();
            DateTime expireDate = DateTime.now().plusMinutes(sessionManager.getBroadcastDuration());
            Broadcast b = new Broadcast(broadcaster, location, message, expireDate);//add broadcast to PARSE with message
            b.saveInBackground();

        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity(),"TESTOops, something went wrong with your broadcast. Please try again!",Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        broadcasted = true;//leave this here

        hideBroadcastBar();

        listFab.show();
        refreshFab.show();

        updateMap();
    }

    public void updateMap()
    {
        try
        {
            // Update the UI of both fragments here
            parent.updateUI(sessionManager);
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
                if (!sessionManager.getPromptBroadcast() && !sessionManager.getFromDialog())
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

    private void hideBroadcastBar()
    {
        ObjectAnimator slideDown = ObjectAnimator.ofFloat(broadcastBar, "translationY", 2000);
        slideDown.setDuration(500).start();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        locationChangedListener = onLocationChangedListener;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7500, 10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 7500, 10, this);
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
