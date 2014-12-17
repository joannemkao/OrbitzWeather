package com.jkao.orbitzweather;

import android.graphics.Bitmap;

/**
 * Created by Joanne on 12/16/2014.
 */
public class Forecast {
    protected static final String NOT_AVAILABLE_MESSAGE = "Not Available";
    private Bitmap mIcon;
    private String mTemperature;
    private String mDescription;
    private String mPrecipitation;
    private String mDate;

    public Forecast() {
        mTemperature = NOT_AVAILABLE_MESSAGE;
        mDescription = NOT_AVAILABLE_MESSAGE;
        mPrecipitation = NOT_AVAILABLE_MESSAGE;
        mDate = NOT_AVAILABLE_MESSAGE;
    }

    public Bitmap getIcon() {
        return mIcon;
    }
    public void setIcon(Bitmap mIcon) {
        this.mIcon = mIcon;
    }

    public String getTemperature() {
        return mTemperature;
    }
    public void setTemperature(String mTemperature) {
        this.mTemperature = mTemperature;
    }

    public String getDescription() {
        return mDescription;
    }
    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPrecipitation() {
        return mPrecipitation;
    }
    public void setPrecipitation(String mPrecipitation) {
        this.mPrecipitation = mPrecipitation;
    }

    public String getDate() { return mDate; }
    public void setDate(String mDate) { this.mDate = mDate; }
}
