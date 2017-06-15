package com.napsterwan.cloudweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Napsterwan on 2017/6/14.
 * {
 *  "cond": {
 *      "code": "100",
 *      "txt": "晴"
 *  },
 *  "fl": "28",
 *  "hum": "41",
 *  "pcpn": "0",
 *  "pres": "1005",
 *  "tmp": "26",
 *  "vis": "10",
 *  "wind": {
 *      "deg": "330",
 *      "dir": "西北风",
 *      "sc": "6-7",
 *      "spd": "34"
 *  }
 *}
 */

public class Now {

    @SerializedName("cond")
    public Condition condition;

    @SerializedName("fl")
    public String fl;

    @SerializedName("hum")
    public String humidity;

    @SerializedName("pcpn")
    public String pcpn;

    @SerializedName("press")
    public String press;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("vis")
    public String visibility;

    @SerializedName("wind")
    public Wind wind;

    public class Condition {

        @SerializedName("code")
        public String code;

        @SerializedName("txt")
        public String info;
    }

    public class Wind {

        /**
         * 风向(360度)
         */
        @SerializedName("deg")
        public String degree;

        /**
         * 风向
         */
        @SerializedName("dir")
        public String direction;

        /**
         * 风力等级
         */
        @SerializedName("sc")
        public String windScale;

        /**
         * 风速
         */
        @SerializedName("spd")
        public String speed;
    }
}
