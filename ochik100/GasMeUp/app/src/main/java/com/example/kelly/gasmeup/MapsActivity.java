package com.example.kelly.gasmeup;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelly.gasmeup.models.DirectionModel;
import com.example.kelly.gasmeup.tasks.GetDistanceTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.widget.Toast.makeText;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    EditText origin;
    EditText destination;
    Button search, clear;
    String origin_location, dest_location, distance;
    TextView output_distance, output_duration;

    private float lat, lng;
    private String marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        origin = (EditText)findViewById(R.id.et_origin);
        destination = (EditText)findViewById(R.id.et_dest);
        search = (Button)findViewById(R.id.btn_search);
        clear = (Button)findViewById(R.id.btn_clear);
        output_distance = (TextView)findViewById(R.id.tv_distance);
        output_duration = (TextView)findViewById(R.id.tv_duration);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                origin_location = origin.getText().toString();
                dest_location = destination.getText().toString();

                if(!(origin_location.isEmpty()) || !(dest_location.isEmpty())){
                    if(origin_location.contains(" ")){
                        origin_location = origin_location.replace(" ", "+");
                    }
                    if(dest_location.contains(" ")){
                        dest_location = dest_location.replace(" ", "+");
                    }
                    GetDistanceTask distanceTask = new GetDistanceTask(MapsActivity.this);
                    distanceTask.execute(origin_location, dest_location);
                } else {
                    Toast.makeText(MapsActivity.this, "Missing a field, try again!", Toast.LENGTH_LONG).show();
                }


            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin.setText("");
                destination.setText("");
                output_distance.setText("");
                output_duration.setText("");
                mMap.clear();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())).title("You are here!"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void loadingFinished(DirectionModel model){

        output_distance.setText(model.routes[0].legs[0].distance.text);
        output_duration.setText(model.routes[0].legs[0].duration.text);
        lat = model.routes[0].legs[0].end_location.lat;
        lng = model.routes[0].legs[0].end_location.lng;
        marker = model.routes[0].legs[0].end_address;
        setMarker(lat, lng, marker);

    }

    public void setMarker(float lat, float lng, String marker){

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(marker));
        CameraPosition camPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(10).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));

    }
}
