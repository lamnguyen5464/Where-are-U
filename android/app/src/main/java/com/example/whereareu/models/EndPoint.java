package com.example.whereareu.models;
import com.example.whereareu.helpers.JSONHelper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

public class EndPoint {
    public String id;
    public double latitude, longitude;

    public EndPoint(JSONObject response){
        this.id = JSONHelper.getFieldSafely(response, "id");

        String tmpLatitude = JSONHelper.getFieldSafely(response, "latitude");
        String tmpLongitude = JSONHelper.getFieldSafely(response, "longitude");

        if (!tmpLatitude.equals("") && !tmpLongitude.equals("")){
            this.latitude = Double.parseDouble(tmpLatitude);
            this.longitude = Double.parseDouble(tmpLongitude);
        }
    }

    public LatLng getLatLng(){
        return new LatLng(latitude, longitude);
    }
}
