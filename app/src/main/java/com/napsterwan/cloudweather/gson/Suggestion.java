package com.napsterwan.cloudweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Napsterwan on 2017/6/14.
 *
 * {
 *  "comf": {
 *      "brf": "较舒适",
 *      "txt": "白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"
 *  },
 *  "cw": {
 *      "brf": "较不宜",
 *      "txt": "较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。"
 *  },
 *  "drsg": {
 *      "brf": "热",
 *      "txt": "天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"
 *  },
 *  "flu": {
 *      "brf": "较易发",
 *      "txt": "虽然温度适宜但风力较大，仍较易发生感冒，体质较弱的朋友请注意适当防护。"
 *  },
 *  "sport": {
 *      "brf": "较适宜",
 *      "txt": "天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意防风。"
 *  },
 *  "trav": {
 *      "brf": "适宜",
 *      "txt": "天气较好，风稍大，但温度适宜，是个好天气哦。适宜旅游，您可以尽情地享受大自然的无限风光。"
 *  },
 *  "uv": {
 *      "brf": "强",
 *      "txt": "紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。"
 *  }
 *  }
 */

public class Suggestion {

    /**
     * 舒适度指数
     */
    @SerializedName("comf")
    public Comfort comfort;

    /**
     * 洗车指数
     */
    @SerializedName("cw")
    public CarWash carWash;

    /**
     * 穿衣指数
     */
    @SerializedName("drsg")
    public DressSuggestion dressSuggestion;

    /**
     * 感冒指数
     */
    @SerializedName("flu")
    public Flu flu;

    /**
     * 运动指数
     */
    @SerializedName("sport")
    public Sport sport;

    /**
     * 旅游指数
     */
    @SerializedName("trav")
    public Travel travel;

    /**
     * 紫外线指数
     */
    @SerializedName("uv")
    public Ultraviolet ultraviolet;

    public class Comfort {

        @SerializedName("brf")
        public String bref;

        @SerializedName("txt")
        public String info;
    }

    public class CarWash {

        @SerializedName("brf")
        public String bref;

        @SerializedName("txt")
        public String info;
    }

    public class DressSuggestion {

        @SerializedName("brf")
        public String bref;

        @SerializedName("txt")
        public String info;
    }

    public class Flu {

        @SerializedName("brf")
        public String bref;

        @SerializedName("txt")
        public String info;
    }

    public class Sport {

        @SerializedName("brf")
        public String bref;

        @SerializedName("txt")
        public String info;
    }

    public class Travel {

        @SerializedName("brf")
        public String bref;

        @SerializedName("txt")
        public String info;
    }

    public class Ultraviolet {

        @SerializedName("brf")
        public String bref;

        @SerializedName("txt")
        public String info;
    }
}
