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

    private static final String sfTAG = "OpenWeatherApi";

    // Api Props
    private static final String msfAPI_KEY =  "***REMOVED***";
    private static final String msfONE_CALL_API_URL = "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String msfTAG_API = "appid=";
    private static final String msfTAG_LATITUDE = "lat=";
    private static final String msfTAG_LONGITUDE = "lon=";
    private static final String msfTAG_UNITS = "units=";
    private static final String msfUNITS_IMPERIAL = "imperial"; // Fahrenheit,
    private static final String msfUNITS_METRIC = "metric"; // Celsius,

    // JSON fields
    private static final String msfFIELD_CURRENT = "current";
    private static final String msfFIELD_MINUTELY = "minutely";
    private static final String msfFIELD_HOURLY = "hourly";
    private static final String msfFIELD_DAILY = "daily";
    private static final String msfFIELD_TIMEZONE = "timezone";
    private static final String msfFIELD_TIMEZONE_OFFSET = "timezone_offset";
    private static final String msfFIELD_WEATHER = "weather";
    private static final String msfFIELD_DESCRIPTION = "description";
    private static final String msfFIELD_MAIN = "main";
    private static final String msfFIELD_ICON = "icon";
    private static final String msfFIELD_DT = "dt";
    private static final String msfFIELD_SUNRISE = "sunrise";
    private static final String msfFIELD_SUNSET = "sunset";
    private static final String msfFIELD_TEMP = "temp";
    private static final String msfFIELD_FEELS_LIKE = "feels_like";
    private static final String msfFIELD_CLOUDS = "clouds";
    private static final String msfFIELD_WIND_SPEED = "wind_speed";
    private static final String msfFIELD_WIND_GUST = "wind_gust";
    private static final String msfFIELD_RAIN = "rain";
    private static final String msfFIELD_SNOW = "snow";
    private static final String msfFIELD_1HR = "1hr";
    private static final String msfFIELD_PRECIPITATION = "precipitation";
    private static final String msfFIELD_DAY = "day";
    private static final String msfFIELD_NIGHT = "night";
    private static final String msfFIELD_EVE = "eve";
    private static final String msfFIELD_MORN = "morn";
    private static final String msfFIELD_MIN = "min";
    private static final String msfFIELD_MAX = "max";


    // Constructor props
    private Context mContext; // Use getContext() or getActivity().getApplicationContext()
    private Double mLatitude;
    private Double mLongitude;
    private Instant mCurrentDate;
    private long mCurrentUnixUtcTime;

    // Url props
    private String mApiUrl = msfONE_CALL_API_URL;
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
        addParamToUrl(msfTAG_LATITUDE + mLatitude.toString());
        addParamToUrl(msfTAG_LONGITUDE + mLongitude.toString());
        addParamToUrl(msfTAG_UNITS + msfUNITS_IMPERIAL);
        addParamToUrl(msfTAG_API + msfAPI_KEY);
        Log.i(sfTAG, " - callApi Called. ApiUrl = \"" + mApiUrl + "\"");

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
                        Log.e(sfTAG, "callApi: There was an error with Volley's response.");
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
        Log.i(sfTAG, " - parseResponseJson Called.");

        try {
            mTimezoneString = mResponseJson.getString(msfFIELD_TIMEZONE);
            mTimezoneOffset_s = mResponseJson.getInt(msfFIELD_TIMEZONE_OFFSET);
            mCurrentJson = mResponseJson.getJSONObject(msfFIELD_CURRENT);
            mMinutelyJson = mResponseJson.getJSONArray(msfFIELD_MINUTELY);
            mHourlyJson = mResponseJson.getJSONArray(msfFIELD_HOURLY);
            mDailyJson = mResponseJson.getJSONArray(msfFIELD_DAILY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseResponse(JSONObject response) {
        Log.i(sfTAG, " - parseResponse Called.");
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
        Log.i(sfTAG, " - parseWeatherJson Called.");
        WeatherField weatherField = new WeatherField();
        JSONArray weatherJsonArr;
        try {
            weatherJsonArr = parentObjOfWeatherJsonArr.getJSONArray(msfFIELD_WEATHER);
            JSONObject weatherJsonObj = weatherJsonArr.getJSONObject(0);
            weatherField.setDescription(weatherJsonObj.getString(msfFIELD_DESCRIPTION));
            weatherField.setMainDescriptionShort(weatherJsonObj.getString(msfFIELD_MAIN));
            weatherField.setIcon(weatherJsonObj.getString(msfFIELD_ICON));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherField;
    }

    private void parseCurrent() {
        Log.i(sfTAG, " - ParseCurrent Called.");
        mCurrentWeatherData = new CurrentWeatherData();
        try {
            mCurrentWeatherData.setTime(Instant.ofEpochSecond(mCurrentJson.getLong(msfFIELD_DT)));
            mCurrentWeatherData.setSunriseTime(Instant.ofEpochSecond(mCurrentJson.getLong(msfFIELD_SUNRISE)));
            mCurrentWeatherData.setSunsetTime(Instant.ofEpochSecond(mCurrentJson.getLong(msfFIELD_SUNSET)));
            mCurrentWeatherData.setTemperature(mCurrentJson.getDouble(msfFIELD_TEMP));
            mCurrentWeatherData.setTemperatureFeelsLike(mCurrentJson.getDouble(msfFIELD_FEELS_LIKE));
            mCurrentWeatherData.setPercentCloudy(mCurrentJson.getInt(msfFIELD_CLOUDS));
            mCurrentWeatherData.setWeatherField(parseWeatherFieldJson(mCurrentJson));
            mCurrentWeatherData.setWindSpeed(mCurrentJson.getDouble(msfFIELD_WIND_SPEED));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set optional fields separately to avoid JSONException triggering the catch statement
        double windGust = 0.0;
        try {
            windGust += mCurrentJson.getDouble(msfFIELD_WIND_GUST);
        } catch (JSONException ignored) {}
        mCurrentWeatherData.setWindGust(windGust);

        double precipitationVolume = 0.0;
        try {
            JSONObject rain = mCurrentJson.getJSONObject(msfFIELD_RAIN);
            precipitationVolume += rain.getDouble(msfFIELD_1HR);
        } catch (JSONException ignored) {}
        mCurrentWeatherData.setPrecipitationVolume(precipitationVolume);

        double snowVolume = 0.0;
        try {
            JSONObject snow = mCurrentJson.getJSONObject(msfFIELD_SNOW);
            snowVolume += snow.getDouble(msfFIELD_1HR);
        } catch (JSONException ignored) {}
        mCurrentWeatherData.setPrecipitationVolume(mCurrentWeatherData.getPrecipitationVolume() + snowVolume);
        mCurrentWeatherData.setSnowVolume(snowVolume);
    }

    private void parseMinutely() {
        Log.i(sfTAG, " - parseMinutely Called.");
        mMinutelyWeatherData = new ArrayList<>();
        try {
            for (int iMinute = 0; iMinute < mMinutelyJson.length(); iMinute++) {
                JSONObject minuteJson = mMinutelyJson.getJSONObject(iMinute);
                MinutelyWeatherData minutelyWeatherData = new MinutelyWeatherData();

                minutelyWeatherData.setTime(Instant.ofEpochSecond(minuteJson.getLong(msfFIELD_DT)));
                minutelyWeatherData.setPrecipitationVolume(minuteJson.getDouble(msfFIELD_PRECIPITATION));

                mMinutelyWeatherData.add(minutelyWeatherData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseHourly() {
        Log.i(sfTAG, " - parseHourly Called.");
        mHourlyWeatherData = new ArrayList<>();
        try {
            for (int iHour = 0; iHour < mHourlyJson.length(); iHour++) {
                JSONObject hourJson = mHourlyJson.getJSONObject(iHour);
                HourlyWeatherData hourlyWeatherData = new HourlyWeatherData();

                hourlyWeatherData.setTime(Instant.ofEpochSecond(hourJson.getLong(msfFIELD_DT)));
                hourlyWeatherData.setTemperature(hourJson.getDouble(msfFIELD_TEMP));
                hourlyWeatherData.setTemperatureFeelsLike(hourJson.getDouble(msfFIELD_FEELS_LIKE));
                hourlyWeatherData.setPercentCloudy(hourJson.getInt(msfFIELD_CLOUDS));
                hourlyWeatherData.setWindSpeed(hourJson.getDouble(msfFIELD_WIND_SPEED));
                hourlyWeatherData.setWeatherField(parseWeatherFieldJson(hourJson));

                // Set optional fields separately to avoid JSONException triggering the catch statement
                double windGust = 0.0;
                try {
                    windGust += hourJson.getDouble(msfFIELD_WIND_GUST);
                } catch (JSONException ignored) {}
                hourlyWeatherData.setWindGust(windGust);

                double precipitationVolume = 0.0;
                try {
                    JSONObject rain = hourJson.getJSONObject(msfFIELD_RAIN);
                    precipitationVolume += rain.getDouble(msfFIELD_1HR);
                } catch (JSONException ignored) {}
                hourlyWeatherData.setPrecipitationVolume(precipitationVolume);

                double snowVolume = 0.0;
                try {
                    JSONObject snow = hourJson.getJSONObject(msfFIELD_SNOW);
                    snowVolume += snow.getDouble(msfFIELD_1HR);
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
        Log.i(sfTAG, " - parseDaily Called.");
        mDailyWeatherData = new ArrayList<>();
        for (int iDay = 0; iDay < mDailyJson.length(); iDay++) {
            DailyWeatherData dailyWeatherData = new DailyWeatherData();
            try {
                JSONObject dayJson = mDailyJson.getJSONObject(iDay);
                dailyWeatherData.setTime(Instant.ofEpochSecond(dayJson.getLong(msfFIELD_DT)));
                dailyWeatherData.setSunriseTime(Instant.ofEpochSecond(dayJson.getLong(msfFIELD_SUNRISE)));
                dailyWeatherData.setSunriseTime(Instant.ofEpochSecond(dayJson.getLong(msfFIELD_SUNSET)));
                dailyWeatherData.setDailyTemp(parseDailyTemp(dayJson));
                dailyWeatherData.setDailyTempFeelsLike(parseDailyTempFeelsLike(dayJson));
                dailyWeatherData.setWindSpeed(dayJson.getDouble(msfFIELD_WIND_SPEED));
                dailyWeatherData.setWeatherField(parseWeatherFieldJson(dayJson));
                dailyWeatherData.setPercentCloudy(dayJson.getInt(msfFIELD_CLOUDS));

                double windGust = 0.0;
                try {
                    windGust += dayJson.getDouble(msfFIELD_WIND_GUST);
                } catch (JSONException ignored) {}
                dailyWeatherData.setWindGust(windGust);

                double precipitationVolume = 0.0;
                try {
                    precipitationVolume = dayJson.getDouble(msfFIELD_RAIN);
                } catch(JSONException ignored) {}
                dailyWeatherData.setPrecipitationVolume(precipitationVolume);

                double snowVolume = 0.0;
                try {
                    snowVolume = dayJson.getDouble(msfFIELD_SNOW);
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
            JSONObject dailyTempJsonObj = parentObjOfDailyTempJsonObj.getJSONObject(msfFIELD_TEMP);
            dailyTemp.setDay(dailyTempJsonObj.getDouble(msfFIELD_DAY));
            dailyTemp.setNight(dailyTempJsonObj.getDouble(msfFIELD_NIGHT));
            dailyTemp.setEvening(dailyTempJsonObj.getDouble(msfFIELD_EVE));
            dailyTemp.setMorning(dailyTempJsonObj.getDouble(msfFIELD_MORN));
            dailyTemp.setMax(dailyTempJsonObj.getDouble(msfFIELD_MIN));
            dailyTemp.setMin(dailyTempJsonObj.getDouble(msfFIELD_MAX));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dailyTemp;
    }

    private DailyTempFeelsLike parseDailyTempFeelsLike(JSONObject parentObjOfDailyTempFeelsLikeJsonObj) {
        DailyTempFeelsLike dailyTempFeelsLike = new DailyTempFeelsLike();
        try {
            JSONObject dailyTempFeelsLikeJsonObj = parentObjOfDailyTempFeelsLikeJsonObj.getJSONObject(msfFIELD_FEELS_LIKE);
            dailyTempFeelsLike.setDay(dailyTempFeelsLikeJsonObj.getDouble(msfFIELD_DAY));
            dailyTempFeelsLike.setNight(dailyTempFeelsLikeJsonObj.getDouble(msfFIELD_NIGHT));
            dailyTempFeelsLike.setEvening(dailyTempFeelsLikeJsonObj.getDouble(msfFIELD_EVE));
            dailyTempFeelsLike.setMorning(dailyTempFeelsLikeJsonObj.getDouble(msfFIELD_MORN));

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
