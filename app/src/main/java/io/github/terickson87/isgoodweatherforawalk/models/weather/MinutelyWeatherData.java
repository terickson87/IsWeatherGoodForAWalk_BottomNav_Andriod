package io.github.terickson87.isgoodweatherforawalk.models.weather;

import java.time.Instant;

public class MinutelyWeatherData {
    private Instant mTime;
    private double mPrecipitationVolume; // mm

    public MinutelyWeatherData() { }

    public MinutelyWeatherData(Instant time, double precipitationVolume) {
        mTime = time;
        mPrecipitationVolume = precipitationVolume;
    }

    public Instant getTime() {
        return mTime;
    }

    public void setTime(Instant time) {
        mTime = time;
    }

    public double getPrecipitationVolume() {
        return mPrecipitationVolume;
    }

    public void setPrecipitationVolume(double precipitationVolume) {
        mPrecipitationVolume = precipitationVolume;
    }
}
