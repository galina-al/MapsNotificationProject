package com.example.user.mapsproject;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {

    private double latitude;
    private double longitude;
    private String title;

    public MarkerItem() {
    }

    public MarkerItem(double latitude, double longitude, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public MarkerOptions getOptions(){
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker());
    }

}
