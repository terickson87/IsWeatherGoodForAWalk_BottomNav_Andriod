package io.github.terickson87.isgoodweatherforawalk.models.weather;

public class DailyTempFeelsLike {
    private double mDay;
    private double mNight;
    private double mEvening;
    private double mMorning;

    public DailyTempFeelsLike() { }

    public DailyTempFeelsLike(double day, double night, double evening, double morning) {
        mDay = day;
        mNight = night;
        mEvening = evening;
        mMorning = morning;
    }

    public double getDay() {
        return mDay;
    }

    public void setDay(double day) {
        mDay = day;
    }

    public double getNight() {
        return mNight;
    }

    public void setNight(double night) {
        mNight = night;
    }

    public double getEvening() {
        return mEvening;
    }

    public void setEvening(double evening) {
        mEvening = evening;
    }

    public double getMorning() {
        return mMorning;
    }

    public void setMorning(double morning) {
        mMorning = morning;
    }
}
