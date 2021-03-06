package com.napsterwan.cloudweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.napsterwan.cloudweather.gson.Forecast;
import com.napsterwan.cloudweather.gson.Weather;
import com.napsterwan.cloudweather.service.AutoUpdateService;
import com.napsterwan.cloudweather.util.Constant;
import com.napsterwan.cloudweather.util.HttpUtil;
import com.napsterwan.cloudweather.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView titleCity;

    private TextView nowCondition;

    private TextView nowTemperature;

    private TextView nowRefreshTime;

    private TextView aqiNum;

    private TextView pm25;

    private TextView comfortInfo;

    private TextView carWashInfo;

    private TextView dressSuggestion;

    private TextView fluInfo;

    private TextView sportInfo;

    private TextView travelInfo;

    private TextView ultravioletInfo;

    private ImageView nowImage;

    private ScrollView scrollView;

    private LinearLayout forecastListLayout;

    private ImageView bingPic;

    public SwipeRefreshLayout refreshLayout;

    public DrawerLayout drawerLayout;

    private ImageView homeBtn;

    private LinearLayout aqiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        titleCity = (TextView) findViewById(R.id.title_text);
        nowCondition = (TextView) findViewById(R.id.now_condition);
        nowRefreshTime = (TextView) findViewById(R.id.now_refresh_time);
        nowTemperature = (TextView) findViewById(R.id.now_temperature);
        nowImage = (ImageView) findViewById(R.id.now_img);
        aqiNum = (TextView) findViewById(R.id.aqi_num);
        pm25 = (TextView) findViewById(R.id.aqi_pm25);
        comfortInfo = (TextView) findViewById(R.id.suggestion_comfort);
        carWashInfo = (TextView) findViewById(R.id.suggestion_car_wash);
        dressSuggestion = (TextView) findViewById(R.id.suggestion_dress);
        fluInfo = (TextView) findViewById(R.id.suggestion_flu);
        sportInfo = (TextView) findViewById(R.id.suggestion_sport);
        travelInfo = (TextView) findViewById(R.id.suggestion_travel);
        ultravioletInfo = (TextView) findViewById(R.id.suggestion_ultraviolet);
        scrollView = (ScrollView) findViewById(R.id.weather_scrollview);
        forecastListLayout = (LinearLayout) findViewById(R.id.dialy_forecast_layout);
        bingPic = (ImageView) findViewById(R.id.weather_pic);
        scrollView.setVisibility(View.INVISIBLE);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        homeBtn = (ImageView) findViewById(R.id.title_home);
        aqiLayout = (LinearLayout) findViewById(R.id.aqi_layout);

        if (Build.VERSION.SDK_INT >= 21) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather", null);
        final String weatherId;
        if (weatherString == null) {
            weatherId = getIntent().getStringExtra("weather_id");
            requestWeather(weatherId);
        } else {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }
        String bingPicUrl = sharedPreferences.getString("bingPic", null);
        if (bingPicUrl == null) {
            Glide.with(this).load(R.drawable.default_bing).into(bingPic);
            requestBingPic();
        } else {
            Glide.with(this).load(bingPicUrl).into(bingPic);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void requestBingPic() {
        HttpUtil.sendOkhttpRequest(Constant.BING_PIC_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseContent = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseContent);
                    final String imageUrl = jsonObject.getJSONObject("data").getString("url");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (imageUrl != null) {
                                PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit().putString("bingPic", imageUrl).apply();
                                Glide.with(WeatherActivity.this).load(imageUrl).transition(new DrawableTransitionOptions().crossFade()).into(bingPic);
                            } else {
                                Glide.with(WeatherActivity.this).load(R.drawable.default_bing).into(bingPic);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        titleCity.setText(weather.basic.cityName);
        nowTemperature.setText(weather.now.temperature + getString(R.string.degree));
        nowRefreshTime.setText(getString(R.string.refresh_time) + weather.basic.update.upateTime.substring(11, 16));
        nowCondition.setText(weather.now.condition.info);
        if (weather.aqi != null) {
            aqiLayout.setVisibility(View.VISIBLE);
            aqiNum.setText(weather.aqi.city.aqi);
            pm25.setText(weather.aqi.city.pm25);
        } else {
            aqiLayout.setVisibility(View.GONE);
        }
        comfortInfo.setText(getString(R.string.comfort_title) + weather.suggestion.comfort.info);
        carWashInfo.setText(getString(R.string.carwash_title) + weather.suggestion.carWash.info);
        dressSuggestion.setText(getString(R.string.dress_title) + weather.suggestion.dressSuggestion.info);
        sportInfo.setText(getString(R.string.sport_title) + weather.suggestion.sport.info);
        fluInfo.setText(getString(R.string.flu_title) + weather.suggestion.flu.info);
        travelInfo.setText(getString(R.string.travel_title) + weather.suggestion.travel.info);
        ultravioletInfo.setText(getString(R.string.uv_title) + weather.suggestion.ultraviolet.info);
        forecastListLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.daily_forecast_item, forecastListLayout, false);
            TextView forecastDate = (TextView) view.findViewById(R.id.forecast_item_date);
            TextView forecastInfo = (TextView) view.findViewById(R.id.forecast_item_info);
            TextView maxTemperature = (TextView) view.findViewById(R.id.forecast_item_max_temperature);
            TextView minTemperature = (TextView) view.findViewById(R.id.forecast_item_min_temperature);
            forecastDate.setText(forecast.date);
            forecastInfo.setText(forecast.condition.info);
            maxTemperature.setText(forecast.temperature.max);
            minTemperature.setText(forecast.temperature.min);
            forecastListLayout.addView(view);
        }

        String code = weather.now.condition.code;
        Glide.with(this).load(Constant.IMAGE_URL + code + Constant.PNG_SUFFIX).into(nowImage);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);

        scrollView.setVisibility(View.VISIBLE);
    }

    public void requestWeather(String weatherId) {
        String url = Constant.WEATHER_URL + "city=" + weatherId + "&key=" + Constant.WEATHER_KEY;
        HttpUtil.sendOkhttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, R.string.request_weather_fail, Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseContent = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseContent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status.equals("ok")) {
                            PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit().putString("weather", responseContent).apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, R.string.request_weather_fail, Toast.LENGTH_SHORT).show();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
