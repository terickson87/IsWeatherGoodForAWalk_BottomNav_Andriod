package io.github.terickson87.isgoodweatherforawalk.models.location;

public interface LocationCallback {
    void onSuccess(Double latitude, Double longitude);
}
