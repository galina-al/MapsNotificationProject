package com.example.user.mapsproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 666;
    private GoogleMap mMap;
    private FusedLocationProviderClient providerClient;
    Geocoder geocoder;
    private FloatingActionButton addButton;
    private ImageView addMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this, Locale.getDefault());


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addMarker = (ImageView) findViewById(R.id.addMarker);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMarker.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (checkLocationPermission()) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission. ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//                //Request location updates:
//                locationManager.requestLocationUpdates(provider, 400, 1, this);
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_marker, menu);
        return true;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        providerClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            providerClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String locationName = addresses.get(0).getAddressLine(0);
                    LatLng fromLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    LatLng southWest = new LatLng(location.getLatitude() - 0.1, location.getLongitude() - 0.1);
                    LatLng northEast = new LatLng(location.getLatitude() + 0.1, location.getLongitude() + 0.1);
                    LatLngBounds bounds = new LatLngBounds(southWest, northEast);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                    mMap.addMarker(new MarkerOptions().position(fromLocation).title(locationName));
                    if (location == null) {

                    }
                }
            });
        } else {

        }


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                //показываем диалог
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Юзер одобрил
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                //запрашиваем пермишен, уже не показывая диалогов с пояснениями
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // пермишен получен можем работать с locationManager
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

//                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);

                        mMap.setMyLocationEnabled(true);
                        providerClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String locationName = addresses.get(0).getAddressLine(0);
                                LatLng fromLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                LatLng southWest = new LatLng(location.getLatitude() - 0.1, location.getLongitude() - 0.1);
                                LatLng northEast = new LatLng(location.getLatitude() + 0.1, location.getLongitude() + 0.1);
                                LatLngBounds bounds = new LatLngBounds(southWest, northEast);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                                mMap.addMarker(new MarkerOptions().position(fromLocation).title(locationName));
                                if (location == null) {

                                }
                            }
                        });
                    }

                } else {

                }
                return;
            }

        }
    }
}
