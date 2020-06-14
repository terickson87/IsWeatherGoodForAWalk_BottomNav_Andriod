package io.github.terickson87.isgoodweatherforawalk.models.location;

public class Location {
    private double mLatitude;
    private double mLongitude;
    private String mCityName;
    private String mStateName;
    private String mCountryCode;

    public Location() {}

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
}
