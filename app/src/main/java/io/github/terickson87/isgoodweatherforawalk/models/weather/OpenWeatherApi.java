package io.github.terickson87.isgoodweatherforawalk.models.weather;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * OpenWeatherApi get the weather data from a location from the openweathermap.org onecall API.
 *
 * https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={part}&units={units}&appid={YOUR API KEY}
 * lat, lon - geographical coordinates (latitude, longitude)
 * YOUR API KEY -  unique API key (you can always find it on the account page, on the "API key" tab)
 * part - (optional parameter) by using this parameter you can exclude some parts of weather data from the API response.
 * The value parts should be a comma-delimited list (without spaces). Available values:
 * current, minutely, hourly, daily
 */
public class OpenWeatherApi {

    // Static Props
    private static final String TAG = "OpenWeatherApi";
    private static final String mc_API_KEY =  "84f0c5edd23b278b44921947bfe13289";
    private static final String mc_ONE_CALL_API_URL = "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String mc_TAG_API = "appid=";
    private static final String mc_TAG_LATITUDE = "lat=";
    private static final String mc_TAG_LONGITUDE = "lon=";
    private static final String mc_TAG_UNITS = "units=";
    private static final String mc_UNITS_IMPERIAL = "imperial"; // Fahrenheit,
    private static final String mc_UNITS_METRIC = "metric"; // Celsius,

    // Constructor props
    private Context mContext; // Use getContext() or getActivity().getApplicationContext()
    private Double mLatitude;
    private Double mLongitude;
    private Instant mCurrentDate;
    private long mCurrentUnixUtcTime;

    // Url props
    private String mApiUrl = mc_ONE_CALL_API_URL;
    private int mNumberOfApiParams = 0;

    // Response JSON props
    private JSONObject mResponseJson;
    private JSONObject mCurrentJson;
    private JSONArray mMinutelyJson;
    private JSONArray mHourlyJson;
    private JSONArray mDailyJson;

    // Response Data props
    private String mTimezoneString;
    private Integer mTimezoneOffset_s; // Shift in seconds from UTC
    private CurrentWeatherData mCurrentWeatherData;
    private List<MinutelyWeatherData> mMinutelyWeatherData;
    private List<HourlyWeatherData> mHourlyWeatherData;
    private List<DailyWeatherData> mDailyWeatherData;
    private WeatherData mWeatherData;

    // Getters
    public CurrentWeatherData getCurrentWeatherData() {
        return mCurrentWeatherData;
    }

    public List<MinutelyWeatherData> getMinutelyWeatherData() {
        return mMinutelyWeatherData;
    }

    public List<HourlyWeatherData> getHourlyWeatherData() {
        return mHourlyWeatherData;
    }

    public List<DailyWeatherData> getDailyWeatherData() {
        return mDailyWeatherData;
    }

    public WeatherData getWeatherData() {
        return mWeatherData;
    }

    public String getTimezoneString() {
        return mTimezoneString;
    }

    public Integer getTimezoneOffset_s() {
        return mTimezoneOffset_s;
    }

    public Instant getCurrentDateTime() {
        return mCurrentDate;
    }

    // Constructor
    public OpenWeatherApi(Context context, double latitude, double longitude) {
        mContext = context;
        mLatitude = latitude;
        mLongitude = longitude;
        mCurrentDate = Instant.now();
        mCurrentUnixUtcTime = Calendar.getInstance().getTimeInMillis();
    }

    // Public Methods
    public void callApi(final ApiCallback apiCallback) {

        // Build Url
        addParamToUrl(mc_TAG_LATITUDE + mLatitude.toString());
        addParamToUrl(mc_TAG_LONGITUDE + mLongitude.toString());
        addParamToUrl(mc_TAG_UNITS + mc_UNITS_IMPERIAL);
        addParamToUrl(mc_TAG_API + mc_API_KEY);
        Log.i(TAG, " - callApi Called. ApiUrl = \"" + mApiUrl + "\"");

        // Build request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, mApiUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        apiCallback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "callApi: There was an error with Volley's response.");
                    }
                });

        // Add to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjectRequest);
    }

    private void addParamToUrl(String param) {
        if (mNumberOfApiParams < 1) {
            mApiUrl += param;
        } else {
            mApiUrl += "&" + param;
        }
        mNumberOfApiParams++;
    }

    private void parseResponseJson() {
        Log.i(TAG, " - parseResponseJson Called.");
        String currentTag = "current";
        String minutelyTag = "minutely";
        String hourlyTag = "hourly";
        String dailyTag = "daily";

        try {
            mTimezoneString = mResponseJson.getString("timezone");
            mTimezoneOffset_s = mResponseJson.getInt("timezone_offset");
            mCurrentJson = mResponseJson.getJSONObject(currentTag);
            mMinutelyJson = mResponseJson.getJSONArray(minutelyTag);
            mHourlyJson = mResponseJson.getJSONArray(hourlyTag);
            mDailyJson = mResponseJson.getJSONArray(dailyTag);
            mTimezoneString = mResponseJson.getString("timezone");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseResponse(JSONObject response) {
        Log.i(TAG, " - parseResponse Called.");
        mResponseJson = response;
        parseResponseJson();
        parseCurrent();
        parseMinutely();
        parseHourly();
        parseDaily();
        createWeatherData();
    }

    public MutableLiveData<WeatherData> getWeatherDataLiveData() {
        MutableLiveData<WeatherData> weatherDataLiveData = new MutableLiveData<>();


        return  weatherDataLiveData;
    }

    private WeatherField parseWeatherFieldJson(JSONObject parentObjOfWeatherJsonArr) {
        Log.i(TAG, " - parseWeatherJson Called.");
        WeatherField weatherField = new WeatherField();
        JSONArray weatherJsonArr;
        try {
            weatherJsonArr = parentObjOfWeatherJsonArr.getJSONArray("weather");
            JSONObject weatherJsonObj = weatherJsonArr.getJSONObject(0);
            weatherField.setDescription(weatherJsonObj.getString("description"));
            weatherField.setMainDescriptionShort(weatherJsonObj.getString("main"));
            weatherField.setIcon(weatherJsonObj.getString("icon"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherField;
    }

    private void parseCurrent() {
        Log.i(TAG, " - ParseCurrent Called.");
        mCurrentWeatherData = new CurrentWeatherData();
        try {
            mCurrentWeatherData.setTime(Instant.ofEpochSecond(mCurrentJson.getLong("dt")));
            mCurrentWeatherData.setSunriseTime(Instant.ofEpochSecond(mCurrentJson.getLong("sunrise")));
            mCurrentWeatherData.setSunsetTime(Instant.ofEpochSecond(mCurrentJson.getLong("sunset")));
            mCurrentWeatherData.setTemperature(mCurrentJson.getDouble("temp"));
            mCurrentWeatherData.setTemperatureFeelsLike(mCurrentJson.getDouble("feels_like"));
            mCurrentWeatherData.setPercentCloudy(mCurrentJson.getInt("clouds"));
            mCurrentWeatherData.setWeatherField(parseWeatherFieldJson(mCurrentJson));
            mCurrentWeatherData.setWindSpeed(mCurrentJson.getDouble("wind_speed"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set optional fields separately to avoid JSONException triggering the catch statement
        double windGust = 0.0;
        try {
            windGust += mCurrentJson.getDouble("wind_gust");
        } catch (JSONException ignored) {}
        mCurrentWeatherData.setWindGust(windGust);

        double precipitationVolume = 0.0;
        try {
            JSONObject rain = mCurrentJson.getJSONObject("rain");
            precipitationVolume += rain.getDouble("1hr");
        } catch (JSONException ignored) {}
        mCurrentWeatherData.setPrecipitationVolume(precipitationVolume);

        double snowVolume = 0.0;
        try {
            JSONObject snow = mCurrentJson.getJSONObject("snow");
            snowVolume += snow.getDouble("1hr");
        } catch (JSONException ignored) {}
        mCurrentWeatherData.setPrecipitationVolume(mCurrentWeatherData.getPrecipitationVolume() + snowVolume);
        mCurrentWeatherData.setSnowVolume(snowVolume);
    }

    private void parseMinutely() {
        Log.i(TAG, " - parseMinutely Called.");
        mMinutelyWeatherData = new ArrayList<>();
        try {
            for (int iMinute = 0; iMinute < mMinutelyJson.length(); iMinute++) {
                JSONObject minuteJson = mMinutelyJson.getJSONObject(iMinute);
                MinutelyWeatherData minutelyWeatherData = new MinutelyWeatherData();

                minutelyWeatherData.setTime(Instant.ofEpochSecond(minuteJson.getLong("dt")));
                minutelyWeatherData.setPrecipitationVolume(minuteJson.getDouble("precipitation"));

                mMinutelyWeatherData.add(minutelyWeatherData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseHourly() {
        Log.i(TAG, " - parseHourly Called.");
        mHourlyWeatherData = new ArrayList<>();
        try {
            for (int iHour = 0; iHour < mHourlyJson.length(); iHour++) {
                JSONObject hourJson = mHourlyJson.getJSONObject(iHour);
                HourlyWeatherData hourlyWeatherData = new HourlyWeatherData();

                hourlyWeatherData.setTime(Instant.ofEpochSecond(hourJson.getLong("dt")));
                hourlyWeatherData.setTemperature(hourJson.getDouble("temp"));
                hourlyWeatherData.setTemperatureFeelsLike(hourJson.getDouble("feels_like"));
                hourlyWeatherData.setPercentCloudy(hourJson.getInt("clouds"));
                hourlyWeatherData.setWindSpeed(hourJson.getDouble("wind_speed"));
                hourlyWeatherData.setWeatherField(parseWeatherFieldJson(hourJson));

                // Set optional fields separately to avoid JSONException triggering the catch statement
                double windGust = 0.0;
                try {
                    windGust += hourJson.getDouble("wind_gust");
                } catch (JSONException ignored) {}
                hourlyWeatherData.setWindGust(windGust);

                double precipitationVolume = 0.0;
                try {
                    JSONObject rain = hourJson.getJSONObject("rain");
                    precipitationVolume += rain.getDouble("1hr");
                } catch (JSONException ignored) {}
                hourlyWeatherData.setPrecipitationVolume(precipitationVolume);

                double snowVolume = 0.0;
                try {
                    JSONObject snow = hourJson.getJSONObject("snow");
                    snowVolume += snow.getDouble("1hr");
                } catch (JSONException ignored) {}
                hourlyWeatherData.setPrecipitationVolume(hourlyWeatherData.getPrecipitationVolume() + snowVolume);
                hourlyWeatherData.setSnowVolume(snowVolume);

                mHourlyWeatherData.add(hourlyWeatherData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseDaily() {
        Log.i(TAG, " - parseDaily Called.");
        mDailyWeatherData = new ArrayList<>();
        for (int iDay = 0; iDay < mDailyJson.length(); iDay++) {
            DailyWeatherData dailyWeatherData = new DailyWeatherData();
            try {
                JSONObject dayJson = mDailyJson.getJSONObject(iDay);
                dailyWeatherData.setTime(Instant.ofEpochSecond(dayJson.getLong("dt")));
                dailyWeatherData.setSunriseTime(Instant.ofEpochSecond(dayJson.getLong("sunrise")));
                dailyWeatherData.setSunriseTime(Instant.ofEpochSecond(dayJson.getLong("sunset")));
                dailyWeatherData.setDailyTemp(parseDailyTemp(dayJson));
                dailyWeatherData.setDailyTempFeelsLike(parseDailyTempFeelsLike(dayJson));
                dailyWeatherData.setWindSpeed(dayJson.getDouble("wind_speed"));
                dailyWeatherData.setWeatherField(parseWeatherFieldJson(dayJson));
                dailyWeatherData.setPercentCloudy(dayJson.getInt("clouds"));

                double windGust = 0.0;
                try {
                    windGust += dayJson.getDouble("wind_gust");
                } catch (JSONException ignored) {}
                dailyWeatherData.setWindGust(windGust);

                double precipitationVolume = 0.0;
                try {
                    precipitationVolume = dayJson.getDouble("rain");
                } catch(JSONException ignored) {}
                dailyWeatherData.setPrecipitationVolume(precipitationVolume);

                double snowVolume = 0.0;
                try {
                    snowVolume = dayJson.getDouble("snow");
                } catch(JSONException ignored) {}
                dailyWeatherData.setPrecipitationVolume(dailyWeatherData.getPrecipitationVolume() + snowVolume);
                dailyWeatherData.setSnowVolume(snowVolume);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mDailyWeatherData.add(dailyWeatherData);
            }

    }

    private DailyTemp parseDailyTemp(JSONObject parentObjOfDailyTempJsonObj) {
        DailyTemp dailyTemp = new DailyTemp();
        try {
            JSONObject dailyTempJsonObj = parentObjOfDailyTempJsonObj.getJSONObject("temp");
            dailyTemp.setDay(dailyTempJsonObj.getDouble("day"));
            dailyTemp.setNight(dailyTempJsonObj.getDouble("night"));
            dailyTemp.setEvening(dailyTempJsonObj.getDouble("eve"));
            dailyTemp.setMorning(dailyTempJsonObj.getDouble("morn"));
            dailyTemp.setMax(dailyTempJsonObj.getDouble("min"));
            dailyTemp.setMin(dailyTempJsonObj.getDouble("max"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dailyTemp;
    }

    private DailyTempFeelsLike parseDailyTempFeelsLike(JSONObject parentObjOfDailyTempFeelsLikeJsonObj) {
        DailyTempFeelsLike dailyTempFeelsLike = new DailyTempFeelsLike();
        try {
            JSONObject dailyTempFeelsLikeJsonObj = parentObjOfDailyTempFeelsLikeJsonObj.getJSONObject("feels_like");
            dailyTempFeelsLike.setDay(dailyTempFeelsLikeJsonObj.getDouble("day"));
            dailyTempFeelsLike.setNight(dailyTempFeelsLikeJsonObj.getDouble("night"));
            dailyTempFeelsLike.setEvening(dailyTempFeelsLikeJsonObj.getDouble("eve"));
            dailyTempFeelsLike.setMorning(dailyTempFeelsLikeJsonObj.getDouble("morn"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dailyTempFeelsLike;
    }

    private void createWeatherData() {
        mWeatherData = new WeatherData();
        mWeatherData.setCurrentWeatherData(mCurrentWeatherData);
        mWeatherData.setMinutelyWeatherData(mMinutelyWeatherData);
        mWeatherData.setHourlyWeatherData(mHourlyWeatherData);
        mWeatherData.setDailyWeatherData(mDailyWeatherData);

    }

    private Date unixSecondsToDate(long unixSeconds) {
        return new Date(unixSeconds*1000L);
    }

    public Instant getNextRainTime() {
        Instant nextRain = getNextRainTimeMinute();
        if (nextRain != null) {
            return nextRain;
        }

        nextRain = getNextRainTimeHour();
        if (nextRain != null) {
            return nextRain;
        }

        nextRain = getNextRainTimeDay();
        return nextRain;
    }

    private Instant getNextRainTimeMinute() {
        for (MinutelyWeatherData minutelyWeatherData : mMinutelyWeatherData) {
            if (minutelyWeatherData.getPrecipitationVolume() > 0) {
                return minutelyWeatherData.getTime();
            }
        }
        return null;
    }

    private Instant getNextRainTimeHour() {
        for (HourlyWeatherData hourlyWeatherData : mHourlyWeatherData) {
            if (hourlyWeatherData.getPrecipitationVolume() > 0) {
                return hourlyWeatherData.getTime();
            }
        }
        return null;
    }

    private Instant getNextRainTimeDay() {
        for (DailyWeatherData dailyWeatherData : mDailyWeatherData) {
            if (dailyWeatherData.getPrecipitationVolume() > 0) {
                return dailyWeatherData.getTime();
            }
        }
        return null;
    }
}
