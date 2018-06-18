package com.example.user.mapsproject.models;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.List;

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

}
