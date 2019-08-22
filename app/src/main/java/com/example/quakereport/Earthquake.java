package com.example.quakereport;

public class Earthquake {
    private Double mEarthquakeMagnitude;
    private String mEarthQuakeLocation;
    private String mEarthQuakeDate;
    private String mEarthQuakeTime;
    private String mUrl;
    public Earthquake(Double earthquakeMagnitude, String earthQuakeLocation, String earthQuakeDate,String earthQuakeTime,String url) {
        mEarthquakeMagnitude = earthquakeMagnitude;
        mEarthQuakeLocation = earthQuakeLocation;
        mEarthQuakeDate = earthQuakeDate;
        mEarthQuakeTime = earthQuakeTime;
        mUrl = url;
    }

    public String getmUrl() {
        return mUrl;
    }

    public Double getmEarthquakeMagnitude() {
        return mEarthquakeMagnitude;
    }

    public String getmEarthQuakeLocation() {
        return mEarthQuakeLocation;
    }

    public String getmEarthQuakeDate() {
        return mEarthQuakeDate;
    }

    public String getmEarthQuakeTime() {
        return mEarthQuakeTime;
    }
}
