package com.example.user.mapsproject.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MarkerItem implements ClusterItem {

    private Long _id;
    private double latitude;
    private double longitude;
    private String title;
    private String key;

    public MarkerItem() {
    }

    public MarkerItem(double latitude, double longitude, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.key = latitude + "/" + longitude;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String getTitle() {
        return title;
    }


    @Override
    public String getSnippet() {
        return null;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public MarkerOptions getOptions() {
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker());
    }

    public String getAddress(Context context) {
        String resultAddress = "";
        try {

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(this.getPosition().latitude, this.getPosition().longitude, 1);

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

            resultAddress = newAddress + ", "
                    + addresses.get(0).getFeatureName() + "\n"
                    + addresses.get(0).getLocality() + ", "
                    + addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultAddress;
    }

}
