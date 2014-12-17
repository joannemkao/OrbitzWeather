package com.jkao.orbitzweather;

import java.util.ArrayList;

/**
 * Created by Joanne on 12/16/2014.
 *
 * Model-level object extending from the Forecast class.
 * ForecastSummary objects have all the current weather conditions
 * as well as an array of Forecast objects for specific dates
 */
public class ForecastSummary extends Forecast {
    private ArrayList<Forecast> mFutureForecast;
    private String mLocation;

    public ForecastSummary() {
        super();
        mLocation = Forecast.NOT_AVAILABLE_MESSAGE;
        mFutureForecast = new ArrayList<Forecast>();
    }

    public String getLocation() { return mLocation; }
    public void setLocation(String mLocation) { this.mLocation = mLocation; }

    public ArrayList<Forecast> getFutureForecast() { return mFutureForecast; }
    public void setFutureForecast(ArrayList<Forecast> mFutureForecast) {this.mFutureForecast = mFutureForecast;}
}
