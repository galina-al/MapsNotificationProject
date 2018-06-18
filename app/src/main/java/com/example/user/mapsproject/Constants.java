package com.example.user.mapsproject;

public class Constants {
    public static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATE = 1; // in Meters
    public static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

    public static final long POINT_RADIUS = 300; // in Meters
    public static final long PROX_ALERT_EXPIRATION = -1;

    public static final String PROX_ALERT_INTENT =
            "com.javacodegeeks.android.lbs.ProximityAlert";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 666;
}
