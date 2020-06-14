package io.github.terickson87.isgoodweatherforawalk;

import androidx.lifecycle.MutableLiveData;

import io.github.terickson87.isgoodweatherforawalk.models.location.GetLocation;
import io.github.terickson87.isgoodweatherforawalk.models.weather.OpenWeatherApi;

public class Repository {
    // Singleton Pattern
    private static Repository mRepository;

    public static Repository getInstance() {
        if (mRepository == null) {
            mRepository = new Repository();
        }
        return mRepository;
    }

    private OpenWeatherApi mOpenWeatherApi;
    private GetLocation mGetLocation;

    public MutableLiveData<OpenWeatherApi> getWeatherData() {

        MutableLiveData<OpenWeatherApi> openWeatherApi = new MutableLiveData<>();
        openWeatherApi.setValue(mOpenWeatherApi);
        return openWeatherApi;
    }

    public MutableLiveData<GetLocation> getLocation() {

        MutableLiveData<GetLocation> getLocation = new MutableLiveData<>();
        getLocation.setValue(mGetLocation);
        return getLocation;
    }
}
