package com.napsterwan.cloudweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Napsterwan on 2017/6/6.
 */

public class Province extends DataSupport{

    private int id;

    private String name;

    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
