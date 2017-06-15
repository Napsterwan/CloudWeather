package com.napsterwan.cloudweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Napsterwan on 2017/6/14.
 *
 *  {
 *       "city": "青岛",
 *       "cnty": "中国",
 *       "id": "CN101120201",
 *       "lat": "36.088000",
 *       "lon": "120.343000",
 *       "prov": "山东",
 *       "update": {
 *           "loc": "2016-08-30 11:52",
 *           "utc": "2016-08-30 03:52"
 *       }
 *   }
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("cnty")
    public String countryName;

    @SerializedName("id")
    public String weatherId;

    @SerializedName("lat")
    public String latitute;

    @SerializedName("lon")
    public String longitude;

    @SerializedName("prov")
    public String provinceName;

    @SerializedName("update")
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String upateTime;
    }
}
