package com.scholat.law.ontrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TraceActivity extends Activity implements AMap.OnMapLoadedListener{

    private MapView mapView;
    private AMap aMap;

    List<LatLng> list;

    double trace[] = {
            40.03251239655422,116.44158040498262,
            40.03231736762684,116.44159368886524,
            40.03210400929456,116.44160364364582,
            40.031903970468456,116.44162358064602,
            40.031803975210416,116.44161691479444,
            40.03160064154208,116.44161023642441,
            40.03141064428492,116.44160189623058,
            40.03120398679096,116.44158856370204,
            40.03100398181874,116.4415852126018,
            40.03090898199395,116.44158187421888,
            40.030750657801356,116.44157021096972
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trace);
        mapView = (MapView) findViewById(R.id.map);//找到地图控件
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);


        mapView.getMap().setOnMapLoadedListener(this);

        aMap = mapView.getMap();//初始化地图控制器对象

        list = new ArrayList<LatLng>();
        for (int i = 0; i < trace.length-1; i++){
            LatLng latlng = new LatLng(trace[i],trace[++i]);
            list.add(latlng);
        }
    }

    @Override
    public void onMapLoaded() {
        mapView.getMap().addPolyline(new PolylineOptions().addAll(list));
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
