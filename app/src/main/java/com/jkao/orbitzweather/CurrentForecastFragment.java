package com.jkao.orbitzweather;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Joanne on 12/15/2014.
 */
public class CurrentForecastFragment extends Fragment {
    public static final String TAG = "CurrentForecastFragment";
    private ForecastSummary mForecastSummary;
    private ImageView mIconImage;
    private TextView mTemperatureText;
    private TextView mLocationText;
    private TextView mDescriptionText;
    private TextView mPrecipitationText;

    // configure fragment instance
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForecastSummary = new ForecastSummary();

        setRetainInstance(true);
        setHasOptionsMenu(true);
        refreshForecastSummary();
    }

    public void refreshForecastSummary() {
        new FetchForecastTask().execute();
    }

    // inflate layout for the fragment's view and return view to hosting activity (CurrentForecastActivity)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_forecast, parent, false);
        RelativeLayout mForecastSnapshot = (RelativeLayout)v.findViewById(R.id.forecast_snapshot);

        mForecastSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when clicked, open 5-day forecast
                ForecastListActivity.setFutureForecast(mForecastSummary.getFutureForecast());
                Intent i = new Intent(getActivity(), ForecastListActivity.class);
                startActivity(i);
            }
        });

        mIconImage = (ImageView)v.findViewById(R.id.icon);
        mTemperatureText = (TextView)v.findViewById(R.id.temperature);
        mLocationText = (TextView)v.findViewById(R.id.location);
        mDescriptionText = (TextView)v.findViewById(R.id.description);
        mPrecipitationText = (TextView)v.findViewById(R.id.precipitation);

        return v;
    }

    //for location search feature
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_current_forecast, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_location_search) {
            getActivity().onSearchRequested();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    private void updateFragmentFields() {
        //if no forecast, the Forecast constructor will provide default text
        if (mForecastSummary == null) mForecastSummary = new ForecastSummary();

        mIconImage.setImageBitmap(mForecastSummary.getIcon());

        if (mForecastSummary == null)
            mForecastSummary = new ForecastSummary();

        mTemperatureText.setText(getString(R.string.temperature) + " " + mForecastSummary.getTemperature());
        mLocationText.setText(getString(R.string.location) + " " + mForecastSummary.getLocation().replace('+',' '));
        mDescriptionText.setText(getString(R.string.description) + " " + mForecastSummary.getDescription());
        mPrecipitationText.setText(getString(R.string.precipitation) + " " + mForecastSummary.getPrecipitation());
    }

    // for background thread to pull weather forecast summary
    private class FetchForecastTask extends AsyncTask<Void,Void,ForecastSummary> {

        @Override
        protected ForecastSummary doInBackground(Void... params) {
            // retrieve the location the user searched for
            Activity activity = getActivity();
            if (activity != null) {
                String searchText = PreferenceManager
                        .getDefaultSharedPreferences(activity)
                        .getString(ForecastFetcher.SAVED_SEARCH, null);

                return new ForecastFetcher().getForecastData(searchText);
            }
            else {
                return new ForecastSummary();
            }
        }

        @Override
        protected void onPostExecute(ForecastSummary f) {
            mForecastSummary = f;
            updateFragmentFields();
        }

    }

}
