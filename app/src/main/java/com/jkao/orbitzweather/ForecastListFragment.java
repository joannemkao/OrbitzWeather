package com.jkao.orbitzweather;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joanne on 12/16/2014.
 */
public class ForecastListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.future_forecast_title));
        ArrayList<Forecast> mForecastList = ForecastListActivity.get(getActivity());

        //Create adapter to manage forecast items
        ForecastAdapter adapter = new ForecastAdapter(mForecastList);
        setListAdapter(adapter);
    }

    //custom adapter for custom view of forecast list items
    private class ForecastAdapter extends ArrayAdapter<Forecast> {
        public ForecastAdapter(ArrayList<Forecast> forecastList) {
            // no pre-defined layout
            super(getActivity(), 0, forecastList);
        }

        // create and return custom list item where convertview is existing list item
        public View getView(int position, View convertView, ViewGroup parent) {
            //create if d.n.e
            if (convertView == null ) convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_forecast, null);

            // Configure view for array item
            Forecast forecastDay = getItem(position);

            ImageView weatherImage = (ImageView)convertView.findViewById(R.id.forecast_list_item_iconImageView);
            weatherImage.setImageBitmap(forecastDay.getIcon());

            TextView dateText = (TextView)convertView.findViewById(R.id.forecast_list_item_dateTextView);
            dateText.setText(forecastDay.getDate());

            TextView descText = (TextView)convertView.findViewById(R.id.forecast_list_item_descTextView);
            descText.setText(forecastDay.getDescription());

            TextView precipText = (TextView)convertView.findViewById(R.id.forecast_list_item_precipTextView);
            precipText.setText(getString(R.string.precipitation) + forecastDay.getPrecipitation());

            TextView tempText = (TextView)convertView.findViewById(R.id.forecast_list_item_tempTextView);
            tempText.setText(getString(R.string.temperature) + forecastDay.getTemperature());

            return convertView;
        }
    }
}

