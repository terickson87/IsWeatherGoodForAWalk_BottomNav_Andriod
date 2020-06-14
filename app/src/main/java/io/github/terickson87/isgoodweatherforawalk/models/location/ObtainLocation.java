package io.github.terickson87.isgoodweatherforawalk.models.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Gets the location from the android.location library and reports it.
 */
public class ObtainLocation implements LocationListener {
    public static final int mc_REQUEST_LOCATION_PERMISSION = 1;
    public static final int mc_MAX_LOCATION_RESULTS = 1;
    public static final String TAG = "GetLocation";

    private Activity mActivity;
    private Double mLatitude;
    private Double mLongitude;
    private String mCityName;
    private String mStateName;
    private String mCountryCode;
    private LocationCallback mLocationCallback = null;
    public LocationManager mLocationManager;

    // ***** Constructor *****
    public ObtainLocation(Activity activity) {
        mActivity = activity;
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
    }

    public ObtainLocation(Activity activity, LocationCallback locationCallback) {
        mActivity = activity;
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        mLocationCallback = locationCallback;
    }

    // ***** Main functionality *****
    public void retrieveLocation(LocationCallback locationCallback) {
        getPermissions(true, locationCallback);
    }

    public void retrieveLocation() {
        getPermissions(true);
    }

    private boolean isCoarseLocationPermission() {
        return ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isFineLocationPermission() {
        return ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationPermissions() {
        return  isCoarseLocationPermission() && isFineLocationPermission();
    }

    public void getPermissions(boolean isGetLocation, LocationCallback locationCallback) {
        if (locationCallback != null) {
            mLocationCallback = locationCallback;
        }

        if (!isCoarseLocationPermission() || !isFineLocationPermission()) {
            Log.i(TAG, " - GetPermissions: Location Permissions Not Yet Granted.");
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, mc_REQUEST_LOCATION_PERMISSION);
        } else {
            Log.i(TAG, " - GetPermissions: Location Permissions Granted.");
            if (isGetLocation) {
                getAndHandleGoodLocation();
            }
        }
    }

    public void getPermissions(boolean isGetLocation) {
        getPermissions(isGetLocation, null);
    }

    private void getAndHandleGoodLocation() {
        // This check must be local to prevent lint errors
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermissions(true);
            return;
        }

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            Log.i(TAG, "getAndHandleGoodLocation - Non Null Location");
            handleGoodLocation(location);

        } else {
            Log.i(TAG, "getAndHandleGoodLocation - Null Location");
            Criteria criteria = new Criteria();
            String bestProvider = String.valueOf(mLocationManager.getBestProvider(criteria, true)).toString();
            mLocationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        }
    }

    private void handleGoodLocation(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        Log.i(TAG, " handleGoodLocation() - " + "lat: " + mLatitude + ", long: " + mLongitude);
        Address address = getAddress(mActivity, mLatitude, mLongitude);
        mCityName = getCityName(address);
        Log.i(TAG, "City Name: " + mCityName);
        mStateName = getStateName(address);
        Log.i(TAG, "State Name: " + mStateName);
        mCountryCode = getCountryCode(address);
        Log.i(TAG, "Country Code: " + mCountryCode);

        if (mLocationCallback != null) {
            mLocationCallback.onSuccess(mLatitude, mLongitude);
        }
    }

    // ***** Utility Methods *****
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == mc_REQUEST_LOCATION_PERMISSION) {
            if(grantResults.length==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission granted");
                getAndHandleGoodLocation();
            }
        } else {
            Log.i(TAG, "onRequestPermissionsResult(): requestCode (" + requestCode + ") not equal to " + mc_REQUEST_LOCATION_PERMISSION);
        }
    }

    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, mc_MAX_LOCATION_RESULTS);
            return addresses.get(0);
        } catch (IOException e) {
            Log.e(TAG, "Error getting Address: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getCityName(Address address) {
        return address.getLocality();
    }

    public static String getStateName(Address address){
        return address.getAdminArea();
    }

    public static String getCountryCode(Address address) {
        return address.getCountryCode();
    }

    // ***** Setters *****
    public void setLatitude(Double latitude) {
        this.mLatitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.mLongitude = longitude;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }

    public void setStateName(String stateName) {
        this.mStateName = stateName;
    }

    // ***** Getters *****
    public Double getLatitude() {
        return mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getStateName() {
        return mStateName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    /**
     * Called when the location has changed.
     *
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {
        // remove location callback:
        mLocationManager.removeUpdates(this);

        // Handle good location
        Log.i(TAG, "onLocationChange(): Location Received");
        handleGoodLocation(location);
    }

    /**
     * This callback will never be invoked and providers can be considers as always in the
     * @link LocationProvider#AVAILABLE state.
     *
     * @param provider
     * @param status
     * @param extras
     * @deprecated This callback will never be invoked.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
}
