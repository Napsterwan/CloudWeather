package com.napsterwan.cloudweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Napsterwan on 2017/6/14.
 *
 * {
 *      "city": {
 *          "aqi": "60",
 *          "co": "0",
 *          "no2": "14",
 *          "o3": "95",
 *          "pm10": "67",
 *          "pm25": "15",
 *          "qlty": "良",
 *          "so2": "10"
 *      }
 *  }
 */

public class AQI {

    @SerializedName("city")
    public AQICity city;

    public class AQICity {
        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm25")
        public String pm25;

        @SerializedName("qlty")
        public String quality;
    }
}
