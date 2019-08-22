package com.example.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {
    private static final String USGS_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);


        EarthQuakeAsyncTask earthQuakeAsyncTask = new EarthQuakeAsyncTask();
        earthQuakeAsyncTask.execute(USGS_JSON_RESPONSE);

    }


    private class EarthQuakeAsyncTask extends AsyncTask<String,Void, List<Earthquake>>{

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            List<Earthquake> earthquakes = QueryUtils.extractEarthquakes(urls[0]);

            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            final EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(EarthquakeActivity.this,earthquakes);
            ListView earthQuakeListView = findViewById(R.id.listView);
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
    }
}
