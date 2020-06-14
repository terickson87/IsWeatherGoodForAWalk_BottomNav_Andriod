package io.github.terickson87.isgoodweatherforawalk.models.weather;

import org.json.JSONObject;


/**
 * The ApiCallback interface is a generic callback interface intended to be used with REST Apis that return a JSONObject response.
 */
public interface ApiCallback {
    void onSuccess(JSONObject response);
}
