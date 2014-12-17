package com.jkao.orbitzweather;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Joanne on 12/16/2014.
 */
public class ForecastListActivity  extends SingleFragmentActivity {

    private static ArrayList<Forecast> sFutureForecast;

    @Override
    protected Fragment createFragment() {
        return new ForecastListFragment();
    }

    public static ArrayList<Forecast> get(Context c) {
        if (sFutureForecast == null) {
            sFutureForecast = new ArrayList<Forecast>();
        }
        return sFutureForecast;
    }

    public static void setFutureForecast(ArrayList<Forecast> sFutureForecast) {
        ForecastListActivity.sFutureForecast = sFutureForecast;
    }
}
