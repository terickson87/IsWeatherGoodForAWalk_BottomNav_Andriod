package io.github.terickson87.isgoodweatherforawalk;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import io.github.terickson87.isgoodweatherforawalk.models.location.ObtainLocation;
import io.github.terickson87.isgoodweatherforawalk.models.location.Location;
import io.github.terickson87.isgoodweatherforawalk.models.location.LocationCallback;
import io.github.terickson87.isgoodweatherforawalk.models.weather.ApiCallback;
import io.github.terickson87.isgoodweatherforawalk.models.weather.OpenWeatherApi;
import io.github.terickson87.isgoodweatherforawalk.models.weather.WeatherData;

public class Repository {
    // Singleton Pattern
    private static Repository mRepository;

    public static Repository getInstance() {
        if (mRepository == null) {
            mRepository = new Repository();
        }
        return mRepository;
    }

    private WeatherData mWeatherData;
    private Location mLocation;

    public MutableLiveData<WeatherData> getWeatherData(Context context, double latitude, double longitude) {

        MutableLiveData<WeatherData> weatherDataLiveData = new MutableLiveData<>();
        OpenWeatherApi openWeatherApi = new OpenWeatherApi(context, latitude, longitude);
        ApiCallback apiCallback = new ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                openWeatherApi.parseResponse(response);
                mWeatherData = openWeatherApi.getWeatherData();
                weatherDataLiveData.setValue(mWeatherData);
            }
        };
        openWeatherApi.callApi(apiCallback);

        return weatherDataLiveData;
    }

    public MutableLiveData<Location> getLocation(Activity activity) {

        MutableLiveData<Location> locationLiveData = new MutableLiveData<>();
        ObtainLocation obtainLocation = new ObtainLocation(activity);
        obtainLocation.retrieveLocation(new LocationCallback() {
            @Override
            public void onSuccess(Double latitude, Double longitude) {
                mLocation = new Location(latitude, longitude, activity.getApplicationContext());
                locationLiveData.setValue(mLocation);
                getWeatherData(activity, latitude, longitude);
            }
        });

        return locationLiveData;
    }
}
