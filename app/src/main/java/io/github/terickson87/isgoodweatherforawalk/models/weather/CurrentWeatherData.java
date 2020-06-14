package io.github.terickson87.isgoodweatherforawalk.models.weather;

import java.time.Instant;

public class CurrentWeatherData {
    private Instant mTime;
    private Instant mSunsetTime;
    private Instant mSunriseTime;
    private double mTemperature;
    private double mTemperatureFeelsLike;
    private int mPercentCloudy;
    private double mWindSpeed;
    private double mWindGust;
    private double mPrecipitationVolume; // mm
    private double mSnowVolume; // mm
    private WeatherField mWeatherField;

    public CurrentWeatherData() { }

    public CurrentWeatherData(Instant time, Instant sunsetTime, Instant sunriseTime, double temperature,
                              double temperatureFeelsLike, int percentCloudy, double windSpeed,
                              double windGust, double precipitationVolume, double snowVolume,
                              WeatherField weatherField) {
        mTime = time;
        mSunsetTime = sunsetTime;
        mSunriseTime = sunriseTime;
        mTemperature = temperature;
        mTemperatureFeelsLike = temperatureFeelsLike;
        mPercentCloudy = percentCloudy;
        mWindSpeed = windSpeed;
        mWindGust = windGust;
        mPrecipitationVolume = precipitationVolume;
        mSnowVolume = snowVolume;
        mWeatherField = weatherField;
    }

    public Instant getTime() {
        return mTime;
    }

    public void setTime(Instant time) {
        mTime = time;
    }

    public Instant getSunsetTime() {
        return mSunsetTime;
    }

    public void setSunsetTime(Instant sunsetTime) {
        mSunsetTime = sunsetTime;
    }

    public Instant getSunriseTime() {
        return mSunriseTime;
    }

    public void setSunriseTime(Instant sunriseTime) {
        mSunriseTime = sunriseTime;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getTemperatureFeelsLike() {
        return mTemperatureFeelsLike;
    }

    public void setTemperatureFeelsLike(double temperatureFeelsLike) {
        mTemperatureFeelsLike = temperatureFeelsLike;
    }

    public int getPercentCloudy() {
        return mPercentCloudy;
    }

    public void setPercentCloudy(int percentCloudy) {
        mPercentCloudy = percentCloudy;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public double getWindGust() {
        return mWindGust;
    }

    public void setWindGust(double windGust) {
        mWindGust = windGust;
    }

    public double getPrecipitationVolume() {
        return mPrecipitationVolume;
    }

    public void setPrecipitationVolume(double precipitationVolume) {
        mPrecipitationVolume = precipitationVolume;
    }

    public double getSnowVolume() {
        return mSnowVolume;
    }

    public void setSnowVolume(double snowVolume) {
        mSnowVolume = snowVolume;
    }

    public WeatherField getWeatherField() {
        return mWeatherField;
    }

    public void setWeatherField(WeatherField weatherField) {
        mWeatherField = weatherField;
    }
}
