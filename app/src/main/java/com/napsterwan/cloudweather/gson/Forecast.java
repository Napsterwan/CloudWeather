package com.napsterwan.cloudweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Napsterwan on 2017/6/14.
 *
 * {
 *  "astro": {
 *      "mr": "03:09",
 *      "ms": "17:06",
 *      "sr": "05:28",
 *      "ss": "18:29"
 *  },
 *  "cond": {
 *      "code_d": "100",
 *      "code_n": "100",
 *      "txt_d": "晴",
 *      "txt_n": "晴"
 *  },
 *  "date": "2016-08-30",
 *  "hum": "45",
 *  "pcpn": "0.0",
 *  "pop": "8",
 *  "pres": "1005",
 *  "tmp": {
 *      "max": "29",
 *      "min": "22"
 *  },
 *  "vis": "10",
 *  "wind": {
 *      "deg": "339",
 *      "dir": "北风",
 *      "sc": "4-5",
 *      "spd": "24"
 *  }
 *  }
 */

public class Forecast {

    @SerializedName("cond")
    public Condition condition;

    @SerializedName("date")
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    public class Condition {

        @SerializedName("code_d")
        public String code;

        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature {

        @SerializedName("max")
        public String max;

        @SerializedName("min")
        public String min;
    }
}
