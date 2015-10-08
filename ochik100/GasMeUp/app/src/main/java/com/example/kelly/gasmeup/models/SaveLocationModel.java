package com.example.kelly.gasmeup.models;

/**
 * Created by Kelly on 10/7/15.
 */
public class SaveLocationModel {

    public String locationName;
    public String address;

    public SaveLocationModel(String locationName, String address){
        this.locationName = locationName;
        this.address = address;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
