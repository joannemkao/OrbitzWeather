package com.jkao.orbitzweather;

import android.app.SearchManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CurrentForecastActivity extends SingleFragmentActivity {

    //Tag to receive search results and refresh accordingly
    private static final String TAG = "CurrentForecastActivity";

    @Override
    protected Fragment createFragment() {
        return new CurrentForecastFragment();
    }

    @Override
    public void onNewIntent(Intent intent) {
        CurrentForecastFragment f = (CurrentForecastFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);

        String searchText;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchText = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "Received new query: " + searchText );
        }
        else {
            //Default search is Chicago
            searchText = "Chicago";
        }

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString(ForecastFetcher.SAVED_SEARCH, searchText)
                .commit();

        f.refreshForecastSummary();
    }
}
