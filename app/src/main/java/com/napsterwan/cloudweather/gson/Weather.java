package com.napsterwan.cloudweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Napsterwan on 2017/6/14.
 */

public class Weather {

    @SerializedName("alarms")
    public List<Alarm> alarmList;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    public AQI aqi;

    public Basic basic;

    public Now now;

    public Suggestion suggestion;

    public String status;
}
