package com.example.kelly.gasmeup.models;

/**
 * Created by Kelly on 10/7/15.
 */
public class FavoriteLocationModel {

    private String locationName;
    private String address;
    private int count;

    public FavoriteLocationModel(String locationName, String address, int count){
        this.locationName = locationName;
        this.address = address;
        this.count = count;
    }

    public FavoriteLocationModel(String locationName, String address){
        this.locationName = locationName;
        this.address = address;
        this.count = 1;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
