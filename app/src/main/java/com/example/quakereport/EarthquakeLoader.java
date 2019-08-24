package com.example.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.util.List;
import static com.example.quakereport.QueryUtils.LOG_TAG;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    //instantiate a private String to hold the URL
    private String mUrl;
    /*
    * Creates a new Loader
    * @param context of the activity
    * @param Url to fetch data from
    */

    public EarthquakeLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"TEST: onStartLoading() called...");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG,"TEST: loadInBackground() called...");
        if(mUrl==null){
            return null;
        }
        //Perform the http request in the Background
        return QueryUtils.extractEarthquakes(mUrl);
    }
}
