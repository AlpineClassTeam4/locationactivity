package com.qinwang.locationactivity.ui.gps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.qinwang.locationactivity.R;
import com.qinwang.locationactivity.ui.gps.listener.MyLocationListener;
import com.qinwang.locationactivity.ui.track.activity.RouteActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private int MAP_CLICK = 1;
    private int BAIDUMAP_CLICK = 1;

    private int ACTIVITY_X, ACTIVITY_Y;                                 //Activity像素

    public  static LatLng endPt;
    public  static LatLng startPt;

    private static double car_Latitude = 38.897403;
    private static double car_Longitude = 121.54351;

    public static TextView textView_longitude, textView_latitude, textView_address,
            textView_weather, textView_car_longitude, textView_car_latitude,
            textView_distance;
    public static ImageView imageView_weather;
    public static MapView mMapView;     // 定义百度地图组件
    public static BaiduMap mBaiduMap;   // 定义百度地图对

    private ImageView imageView_map;
    private LinearLayout layout_car, layout_navigation, layout_menu;
    private ConstraintLayout layout_car_message;

    private LocationClient mLocationClient;                             //定义LocationClient
    private BDAbstractLocationListener myLocationListener
            = new MyLocationListener(this);                     //定义位置监听
    private MyLocationConfiguration.LocationMode mLocationMode;         //定位模式
    private static WalkNaviLaunchParam walkParam;
    private UiSettings mUiSettings;

    private ObjectAnimator objectAnimator_translation_layout, objectAnimator_translation_image;                          //平移动画

    private BitmapDescriptor bdStart = BitmapDescriptorFactory                      //设置自定义定位图标（自己位置）
            .fromResource(R.drawable.mylocation);
    private BitmapDescriptor bdEnd = BitmapDescriptorFactory
            .fromResource(R.drawable.location);

    private static final String TAG = "BaiDuMap_message";

    /**
     * 地图定位与Activity生命周期绑定
     */
    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,new
                        String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);return;
            }else {
                mLocationClient.start();
            }
        }
        else{
            mLocationClient.start();
        }
    }

    /**
     * 停止地图定位
     */
    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * 销毁地图
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startPt = new LatLng(0.0,0.0);
        endPt = new LatLng(car_Latitude, car_Longitude);

        imageView_map = (ImageView) findViewById(R.id.imageButton_map_select);
        imageView_weather = (ImageView) findViewById(R.id.imageView_weather);
        textView_longitude = (TextView) findViewById(R.id.textView_longitude);
        textView_latitude = (TextView) findViewById(R.id.textView_latitude);
        textView_address = (TextView) findViewById(R.id.textView_address);
        textView_weather = (TextView) findViewById(R.id.textView_weather);
        textView_car_longitude = (TextView) findViewById(R.id.textView_car_longitude);
        textView_car_latitude = (TextView) findViewById(R.id.textView_car_latitude);
        textView_distance = (TextView) findViewById(R.id.textView_distance);
        layout_car = (LinearLayout) findViewById(R.id.layout_car);
        layout_navigation = (LinearLayout) findViewById(R.id.layout_navigation);
        layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
        layout_car_message = (ConstraintLayout) findViewById(R.id.layout_car_message_menu);
        mMapView = (MapView) findViewById(R.id.bmapview);                         //获取地图组件

        GetPixel();
        init();
        initLocation();

        mBaiduMap.setOnMapClickListener(mapClickListenerListener);
        imageView_map.setOnClickListener(this);
        layout_car.setOnClickListener(this);
        layout_navigation.setOnClickListener(this);
        layout_menu.setOnClickListener(this);

        textView_car_longitude.setText(String.valueOf(car_Longitude));
        textView_car_latitude.setText(String.valueOf(car_Latitude));
    }

    /**
     * 地图默认设置
     */
    public void init(){
        mBaiduMap = mMapView.getMap();                                  //获取百度地图对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        // 设置个性化地图样式文件的路径和加载方式
//        mMapView.setMapCustomStylePath(customStyleFilePath);
        // 动态设置个性化地图样式是否生效
        mMapView.setMapCustomStyleEnable(true);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus
                .Builder()
                .zoom(18)                                               //设置级别，放大地图到18倍
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setBuildingsEnabled(true);

        //实例化UiSettings类对象
        mUiSettings = mBaiduMap.getUiSettings();
        //通过设置enable为true或false 选择是否启用地图俯视功能
        mUiSettings.setOverlookingGesturesEnabled(true);

        //设置指南针位置
        Point point_compass = new Point((int)(ACTIVITY_X * 0.1), (int)(ACTIVITY_Y * 0.2));
        mBaiduMap.setCompassPosition(point_compass);
    }

    /**
     * 定位
     */
    private void initLocation(){
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;    //设置定位模式
        mLocationClient = new LocationClient(getApplicationContext());  //定位初始化
        mLocationClient.registerLocationListener(myLocationListener);   //注册LocationListener监听器
        /**
         * 通过LocationClientOption设置LocationClient相关参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                                        // 打开gps
        option.setLocationNotify(true);                                 //设置GPS有效时按照1S/1次频率输出GPS结果
        option.setCoorType("bd09ll");                                   // 设置坐标类型
        option.setScanSpan(1000);                                       //1秒定位一次
        option.setIsNeedAddress(true);                                  //地址信息
        option.setNeedDeviceDirect(true);
        mLocationClient.setLocOption(option);                           //保存定位参数与信息
        /**
         * 位置构造方式，将定位模式，定义图标添加其中
         */
        MyLocationConfiguration config =
                new MyLocationConfiguration(mLocationMode, true, bdStart);
        mBaiduMap.setMyLocationConfiguration(config);                   //地图显示定位图标

        OverlayOptions option_car = new MarkerOptions()
                .position(endPt)
                .icon(bdEnd);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option_car);
    }

    /**
     * 控件平移
     */
    public void translate(){
        objectAnimator_translation_layout = ObjectAnimator.ofFloat(layout_car_message,
                "translationY",
                0.0f, (float) (ACTIVITY_Y * 0.20));
        objectAnimator_translation_image = ObjectAnimator.ofFloat(imageView_map,
                "translationY",
                0.0f, (float) (ACTIVITY_Y * 0.20));
        objectAnimator_translation_layout.setDuration(1000)
                .start();
        objectAnimator_translation_image.setDuration(1000)
                .start();
    }

    /**
     * 控件复位
     */
    public void translate_return(){
        objectAnimator_translation_layout = ObjectAnimator.ofFloat(layout_car_message,
                "translationY",
                (float) (ACTIVITY_Y * 0.2), 0.0f);
        objectAnimator_translation_image = ObjectAnimator.ofFloat(imageView_map,
                "translationY",
                (float) (ACTIVITY_Y * 0.20), 0.0f);
        objectAnimator_translation_layout.setDuration(1000)
                .start();
        objectAnimator_translation_image.setDuration(1000)
                .start();
    }

    /**
     * 地图单击事件监听接口
     */
    BaiduMap.OnMapClickListener mapClickListenerListener = new BaiduMap.OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         * @param latLng    点击的地理坐标
         */
        @Override
        public void onMapClick(LatLng latLng) {
            if (BAIDUMAP_CLICK % 2 != 0){
                translate();
            }
            else {
                translate_return();
            }
            BAIDUMAP_CLICK ++;
        }

        /**
         * 地图内 Poi 单击事件回调函数
         * @param mapPoi    点击的 poi 信息
         */
        @Override
        public void onMapPoiClick(MapPoi mapPoi) {

        }
    };

    /**
     * 获取手机界面像素
     */
    public void GetPixel(){
        // 通过Activity类中的getWindowManager()方法获取窗口管理，再调用getDefaultDisplay()方法获取获取Display对象
        Display display = getWindowManager().getDefaultDisplay();
        //使用Point来保存屏幕宽、高两个数据
        Point outSize = new Point();
        // 通过Display对象获取屏幕宽、高数据并保存到Point对象中
        display.getSize(outSize);
        // 从Point对象中获取宽、高
        ACTIVITY_X = outSize.x;
        ACTIVITY_Y = outSize.y;
        Log.d(TAG, "手机像素为：宽-" + ACTIVITY_X + "，高-" + ACTIVITY_Y);
    }

    /**
     *构造导航起终点参数对象
     */
    public static void walk(){
        WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
        walkStartNode.setLocation(startPt);
        WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
        walkEndNode.setLocation(endPt);
        walkParam = new WalkNaviLaunchParam()
                .startNodeInfo(walkStartNode)
                .endNodeInfo(walkEndNode);
    }

    /**
     * 开始步行导航
     */
    private void startWalkNavi() {
        Log.d(TAG, "startWalkNavi");
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "WalkNavi engineInitSuccess");
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "WalkNavi engineInitFail");
                    WalkNavigateHelper.getInstance().unInitNaviEngine();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 发起步行导航算路
     */
    private void routePlanWithWalkParam() {
        WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "WalkNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {

                Log.d(TAG, "onRoutePlanSuccess");

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WNaviGuideActivity.class);
                startActivity(intent);

            }
            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "WalkNavi onRoutePlanFail:" + error);
                Toast.makeText(MainActivity.this,
                        String.valueOf(error),
                        Toast.LENGTH_LONG).show();
            }

        });
    }

    /**
     * 点击事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButton_map_select:
                if (MAP_CLICK %2 != 0){
                    imageView_map.setImageResource(R.drawable.earth);
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);              //卫星地图
                    MAP_CLICK ++;
                }else {
                    imageView_map.setImageResource(R.drawable.map);
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);                 //普通地图
//                    // 设置个性化地图样式文件的路径和加载方式
//                    mMapView.setMapCustomStylePath(MyApplication.customStyleFilePath);
//                    // 动态设置个性化地图样式是否生效
//                    mMapView.setMapCustomStyleEnable(true);
                    MAP_CLICK ++;
                }
                break;
            case R.id.layout_car:
                Log.d(TAG, "The position of the car has been located");
                break;
            case R.id.layout_navigation:
                walkParam.extraNaviMode(0);
                startWalkNavi();
                Log.d(TAG, "Start to navigate to the car's location");
                break;
            case R.id.layout_menu:
                startActivity(new Intent(MainActivity.this, RouteActivity.class));
                Log.d(TAG, "Travel record opening");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationClient.start();
                }else {
                    Toast.makeText(MainActivity.this,
                            "没有开启定位权限",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}