package com.qinwang.locationactivity.ui.gps.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.qinwang.locationactivity.MyApplication;
import com.qinwang.locationactivity.ui.gps.activity.MainActivity;
import com.qinwang.locationactivity.utils.WeatherMsg;

import java.math.BigDecimal;

/**
 * @Auther:haoyanwang1121@gmail.com
 * @Date:2020/8/18
 * @Description:com.qinwang.locationactivity.util
 * @Version:1.0
 * @function:位置监听
 */
public class MyLocationListener extends BDAbstractLocationListener {

    private boolean isFirstLoc = true;  //定义第一次启动
    private static final String TAG = "LocationListener_message";
    private Context context;
    public MyLocationListener(Context context){
        this.context = context;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onReceiveLocation(BDLocation location) {

        double latitude = location.getLatitude();       //获取纬度信息
        double longitude = location.getLongitude();     //获取经度信息
        String addr = location.getAddrStr();            //获取详细地址信息
        String country = location.getCountry();         //获取国家
        String province = location.getProvince();       //获取省份
        String city = location.getCity();               //获取城市
        String district = location.getDistrict();       //获取区县
        String street = location.getStreet();           //获取街道信息
        String adcode = location.getAdCode();           //获取adcode
        String town = location.getTown();               //获取乡镇信息
        float radius = location.getRadius();            //获取定位精度，默认值为0.0f

        //mapView 销毁后不在处理新接收的位置
        if (location == null || MainActivity.mMapView == null){
            return;
        }
        if (city.equals("北京市") || city.equals("天津市") || city.equals("上海市") || city.equals("重庆市"))
            MainActivity.textView_address.setText(country + "\n" + city + "\n" + district + "\n" + street + town);
        else MainActivity.textView_address.setText(country + "\n" + province + "\n" + city + "\n" + district + "\n" + street + town);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(radius)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection())
                .latitude(latitude)
                .longitude(longitude)
                .build();
        MainActivity.mBaiduMap.setMyLocationData(locData);
        MainActivity.textView_latitude.setText(String.valueOf(latitude));
        MainActivity.textView_longitude.setText(String.valueOf(longitude));
        MainActivity.startPt = new LatLng(latitude, longitude);

        MainActivity.textView_car_longitude.setText(String.valueOf(new BigDecimal(MainActivity.endPt.longitude)
                .setScale(6,BigDecimal.ROUND_HALF_UP)
                .doubleValue()));
        MainActivity.textView_car_latitude.setText(String.valueOf(new BigDecimal(MainActivity.endPt.latitude)
                .setScale(6,BigDecimal.ROUND_HALF_UP)
                .doubleValue()));
        //与车的距离四舍五入保留两位小数
        BigDecimal distance = new BigDecimal((DistanceUtil.getDistance(MainActivity.startPt,
                MainActivity.endPt)) * Math.pow(10, -3));
        MainActivity.textView_distance.setText(String.valueOf(distance
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue()) + "Km");
        MainActivity.walk();

        /**
         * 如果是第一次定位,就定位到以自己为中心
         */
        if (isFirstLoc) {
            LatLng ll = new LatLng(latitude, longitude);                            //获取用户当前经纬度
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);               //更新坐标位置
            MainActivity.mBaiduMap.setMyLocationData(locData);
            MainActivity.mBaiduMap.animateMapStatus(u);                             //设置地图位置
            isFirstLoc = false;                                                     //取消第一次定位
        }
        if (!MyApplication.address.equals(addr)){
            Log.d(TAG,"当前位置信息：经度：" + longitude
                    + "\n维度：" + latitude
                    + "\n城市：" + addr);
            MyApplication.address = addr;
            Log.d(TAG, MyApplication.address);
            WeatherMsg weatherMsg = new WeatherMsg(context, district);
            weatherMsg.getWeatherMsg();
            isFirstLoc = true;
        }
    }

    /**
     *定位失败原因
     * @param i     返回值
     * @param i1    类型
     * @param s     问题排查策略
     */
    @SuppressLint("LongLogTag")
    @Override
    public void onLocDiagnosticMessage(int i, int i1, String s) {
        super.onLocDiagnosticMessage(i, i1, s);
        Log.e(TAG,"返回值：" + i + "类型：" + i1
                + "\n问题策略：" + s);
    }
}