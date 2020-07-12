package io.github.terickson87.isgoodweatherforawalk;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import io.github.terickson87.isgoodweatherforawalk.models.location.Location;
import io.github.terickson87.isgoodweatherforawalk.models.weather.MinutelyWeatherData;
import io.github.terickson87.isgoodweatherforawalk.models.weather.WeatherData;

public class MainActivityViewModel extends ViewModel {

    private Repository mRepository;
    private MutableLiveData<Location> mLocation;
    private MutableLiveData<WeatherData> mWeatherData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init(Activity activity) {
        mRepository = Repository.getInstance();

        if (mLocation == null) {
            mLocation = mRepository.getLocation(activity);
        }

        if (mLocation != null && mWeatherData == null) {
            mWeatherData = mRepository.getWeatherData(activity, mLocation.getValue().getLatitude(), mLocation.getValue().getLongitude());
        }


    }

    public LiveData<Location> getLocation() {
        return mLocation;
    }

    public LiveData<WeatherData> getWeatherData() {
        return  mWeatherData;
    }

    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }
}
