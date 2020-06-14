package io.github.terickson87.isgoodweatherforawalk.models.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Location {
    private static String TAG = "Location";
    private static final int mc_MAX_LOCATION_RESULTS = 1;
    private double mLatitude;
    private double mLongitude;
    private String mCityName;
    private String mStateName;
    private String mCountryCode;

    public Location() {}

    public Location(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Location(double latitude, double longitude, Context context) {
        mLatitude = latitude;
        mLongitude = longitude;
        setCityStateCountry(context);
    }

    // Setters and Getters
    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getStateName() {
        return mStateName;
    }

    public void setStateName(String mStateName) {
        this.mStateName = mStateName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    // Other methods
    public void setCityStateCountry(Context context) {
        Address address = getAddress(context);
        mCityName = Location.getCityName(address);
        mStateName = Location.getStateName(address);
        mCountryCode = Location.getCountryCode(address);

    }

    public Address getAddress(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(mLatitude, mLongitude, mc_MAX_LOCATION_RESULTS);
            return addresses.get(0);
        } catch (IOException e) {
            Log.e(TAG, "Error getting Address: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Static methods
    public static String getCityName(Address address) {
        return address.getLocality();
    }

    public static String getStateName(Address address){
        return address.getAdminArea();
    }

    public static String getCountryCode(Address address) {
        return address.getCountryCode();
    }
}
