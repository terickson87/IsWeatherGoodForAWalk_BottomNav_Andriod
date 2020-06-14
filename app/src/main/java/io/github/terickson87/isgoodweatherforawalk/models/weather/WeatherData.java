package io.github.terickson87.isgoodweatherforawalk.models.weather;

import java.util.List;

public class WeatherData {
    private CurrentWeatherData mCurrentWeatherData;
    private List<MinutelyWeatherData> mMinutelyWeatherData;
    private List<HourlyWeatherData> mHourlyWeatherData;
    private List<DailyWeatherData> mDailyWeatherData;

    public WeatherData() { }

    public WeatherData(CurrentWeatherData currentWeatherData, List<MinutelyWeatherData> minutelyWeatherData,
                       List<HourlyWeatherData> hourlyWeatherData, List<DailyWeatherData> dailyWeatherData) {
        mCurrentWeatherData = currentWeatherData;
        mMinutelyWeatherData = minutelyWeatherData;
        mHourlyWeatherData = hourlyWeatherData;
        mDailyWeatherData = dailyWeatherData;
    }

    public CurrentWeatherData getCurrentWeatherData() {
        return mCurrentWeatherData;
    }

    public void setCurrentWeatherData(CurrentWeatherData currentWeatherData) {
        mCurrentWeatherData = currentWeatherData;
    }

    public List<MinutelyWeatherData> getMinutelyWeatherData() {
        return mMinutelyWeatherData;
    }

    public void setMinutelyWeatherData(List<MinutelyWeatherData> minutelyWeatherData) {
        mMinutelyWeatherData = minutelyWeatherData;
    }

    public List<HourlyWeatherData> getHourlyWeatherData() {
        return mHourlyWeatherData;
    }

    public void setHourlyWeatherData(List<HourlyWeatherData> hourlyWeatherData) {
        mHourlyWeatherData = hourlyWeatherData;
    }

    public List<DailyWeatherData> getDailyWeatherData() {
        return mDailyWeatherData;
    }

    public void setDailyWeatherData(List<DailyWeatherData> dailyWeatherData) {
        mDailyWeatherData = dailyWeatherData;
    }
}
