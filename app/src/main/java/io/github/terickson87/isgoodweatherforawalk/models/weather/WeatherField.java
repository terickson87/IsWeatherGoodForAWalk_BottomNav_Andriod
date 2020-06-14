package io.github.terickson87.isgoodweatherforawalk.models.weather;

public class WeatherField {
    private String mDescription;
    private String mMainDescriptionShort;
    private String mIconUrl;
    private static final String mc_ICON_URL_START = "https://openweathermap.org/img/wn/";
    private static final String mc_ICON_URL_END = "@4x.png";

    public WeatherField() {
    }

    public WeatherField(String description, String mainDescriptionShort, String icon) {
        mDescription = description;
        mMainDescriptionShort = mainDescriptionShort;
        setIcon(icon);
    }

    void setIcon(String icon) {
        mIconUrl = mc_ICON_URL_START + icon + mc_ICON_URL_END;
    }

    void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    String getIconUrl() {
        return mIconUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        mDescription = mDescription;
    }

    public String getMainDescriptionShort() {
        return mMainDescriptionShort;
    }

    public void setMainDescriptionShort(String mMainDescriptionShort) {
        mMainDescriptionShort = mMainDescriptionShort;
    }
}
