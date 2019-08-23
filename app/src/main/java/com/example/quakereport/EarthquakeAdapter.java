package com.example.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.text.DecimalFormat;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private DecimalFormat formatter = new DecimalFormat("0.0");

    public EarthquakeAdapter(Activity context, List<Earthquake>earthquake) {
        super(context, 0, earthquake);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String locationOffset;
        String primaryLocation;

        View listView = convertView;
        if(listView==null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }
        Earthquake currentEarthQuake = getItem(position);

        //First textView containing earthquake Magnitude
        String output = formatter.format(currentEarthQuake.getmEarthquakeMagnitude());
        TextView magnitude = listView.findViewById(R.id.txt_earthQuakeMagnitude);
        magnitude.setText(output);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthQuake.getmEarthquakeMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        //Second textView producing location of earthquake
        String location = currentEarthQuake.getmEarthQuakeLocation();
        if(location.contains(" of ")){
            locationOffset = location.substring(0,location.indexOf("of")+2);
            primaryLocation = location.substring(location.indexOf("of")+2);
        }else{
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = location;
        }
        //offset
        TextView locationOffsetContainer = listView.findViewById(R.id.txt_earthQuakeLocationOffset);
        locationOffsetContainer.setText(locationOffset);
        TextView locationPrimary = listView.findViewById(R.id.txt_earthQuakePrimaryLocation);
        locationPrimary.setText(primaryLocation);

        //Third textView containing the date of the earthquake
        TextView date = listView.findViewById(R.id.txt_earthQuakeDate);
        date.setText(currentEarthQuake.getmEarthQuakeDate());

        //Fourth textView containing the time of the earthQuake
        TextView time = listView.findViewById(R.id.txt_earthQuakeTime);
        time.setText(currentEarthQuake.getmEarthQuakeTime());

        //return the complete list of items
        return listView;
    }

    private int getMagnitudeColor(Double getmEarthquakeMagnitude) {
        int mColorResourceId;
        int magnitudeFloor = (int) Math.floor(getmEarthquakeMagnitude);
        switch(magnitudeFloor){
            case 0:
            case 1:
                mColorResourceId = R.color.magnitude1;
                break;
            case 2:
                mColorResourceId = R.color.magnitude2;
                break;
            case 3:
                mColorResourceId = R.color.magnitude3;
                break;
            case 4:
                mColorResourceId = R.color.magnitude4;
                break;
            case 5:
                mColorResourceId = R.color.magnitude5;
                break;
            case 6:
                mColorResourceId = R.color.magnitude6;
                break;
            case 7:
                mColorResourceId = R.color.magnitude7;
                break;
            case 8:
                mColorResourceId = R.color.magnitude8;
                break;
            case 9:
                mColorResourceId = R.color.magnitude9;
                break;
            default:
                mColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),mColorResourceId);
    }
}
