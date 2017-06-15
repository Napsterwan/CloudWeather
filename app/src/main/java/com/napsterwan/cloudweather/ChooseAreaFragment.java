package com.napsterwan.cloudweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.napsterwan.cloudweather.db.City;
import com.napsterwan.cloudweather.db.Country;
import com.napsterwan.cloudweather.db.Province;
import com.napsterwan.cloudweather.util.HttpUtil;
import com.napsterwan.cloudweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Napsterwan on 2017/6/7.
 */

public class ChooseAreaFragment extends Fragment {

    private static final int LEVEL_PROVINCE = 0;

    private static final int LEVEL_CITY = 1;

    private static final int LEVEL_COUNTRY = 2;

    private TextView title;

    private Button back;

    private ListView listView;

    private List<String> dataList = new ArrayList<String>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<Country> countryList;

    private int currentLevel;

    private ArrayAdapter<String> adapter;

    private Province selectedProvince;

    private City selectedCity;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        title = (TextView) view.findViewById(R.id.title);
        back = (Button) view.findViewById(R.id.back);
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCity();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCountry();
                } else {
                    Country country = countryList.get(position);
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", country.getWeatherId());
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_CITY) {
                    queryProvince();

                } else if (currentLevel == LEVEL_COUNTRY) {
                    queryCity();
                }
            }
        });

        queryProvince();
    }

    private void queryProvince() {
        title.setText("中国");
        back.setVisibility(View.GONE);
        dataList.clear();
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            for (Province province : provinceList) {
                dataList.add(province.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String addr = "http://guolin.tech/api/china";
            queryFromServer(addr, "province");
        }
    }

    private void queryCity() {
        title.setText(selectedProvince.getName());
        back.setVisibility(View.VISIBLE);
        dataList.clear();
        cityList = DataSupport.where("provinceId = ?", String.valueOf(selectedProvince.getProvinceCode())).find(City.class);
        if (cityList.size() > 0) {
            for (City city : cityList) {
                dataList.add(city.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            String addr = "http://guolin.tech/api/china/" + selectedProvince.getProvinceCode();
            queryFromServer(addr, "city");
        }

    }

    private void queryCountry() {
        title.setText(selectedCity.getName());
        back.setVisibility(View.VISIBLE);
        dataList.clear();
        countryList = DataSupport.where("cityId = ?", String.valueOf(selectedCity.getCityCode())).find(Country.class);
        if (countryList.size() > 0) {
            for (Country country : countryList) {
                dataList.add(country.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTRY;
        } else {
            String addr = "http://guolin.tech/api/china/" + selectedCity.getProvinceId() + "/" + selectedCity.getCityCode();
            queryFromServer(addr, "country");
        }
    }


    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                boolean result = false;
                String responseStr = response.body().string();
                if (type.equals("province")) {
                    result = Utility.handleProvinceResponse(responseStr);
                } else if (type.equals("city")) {
                    result = Utility.handleCityResponse(responseStr, selectedProvince.getProvinceCode());
                } else if (type.equals("country")) {
                    result = Utility.handleCountryResponse(responseStr, selectedCity.getCityCode());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                            if (type.equals("province")) {
                                queryProvince();
                            } else if (type.equals("city")) {
                                queryCity();
                            } else if (type.equals("country")) {
                                queryCountry();
                            }
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                            Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
