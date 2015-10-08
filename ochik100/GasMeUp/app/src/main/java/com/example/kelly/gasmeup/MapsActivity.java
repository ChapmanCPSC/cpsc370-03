package com.example.kelly.gasmeup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelly.gasmeup.adapters.FavoriteLocationAdapter;
import com.example.kelly.gasmeup.adapters.SavedLocationAdapter;
import com.example.kelly.gasmeup.database.LocationDataProvider;
import com.example.kelly.gasmeup.database.SaveLocationEntry;
import com.example.kelly.gasmeup.models.DirectionModel;
import com.example.kelly.gasmeup.models.FavoriteLocationModel;
import com.example.kelly.gasmeup.models.SaveLocationModel;
import com.example.kelly.gasmeup.tasks.GetDistanceTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.makeText;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public String latlng;
    public Location myLocation;
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
        forceMapSetup();

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
                    final GetDistanceTask distanceTask = new GetDistanceTask(MapsActivity.this);
                    distanceTask.onFinish = new Runnable() {
                        @Override
                        public void run() {
                            LocationDataProvider.InsertFavoriteLocation(new FavoriteLocationModel(origin_location, dest_location), MapsActivity.this);
                            loadingFinished(distanceTask.result);
                        }
                    };
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

    public String getLatLng(Location _location) throws IOException {
        //String params  = URLEncoder.encode()
        getAddress(_location);
        return String.valueOf(_location.getLatitude()) +","+ String.valueOf(_location.getLongitude());

    }

    public String getAddress(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getAddressLine(1);
        String country = addresses.get(0).getAddressLine(2);
        String final_address = address+". "+city+","+country;
        return final_address;
    }

    private void forceMapSetup(){
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

            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                    myLocation = location;
                    //latlng = getLatLng(location);
                }
            });
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                        myLocation = location;
                        //latlng = getLatLng(location);
                    }
                });
                setUpMap();
            }
        }

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                myLocation = location;
                try {
                    latlng = getLatLng(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        });

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
        if (mMap != null) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                    myLocation = location;

                }
            });
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void manage(MenuItem item){
        final LayoutInflater inflater = getLayoutInflater();

        View modify = inflater.inflate(R.layout.dialog_manage, null);
        modify.findViewById(R.id.btn_add).setVisibility(View.GONE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(modify)
                .setTitle(R.string.recent_searches);
        builder.create().show();

        ListView favorites =(ListView) modify.findViewById(R.id.lv_favorites);
        Button add = (Button) modify.findViewById(R.id.btn_add);

        final List<FavoriteLocationModel> locations = LocationDataProvider.GetFavoriteLocations(this);
        FavoriteLocationAdapter adapter = new FavoriteLocationAdapter(this, locations, myLocation);
        favorites.setAdapter(adapter);

        favorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



                View newView = inflater.inflate(R.layout.dialog_save, null);
                final EditText locationName = (EditText)newView.findViewById(R.id.et_save);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                builder.setView(newView)
                        .setTitle(R.string.manage_favorites)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String location = locationName.getText().toString();
                                final String address = locations.get(position).getAddress();

                                LocationDataProvider.InsertSavedLocation(
                                        new SaveLocationModel(location, address), MapsActivity.this);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });

    }

    public void favorite(MenuItem item){

        final LayoutInflater inflater = getLayoutInflater();

        View modify = inflater.inflate(R.layout.dialog_manage, null);
        modify.findViewById(R.id.btn_add).setVisibility(View.GONE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(modify)
                .setTitle(R.string.view_favorites);
        builder.create().show();

        ListView favorites =(ListView) modify.findViewById(R.id.lv_favorites);
        final List<SaveLocationModel> locations = LocationDataProvider.GetSavedLocations(this);
        SavedLocationAdapter adapter = new SavedLocationAdapter(this, locations, myLocation);
        favorites.setAdapter(adapter);

    }

    public void setMarker(float lat, float lng, String marker){

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(marker));
        CameraPosition camPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(10).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));

    }
}
