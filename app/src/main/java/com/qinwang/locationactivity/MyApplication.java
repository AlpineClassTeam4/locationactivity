package com.qinwang.locationactivity;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Auther:haoyanwang1121@gmail.com
 * @Date:2020/8/21
 * @Description:com.qinwang.locationactivity
 * @Version:1.0
 * @function:全局变量
 */
public class MyApplication extends Application {

    public static double car_Latitude = 38.897403;
    public static double car_Longitude = 121.54351;

    public static String address = "北京";
    public static  String CUSTOM_FILE_NAME_HY = "custom_map_config_HY.sty";         //设置个性化地图的样式文件
    public static String customStyleFilePath;                                       //个性化地图的样式文件地址
    public static String uri = "http://wthrcdn.etouch.cn/weather_mini?city=";

    public static String URI = "http://192.168.43.232/data/all";
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        customStyleFilePath = getCustomStyleFilePath(getApplicationContext(), MyApplication.CUSTOM_FILE_NAME_HY);
    }

    /**
     * 读取sty路径
     * @param context
     * @param customStyleFileName
     * @return
     */
    private String getCustomStyleFilePath(Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;
        try {
            inputStream = context.getAssets().open(customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists()) {
                customStyleFile.delete();
            }
            customStyleFile.createNewFile();

            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Copy custom style file failed", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Close stream failed", e);
                return null;
            }
        }
        return parentPath + "/" + customStyleFileName;
    }

    /***
     * 处理http返回结果
     * @param inputStream
     * @return
     */
    public static String dealResponseResult(InputStream inputStream) {
        StringBuilder html = new StringBuilder();      //存储处理结果
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String s;
            while ((s = reader.readLine()) != null) {
                html.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html.toString();
    }
}
