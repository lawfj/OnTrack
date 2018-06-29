package com.scholat.law.ontrack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.TraceLocation;
import com.scholat.law.ontrack.record.PathRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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

    private Polyline mpolyline;
    private PathRecord record;
    private long mStartTime;
    private long mEndTime;
    private PolylineOptions mPolyoptions, tracePolytion;
    private List<TraceLocation> mTracelocationlist = new ArrayList<TraceLocation>();

    private LocationRecord locationRecord;
    private double longitude;//经度
    private double latitude;//纬度
    List<LocationRecord> locationRecords;


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


        record = new PathRecord();
        mStartTime = System.currentTimeMillis();
        record.setDate(getcueDate(mStartTime));

        Bmob.initialize(this, "205d8b13a2180ea307f707c0cdf62752");

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
                latitude = aMapLocation.getLatitude();//获取纬度
                longitude = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精确信息

//                Toast.makeText(getApplicationContext(), "纬度："+aMapLocation.getLatitude()+"经度："+aMapLocation.getLongitude(), Toast.LENGTH_LONG).show();

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



//                LatLng mylocation = new LatLng(aMapLocation.getLatitude(),
//                        aMapLocation.getLongitude());
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
//                record.addpoint(aMapLocation);
//                mPolyoptions.add(mylocation);
//                mTracelocationlist.add(Util.parseTraceLocation(amapLocation));
//                redrawline();
//                if (mTracelocationlist.size() > tracesize - 1) {
//                    trace();
//                }


                System.out.println("hello!");

                saveLocationMsg();
                getLocationMsg();




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

    /**
     * 实时轨迹画线
     */
    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 1) {
            if (mpolyline != null) {
                mpolyline.setPoints(mPolyoptions.getPoints());
            } else {
                mpolyline = aMap.addPolyline(mPolyoptions);
            }
        }
        System.out.println(mPolyoptions.isVisible());
//		if (mpolyline != null) {
//			mpolyline.remove();
//		}
//		mPolyoptions.visible(true);
//		mpolyline = mAMap.addPolyline(mPolyoptions);
//			PolylineOptions newpoly = new PolylineOptions();
//			mpolyline = mAMap.addPolyline(newpoly.addAll(mPolyoptions.getPoints()));
//		}
    }

    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    /**
     * 保存经纬度数据到Bmob后台
     */
    public void saveLocationMsg(){
        locationRecord = new LocationRecord();
        locationRecord.setLatitude(latitude);
        locationRecord.setLongitude(longitude);
        locationRecord.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e != null){
                    Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MapActivity.this, "success", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 查询Bmob经纬度信息
     */
    public void getLocationMsg(){
        BmobQuery<LocationRecord> query = new BmobQuery<LocationRecord>();
        query.addWhereNotEqualTo("longitude", "");
        query.findObjects(new FindListener<LocationRecord>() {
            @Override
            public void done(List<LocationRecord> list, BmobException e) {
                if (e != null) {
//                    Toast.makeText(MainActivity.this, "出错啦！", Toast.LENGTH_LONG).show();
                }else {
                    locationRecords = list;
                    List<LatLng> locationList = new ArrayList<LatLng>();
                    for (int i = 1; i < locationRecords.size(); i++){
                        LatLng latLng = new LatLng(locationRecords.get(i).getLatitude(), locationRecords.get(i).getLongitude());
                        locationList.add(latLng);
                    }
                    drawTrack(locationList);
                }
            }
        });
    }

    public void drawTrack(List<LatLng> locationList){
        mapView.getMap().addPolyline(new PolylineOptions().addAll(locationList));
    }
}