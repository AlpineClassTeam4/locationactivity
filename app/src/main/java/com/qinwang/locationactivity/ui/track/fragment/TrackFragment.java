package com.qinwang.locationactivity.ui.track.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.qinwang.locationactivity.R;
import com.qinwang.locationactivity.dao.TrackData;
import com.qinwang.locationactivity.ui.track.model.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrackFragment extends Fragment {

    private double lanSum = 0;
    private double lonSum = 0;
    private static int IMAGE_CLICK = 1;
    private int ACTIVITY_X, ACTIVITY_Y;                                 //Activity像素

    private FrameLayout frameLayout_track;
    private ImageView imageView_down_up;
    private TextView textView_start_address, textView_start_time,
            textView_end_address,textView_end_time;
    private MapView track_mapView;
    private BaiduMap BDMap;
    private Polyline mPolyline;                 //线条图层

    private BitmapDescriptor start = BitmapDescriptorFactory
            .fromResource(R.drawable.start);
    private BitmapDescriptor end = BitmapDescriptorFactory
            .fromResource(R.drawable.end);

    private ObjectAnimator objectAnimator_frameLayout_track;
    private static List<LatLng> track_latLng = new ArrayList<>();
    private static LatLng target;

    private static final String TAG = "TrackFragment";

    public static TrackFragment newInstance() {
        return new TrackFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        frameLayout_track = getView().findViewById(R.id.framelayout_track);
        imageView_down_up = getView().findViewById(R.id.imageView_dowm_up);
        textView_start_address = getView().findViewById(R.id.track_start_address);
        textView_end_address = getView().findViewById(R.id.track_end_address);
        textView_start_time = getView().findViewById(R.id.track_start_time);
        textView_end_time = getView().findViewById(R.id.track_end_time);

        getData();

        imageView_down_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IMAGE_CLICK == 1){
                    remove();
                    imageView_down_up.setImageResource(R.drawable.ic_up);
                    IMAGE_CLICK = 2;
                }else {
                    ReRemove();
                    imageView_down_up.setImageResource(R.drawable.ic_down);
                    IMAGE_CLICK = 1;
                }
            }
        });

    }

    /**
     * 地图显示
     */
    public void init(){
        track_mapView = getView().findViewById(R.id.track_map);
        BDMap = track_mapView.getMap();

//        // 设置个性化地图样式文件的路径和加载方式
//        track_mapView.setMapCustomStylePath(MyApplication.customStyleFilePath);
//        //动态设置个性化地图样式是否生效
//        track_mapView.setMapCustomStyleEnable(true);
        MapStatus mMapStatus = new MapStatus
                .Builder()
                .target(target)
                .zoom(17)                                               //设置级别，放大地图到17倍
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        BDMap.setMapStatus(mMapStatusUpdate);
        BDMap.setBuildingsEnabled(true);
    }

    /**
     * 数据监听
     */
    public void getData() {
        new ViewModelProvider(getActivity()).get(MyViewModel.class)
                .getLatLng()
                .observe(getViewLifecycleOwner(), new Observer<TrackData>() {
                    @Override
                    public void onChanged(TrackData trackData) {
                        track_latLng = trackData.getLatLngs();
                        for (int i = 0; i < track_latLng.size(); i++) {
                            lanSum += track_latLng.get(i).latitude;
                            lonSum += track_latLng.get(i).longitude;
                            Log.d(TAG, String.valueOf(track_latLng.get(i)));
                        }
                        /**
                         * 设置地图的缩放中心点为所有点的几何中心点
                         */
                        target = new LatLng(lanSum / track_latLng.size(), lonSum / track_latLng.size());
                        Log.d(TAG, "target = " + target);
                        GetPixel();
                        init();
                        draw();
                        textView_start_address.setText(trackData.getStart_address());
                        textView_start_time.setText(trackData.getStart_time());
                        textView_end_address.setText(trackData.getEnd_address());
                        textView_end_time.setText(trackData.getEnd_time());
                    }
                });
//                .observe(getViewLifecycleOwner(), new Observer<List<LatLng>>() {
//                    @Override
//                    public void onChanged(List<LatLng> latLngs) {
//                        track_latLng = latLngs;
//                        for (int i = 0; i < track_latLng.size(); i++) {
//                            lanSum += track_latLng.get(i).latitude;
//                            lonSum += track_latLng.get(i).longitude;
//                            Log.d(TAG, String.valueOf(track_latLng.get(i)));
//                        }
//                        /**
//                         * 设置地图的缩放中心点为所有点的几何中心点
//                         */
//                        target = new LatLng(lanSum / track_latLng.size(), lonSum / track_latLng.size());
//                        Log.d(TAG, "target = " + target);
//                        GetPixel();
//                        init();
//                        draw();
//                    }
//                });
    }

    /**
     * 绘制路径及始终点
     */
    public void draw(){
        OverlayOptions ooPolyline = new PolylineOptions()
                .color(Color.GREEN)
                .width(16)
                .points(track_latLng);
        //在地图上画出线条图层，mPolyline：线条图层
        mPolyline = (Polyline) BDMap.addOverlay(ooPolyline);
        mPolyline.setZIndex(3);
        //添加起点图层
        MarkerOptions oStart = new MarkerOptions();//地图标记类型的图层参数配置类
        oStart.position(track_latLng.get(0))//图层位置点，第一个点为起点
                .icon(start)
                .zIndex(1);//设置图层Index
        Marker mMarkerA = (Marker) (BDMap.addOverlay(oStart));
        //添加终点图层
        MarkerOptions oFinish = new MarkerOptions()
                .position(track_latLng.get(track_latLng.size() - 1))
                .icon(end)
                .zIndex(2);
        Marker mMarkerB = (Marker) (BDMap.addOverlay(oFinish));
    }

    /**
     * 下降
     */
    public void remove(){
        objectAnimator_frameLayout_track = ObjectAnimator.ofFloat(frameLayout_track,
                "translationY",
                0, ACTIVITY_Y);
        objectAnimator_frameLayout_track.setDuration(1000)
                .start();
    }

    /**
     * 上升
     */
    public void ReRemove(){
        objectAnimator_frameLayout_track = ObjectAnimator.ofFloat(frameLayout_track,
                "translationY",
                ACTIVITY_Y, 0);
        objectAnimator_frameLayout_track.setDuration(1000)
                .start();
    }

    /**
     * 获取手机界面像素
     */
    public void GetPixel(){
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        imageView_down_up.measure(w,h);
        frameLayout_track.measure(w,h);
        ACTIVITY_Y = frameLayout_track.getMeasuredHeight()
                - imageView_down_up.getMeasuredHeight();
        Log.d(TAG, "ACTIVITY_Y = " + ACTIVITY_Y);
    }

}