package io.github.terickson87.isgoodweatherforawalk.models.weather;

import java.time.Instant;

public class DailyWeatherData {
    private Instant mTime;
    private Instant mSunsetTime;
    private Instant mSunriseTime;
    private DailyTemp mDailyTemp;
    private DailyTempFeelsLike mDailyTempFeelsLike;
    private int mPercentCloudy;
    private double mWindSpeed;
    private double mWindGust;
    private double mPrecipitationVolume; // mm
    private double mSnowVolume; // mm
    private WeatherField mWeatherField;

    public DailyWeatherData() { }

    public DailyWeatherData(Instant time, Instant sunsetTime, Instant sunriseTime, DailyTemp dailyTemp,
                            DailyTempFeelsLike dailyTempFeelsLike, int percentCloudy, double windSpeed,
                            double windGust, double precipitationVolume, double snowVolume, WeatherField weatherField) {
        mTime = time;
        mSunsetTime = sunsetTime;
        mSunriseTime = sunriseTime;
        mDailyTemp = dailyTemp;
        mDailyTempFeelsLike = dailyTempFeelsLike;
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

    public DailyTemp getDailyTemp() {
        return mDailyTemp;
    }

    public void setDailyTemp(DailyTemp dailyTemp) {
        mDailyTemp = dailyTemp;
    }

    public DailyTempFeelsLike getDailyTempFeelsLike() {
        return mDailyTempFeelsLike;
    }

    public void setDailyTempFeelsLike(DailyTempFeelsLike dailyTempFeelsLike) {
        mDailyTempFeelsLike = dailyTempFeelsLike;
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
