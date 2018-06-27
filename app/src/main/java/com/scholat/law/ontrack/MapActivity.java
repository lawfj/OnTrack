package com.scholat.law.ontrack;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapActivity extends PermissionsActivity implements LocationSource,AMapLocationListener {

    //
    private MapView mapView;
    private AMap aMap;

    //
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationClientOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //
    private boolean isFirstLoc = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.map);//找到地图控件
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();//初始化地图控制器对象


        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        //是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        //是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        //设置定位点的图标  默认为蓝色小点
        MyLocationStyle myLocationStyle = new MyLocationStyle();



//        myLocationStyle.showMyLocation(true);
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。

        //设置完所有定位蓝点的属性之后再setMyLocationStyle
        aMap.setMyLocationStyle(myLocationStyle);


        //开始定位
        initLoc();

    }

    //定位
    private void initLoc() {

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);


        //初始化参数定位
        mLocationClientOption = new AMapLocationClientOption();
        //设置定位模式
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息，默认返回
        mLocationClientOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认强制
        mLocationClientOption.setWifiScan(true);
        //设置定位间隔  ms
        mLocationClientOption.setInterval(4000);


        //给定客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationClientOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //定位回调函数 必须重写
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功 回调信息 设置相关信息
                aMapLocation.getLocationType();//获取定位结果来源
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精确信息

                Toast.makeText(getApplicationContext(), "纬度："+aMapLocation.getLatitude()+"经度："+aMapLocation.getLongitude(), Toast.LENGTH_LONG).show();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);

                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                //定位后刷新小蓝点
                mListener.onLocationChanged(aMapLocation);

                if (isFirstLoc) {
                    //缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心点移动到定位点
                    mListener.onLocationChanged(aMapLocation);

                    isFirstLoc = false;

                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;

    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
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
        mLocationClient.stopLocation();
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
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }

}