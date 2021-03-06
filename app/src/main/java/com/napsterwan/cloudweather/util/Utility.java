package com.napsterwan.cloudweather.util;

import com.google.gson.Gson;

import com.napsterwan.cloudweather.db.City;
import com.napsterwan.cloudweather.db.Country;
import com.napsterwan.cloudweather.db.Province;
import com.napsterwan.cloudweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Napsterwan on 2017/6/7.
 */

public class Utility {
    /***
     * 处理省份信息
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response) {
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Province province = new Province();
                province.setProvinceCode(jsonObject.getInt("id"));
                province.setName(jsonObject.getString("name"));
                province.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理城市信息
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                City city = new City();
                city.setName(jsonObject.getString("name"));
                city.setCityCode(jsonObject.getInt("id"));
                city.setProvinceId(provinceId);
                city.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理区县信息
     */
    public static boolean handleCountryResponse(String response, int cityId) {
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Country country = new Country();
                country.setName(object.getString("name"));
                country.setCityId(cityId);
                country.setWeatherId(object.getString("weather_id"));
                country.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理天气请求信息
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray heWeather5 = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = heWeather5.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
