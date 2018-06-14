package com.example.user.mapsproject.DB;


import com.example.user.mapsproject.MarkerItem;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MarkersRepository {

    private final MapsDbHelper databaseHelper;

    public MarkersRepository(MapsDbHelper databaseHelper) {

        this.databaseHelper = databaseHelper;
    }

    public List<MarkerItem> getAll() {
        return cupboard().withDatabase(databaseHelper.getWritableDatabase()).query(MarkerItem.class).list();
    }

    public void setMarkerItem(MarkerItem item) {
        cupboard().withDatabase(databaseHelper.getWritableDatabase()).put(item);
    }

    public MarkerItem getMarkerItem(MarkerItem item) {
        return cupboard().withDatabase(databaseHelper.getWritableDatabase()).get(item);
    }

    public void deleteMarkerItem(MarkerItem item) {
        cupboard().withDatabase(databaseHelper.getWritableDatabase()).delete(item);
    }

    public void deleteAll() {
        cupboard().withDatabase(databaseHelper.getWritableDatabase()).delete(MarkerItem.class, null);
    }


}
