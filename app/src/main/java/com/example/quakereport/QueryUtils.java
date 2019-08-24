package com.example.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
     * Helper methods related to requesting and receiving earthquake data from USGS.
     */
    public final class QueryUtils {

        /** Sample JSON response for a USGS query */
        public static final String LOG_TAG = QueryUtils.class.getSimpleName();

        /**
         * Create a private constructor because no one should ever create a {@link QueryUtils} object.
         * This class is only meant to hold static variables and methods, which can be accessed
         * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
         */
        private QueryUtils() {
        }

        /**
         * Return a list of {@link Earthquake} objects that has been built up from
         * parsing a JSON response.
         */
        public static ArrayList<Earthquake> extractEarthquakes(String stringUrl) {
            Log.i(LOG_TAG,"TEST: QueryUtils function extractEarthQuake() called...");
            //Create a thread to sleep the system for 2000 milliseconds
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Create URL
            URL url = createUrl(stringUrl);
            //Perform HTTP Request
            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Extract relevant fields from the jsonResponse gotten from the http request
            ArrayList<Earthquake>earthquakes = fetchEarthQuakeData(jsonResponse);
            return earthquakes;
        }

        private static ArrayList<Earthquake> fetchEarthQuakeData(String jsonResponse) {
            if(TextUtils.isEmpty(jsonResponse)){
                return null;
            }

            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {
                // Create an empty ArrayList that we can start adding earthquakes to
                ArrayList<Earthquake> earthquakes = new ArrayList<>();

                JSONObject root = new JSONObject(jsonResponse);
                JSONArray rootFeatures = root.optJSONArray("features");
                for(int i = 0;i<rootFeatures.length();i++){
                    JSONObject feature = rootFeatures.getJSONObject(i);
                    JSONObject properties = feature.getJSONObject("properties");

                    //Extraction begins
                    Double magnitude = properties.getDouble("mag");
                    String location = properties.getString("place");
                    Long time = properties.getLong("time");
                    //Deal with the date
                    Date dateObject = new Date(time);
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
                    String dateToDisplay = dateFormatter.format(dateObject);

                    //Deal with the time
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mma");
                    String timeToDisplay = timeFormatter.format(dateObject);

                    //Handle the URL of the earthquake data
                    String url = properties.getString("url");
                    //Add the extracted data to a new earthQuake object
                    earthquakes.add(i,new Earthquake(magnitude,location,dateToDisplay,timeToDisplay,url));
                }
                return earthquakes;
                // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
                //@Done Hurray!!
                // build up a list of Earthquake objects with the corresponding data.

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }

        return null;
        }

        private static String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if(url==null){
                return jsonResponse;
            }
            HttpURLConnection urlConnection =null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromInputStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
                }
            }catch (IOException e){
                Log.e(LOG_TAG,"Problem retrieving Eathquake JSON data.",e);
            }finally {
                if(urlConnection!=null) {
                    urlConnection.disconnect();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private static String readFromInputStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader realReader = new BufferedReader(inputStreamReader);
            String reader =realReader.readLine();
            while (reader!=null){
                output.append(reader);
                reader = realReader.readLine();
            }
            }
            return output.toString();
        }

        private static URL createUrl(String stringUrl){
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }

    }

