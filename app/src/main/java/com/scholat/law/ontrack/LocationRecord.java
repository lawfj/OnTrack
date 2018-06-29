package com.scholat.law.ontrack;

import cn.bmob.v3.BmobObject;

public class LocationRecord extends BmobObject {
    private double longitude;//经度
    private double latitude;//纬度

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
