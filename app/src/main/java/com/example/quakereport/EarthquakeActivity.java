package com.example.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import static com.example.quakereport.QueryUtils.LOG_TAG;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {
    private static final String USGS_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final int EARTHQUAKE_LOADER_ID = 2;
    private EarthquakeAdapter earthquakeAdapter;
    private TextView mEmptySetTextView;
    private View progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG,"TEST: Earthquake Activity, OnCreate called!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        ListView earthQuakeListView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        mEmptySetTextView = findViewById(R.id.empty_view);
        earthQuakeListView.setEmptyView(mEmptySetTextView);


        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()) {

            //Loader manager referenced and created in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            Log.i(LOG_TAG, "TEST: calling initloader()...");

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else{
            //Stop showing the indicator
            progressBar.setVisibility(View.GONE);
            mEmptySetTextView.setText(R.string.no_internet);
        }
        earthquakeAdapter = new EarthquakeAdapter(this,new ArrayList<Earthquake>());

        earthQuakeListView.setAdapter(earthquakeAdapter);

        earthQuakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake earthquake = earthquakeAdapter.getItem(i);
                Uri earthQuakeUri = Uri.parse(earthquake.getmUrl());
                Intent earthQuakeIntent = new Intent(Intent.ACTION_VIEW,earthQuakeUri);
                startActivity(earthQuakeIntent);
            }
        });
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG,"TEST: onCreateLoader() called...");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Minimum Magnitude
        String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));

        //order of Magnitudes Object
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_JSON_RESPONSE);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);
        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG,"TEST: onLoadFinished() called...");
        //Hide the progressBar because data has finished loading
        progressBar.setVisibility(View.GONE);
        //Check for internet connection
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo==null){
            mEmptySetTextView.setText(R.string.no_internet);
        }else if(networkInfo.isConnected()&&networkInfo!=null){
            //There is internet but still no earthquake found!!
            //When there is an Empty earthquake
            mEmptySetTextView.setText(R.string.no_earthquake);
        }
        //Clear the adapter of previous earthquake data
        earthquakeAdapter.clear();

        if(earthquakes!=null && !earthquakes.isEmpty()){
            earthquakeAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG,"TEST: onLoaderReset() called...");
        earthquakeAdapter.clear();
    }

}
