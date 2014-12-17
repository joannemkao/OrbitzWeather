package com.jkao.orbitzweather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Joanne on 12/15/2014.
 * 1. Cleanses user input
 * 2. Fetches weather data from World Weather Online
 * 3. Parses JSON return and saves into meaningful forecastSummary object
 */
public class ForecastFetcher {
    public static final String SAVED_SEARCH = "locationSearch";
    public static final String TAG = "ForecastFetcher";

    private static final String API_KEY = "95e364697303565374b533cfbd7a3";
    private static final String API_ENDPOINT = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=%s&q=%s&num_of_days=%s&tp=%s&format=json";
    private static final int TIMEOUT_SECONDS = 15;
    private static final int NUM_DAYS = 5;
    private static final int TIME_INTERVAL = 24; //set to daily average

    public ForecastSummary getForecastData(String searchText) {

        String location = cleanSearchText(searchText);
        ForecastSummary f = new ForecastSummary();
        f.setLocation(location);

        HttpURLConnection connection = null;
        BufferedReader bReader = null;
        InputStreamReader iReader = null;
        String jsonString = "";

        try {
            URL url = new URL(String.format(API_ENDPOINT, API_KEY, location, NUM_DAYS, TIME_INTERVAL));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT_SECONDS * 1000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG,"Unable to connect to URL " + url.toString());
                return null;
            }

            Log.e(TAG,"Successfully connected to URL " + url.toString());

            // get output from request
            iReader = new InputStreamReader(connection.getInputStream());
            bReader = new BufferedReader(iReader);

            String line;
            while ((line = bReader.readLine()) != null) {
                jsonString += line;
            }
            iReader.close();
            bReader.close();
            connection.disconnect();
            Log.e(TAG,"Fetched data successfully: " + jsonString);
            parseData(f, jsonString);
            return f;

        } catch (IOException e) { Log.e(TAG, "Failed to fetch data", e);
        } catch (JSONException e) { Log.e(TAG, "JSON parsing exception", e);
        } finally {
            try { if (iReader != null) iReader.close(); } catch (Exception e) { Log.e(TAG, "Failed to close input stream reader", e);}
            try { if (bReader != null) bReader.close(); } catch (Exception e) { Log.e(TAG, "Failed to close buffered reader", e); }
            try { if (connection != null) connection.disconnect(); } catch (Exception e) { Log.e(TAG, "Failed to close connection", e); }
        }
        return null;
    }

    void parseData(ForecastSummary f, String jsonString) throws JSONException {
        JSONObject reader = new JSONObject(jsonString);
        JSONObject jsonData = reader.getJSONObject("data");

        // If the city is not found, exit
        if (jsonData.has("error")) {
           Log.e(TAG, "Could not resolve location.");
            return;
        }

        // Assuming there is only one element in the current condition array (or accounting for only first element)
        JSONObject snapshotObject = jsonData.getJSONArray("current_condition").getJSONObject(0);
        f.setTemperature(snapshotObject.getString("temp_F"));
        f.setPrecipitation(snapshotObject.getString("precipMM"));

        // Assumed there is only one weather icon (or accounting for only first element)
        JSONObject weatherIconObj = snapshotObject.getJSONArray("weatherIconUrl").getJSONObject(0);
        f.setIcon(convertUrlImage(weatherIconObj.getString("value")));

        // Assumed there is only one description (or accounting for only first element)
        JSONObject weatherDescObj = snapshotObject.getJSONArray("weatherDesc").getJSONObject(0);
        f.setDescription(weatherDescObj.getString("value"));

        // Parse weather outlook into Forecast object. Forecast object is reused
        JSONArray upcomingDaysArray = jsonData.getJSONArray("weather");
        ArrayList<Forecast> futureForecast = new ArrayList<Forecast>();
        JSONObject currObj;
        Forecast dayForecast;
        JSONObject hourObj;
        for (int i = 0; i < upcomingDaysArray.length(); i++ ) {
            dayForecast = new Forecast();
            currObj = upcomingDaysArray.getJSONObject(i);
            dayForecast.setDate(currObj.getString("date"));

            //get first daily info and take attributes (see README.TXT for details - api v2 difference)
            hourObj = currObj.getJSONArray("hourly").getJSONObject(0);
            dayForecast.setDescription(hourObj.getJSONArray("weatherDesc").getJSONObject(0).getString("value"));
            dayForecast.setIcon(convertUrlImage(hourObj.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value")));
            dayForecast.setPrecipitation(hourObj.getString("precipMM"));
            dayForecast.setTemperature(hourObj.getString("tempF"));

            // Uncomment for logging/debugging purposes
            /*Log.e(TAG, "date: " + dayForecast.getDate());
            Log.e(TAG, "description: " + dayForecast.getDescription());
            Log.e(TAG, "icon: " + dayForecast.getIcon());
            Log.e(TAG, "precip: " + dayForecast.getPrecipitation());
            Log.e(TAG, "temp: "+ dayForecast.getTemperature());*/

            futureForecast.add(dayForecast);
        }
        // Set outlook
        f.setFutureForecast(futureForecast);
    }

    // resolves imageURL string to bitmap image
    public Bitmap convertUrlImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT_SECONDS * 1000);
            connection.connect();

            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);

        } catch (IOException e) { e.printStackTrace();}
        return null;
    }

    /*
     * cleans search data sent to HTTP as location parameters
     * Based on WWO documentation: http://www.worldweatheronline.com/api/docs/local-city-town-weather-api.aspx
     */
    public String cleanSearchText(String input) {

        // Will catch a thrown exception in parsing and log error
        if (input == null) return null;

        String newString = input.trim();
        newString = newString.replace(' ','+');
        return newString.replaceAll("[^a-zA-Z0-9.,+]","");
    }
}
