package com.example.user.mapsproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.mapsproject.DB.DB;
import com.example.user.mapsproject.DB.MarkersRepository;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 666;
    private static final int GEOFENCE_RADIUS = 2;
    private GoogleMap mMap;
    private FloatingActionButton addButton;
    private ImageView imageAddMarker;
    private SupportMapFragment mapFragment;
    private Toolbar toolbar;
    private GeofencingClient geofencingClient;
    private ClusterManager<MarkerItem> clusterManager;
    private MarkersRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageAddMarker = (ImageView) findViewById(R.id.imageAddMarker);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAddMarker.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);

            }
        });

        geofencingClient = LocationServices.getGeofencingClient(this);
        repository = DB.getDb().getMarkersRepository();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        clusterManager = new ClusterManager<MarkerItem>(this.getApplicationContext(), mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        clusterManager.cluster();

        List<MarkerItem> markerList = repository.getAll();
        clusterManager.addItems(markerList);
//        for (MarkerItem item : markerList) {
//            mMap.addMarker(item.getOptions());
//        }

        showCurrentLocation();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);

            String addressLine = addresses.get(0).getThoroughfare();
            String newAddress = new String();
            if (addressLine.contains("проспект")) {
                newAddress = addressLine.replace("проспект", "пр-т.");
            } else if (addresses.contains("переулок")) {
                newAddress = addressLine.replace("переулок", "п-к.");
            } else if (addresses.contains("площадь")) {
                newAddress = addressLine.replace("площадь", "пл.");
            } else if (addresses.contains("бульвар")) {
                newAddress = addressLine.replace("бульвар", "б-р.");
            } else if (addresses.contains("улица")) {
                newAddress = addressLine.replace("улица", "ул.");
            }

            String description = newAddress + ", "
                    + addresses.get(0).getFeatureName() + "\n"
                    + addresses.get(0).getLocality() + ", "
                    + addresses.get(0).getCountryName();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), description, BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.setAction("Delete", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> markerId = new ArrayList<>();
                    MarkerItem markerItem = new MarkerItem(
                            marker.getPosition().latitude,
                            marker.getPosition().longitude,
                            marker.getTitle());
                    markerId.add(markerItem.getLatitude() + "/" + markerItem.getLongitude());
                    if (removeLocationAlert(markerId)) {
                        repository.deleteMarkerItem(markerItem);
                        List<MarkerItem> list = repository.getAll();
                        clusterManager.clearItems();
                        clusterManager.addItems(list);
                        clusterManager.cluster();
                    }
                }

            });
            snackbar.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void showCurrentLocation() {
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    public boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCurrentLocation();
                    Toast.makeText(MapsActivity.this,
                            "Location access permission granted, you try " +
                                    "add or remove location allerts",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_marker, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ok_button:
                final LatLng selectedLocal = mMap.getCameraPosition().target;
                final EditText textNotify = new EditText(this);
                final int[] selectedTrigger = new int[1];
                selectedTrigger[0] = GeofencingRequest.INITIAL_TRIGGER_ENTER;
                new AlertDialog.Builder(this)
                        .setTitle(R.string.notification)
                        .setView(textNotify)
                        .setSingleChoiceItems(new String[]{"Enter", "Exit"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    selectedTrigger[0] = GeofencingRequest.INITIAL_TRIGGER_ENTER;
                                } else if (which == 1) {
                                    selectedTrigger[0] = GeofencingRequest.INITIAL_TRIGGER_EXIT;
                                }
                            }
                        })
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addLocationAlert(selectedLocal.latitude,
                                        selectedLocal.longitude,
                                        selectedTrigger[0],
                                        textNotify.getText().toString());
                                imageAddMarker.setVisibility(View.GONE);
                                addButton.setVisibility(View.VISIBLE);
                                toolbar.setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
                return true;
            case android.R.id.home:
                imageAddMarker.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingPermission")
    private void addLocationAlert(double latitude, double longitude, int trigger, String textNotify) {
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else {
            MarkerItem item = new MarkerItem(latitude, longitude, textNotify);
            DB.getDb().getMarkersRepository().setMarkerItem(item);

            clusterManager.addItem(item);
            clusterManager.cluster();

            String key = latitude + "/" + longitude;
            Geofence geofence = getGeofence(latitude, longitude, key, trigger);
            geofencingClient.addGeofences(getGeofencingRequest(geofence, trigger),
                    getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MapsActivity.this,
                                        "Location alter has been added",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapsActivity.this,
                                        "Location alter could not be added",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, LocationAlertIntentService.class);
        return PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence, int trigger) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(trigger);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private Geofence getGeofence(double lat, double lang, String key, int trigger) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(trigger)
                .build();
    }

    private boolean removeLocationAlert(List<String> requestId) {
        final Boolean[] result = {true};
        if (isLocationAccessPermitted()) {
            requestLocationAccessPermission();
        } else {
            geofencingClient.removeGeofences(requestId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                result[0] = true;
                                Toast.makeText(MapsActivity.this,
                                        "Location alters have been removed",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                result[0] = false;
                                Toast.makeText(MapsActivity.this,
                                        "Location alters could not be removed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        return result[0];
    }
}