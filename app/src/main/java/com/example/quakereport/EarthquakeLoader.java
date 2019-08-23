package com.example.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

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
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl==null){
            return null;
        }
        //Perform the http request in the Background
        return QueryUtils.extractEarthquakes(mUrl);
    }
}
