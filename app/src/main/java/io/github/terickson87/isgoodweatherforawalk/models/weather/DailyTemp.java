package io.github.terickson87.isgoodweatherforawalk.models.weather;

public class DailyTemp extends DailyTempFeelsLike {
    double mMin;
    double mMax;

    public DailyTemp() { }

    public DailyTemp(double day, double min, double max, double night, double evening, double morning) {
        super(day, night, evening, morning);
        mMin = min;
        mMax = max;
    }

    public double getMin() {
        return mMin;
    }

    public void setMin(double min) {
        mMin = min;
    }

    public double getMax() {
        return mMax;
    }

    public void setMax(double max) {
        mMax = max;
    }
}
