package com.napsterwan.cloudweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.napsterwan.cloudweather.gson.Weather;
import com.napsterwan.cloudweather.util.Constant;
import com.napsterwan.cloudweather.util.HttpUtil;
import com.napsterwan.cloudweather.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Napsterwan on 2017/6/15.
 */

public class AutoUpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 8 * 60 * 60 * 1000;
        long triggleTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, i, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggleTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        HttpUtil.sendOkhttpRequest(Constant.BING_PIC_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseContent = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseContent);
                    final String imageUrl = jsonObject.getJSONObject("data").getString("url");
                    if (imageUrl != null) {
                        PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit().putString("bingPic", imageUrl).apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sp.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String url = Constant.WEATHER_URL + "city=" + weatherId + "&key=" + Constant.WEATHER_KEY;
            HttpUtil.sendOkhttpRequest(url, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseContent = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseContent);
                    if (weather != null && weather.status.equals("ok")) {
                        PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit().putString("weather", responseContent).apply();
                    }
                }
            });
        }
    }
}
