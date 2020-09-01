package com.qinwang.locationactivity.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.qinwang.locationactivity.MyApplication;
import com.qinwang.locationactivity.R;
import com.qinwang.locationactivity.dao.WeatherBean;;
import com.qinwang.locationactivity.ui.gps.activity.MainActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Auther:haoyanwang1121@gmail.com
 * @Date:2020/8/24
 * @Description:com.qinwang.locationactivity.utils
 * @Version:1.0
 * @function:
 */
public class WeatherMsg {

    private static final String TAG = "WeatherMsg_message";

    private Context context;
    private String city;
    public WeatherMsg(Context context, String city){
        this.context = context;
        this.city = city;
    }
    /**
     * 向天气查询API发送GET请求
     */
    public void getWeatherMsg(){

        Log.d(TAG, "getWeatherMsg: city = " + city);

        final String URI = MyApplication.uri + city;
        Log.d(TAG, "getWeatherMsg: URI = " + URI);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URI);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5 * 1000);
                    connection.setReadTimeout(5 * 1000);
                    connection.connect();
                    //获得服务器的响应码
                    int response = connection.getResponseCode();
                    if(response == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        String html = dealResponseResult(inputStream);
                        //处理服务器相应结果
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = html;
                        mHandler.sendMessage(msg);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    //错误处理
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = e.toString();
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /***
     * 处理http返回结果
     * @param inputStream
     * @return
     */
    private String dealResponseResult(InputStream inputStream) {
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

    /***
     * 跨线程更新UI界面
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //成功返回结果
                    //textView.setText(msg.obj.toString());
                    Gson gson = new Gson();
                    WeatherBean bean = gson.fromJson(msg.obj.toString(),
                            WeatherBean.class);
                    MainActivity.textView_weather.setText(bean.getData().getForecast().get(0).getType()
                            + bean.getData().getWendu() +"℃");
                    if(bean.getData().getForecast().get(0).getType().equals("暴雪")) MainActivity.imageView_weather.setImageResource(R.drawable.baoxue);
                    if(bean.getData().getForecast().get(0).getType().equals("大雪")) MainActivity.imageView_weather.setImageResource(R.drawable.daxue);
                    if(bean.getData().getForecast().get(0).getType().equals("中雪")) MainActivity.imageView_weather.setImageResource(R.drawable.zhongxue);
                    if(bean.getData().getForecast().get(0).getType().equals("小雪")) MainActivity.imageView_weather.setImageResource(R.drawable.xioaxue);
                    if(bean.getData().getForecast().get(0).getType().equals("雨夹雪")) MainActivity.imageView_weather.setImageResource(R.drawable.yujiaxue);
                    if(bean.getData().getForecast().get(0).getType().equals("暴雨")) MainActivity.imageView_weather.setImageResource(R.drawable.baoyu);
                    if(bean.getData().getForecast().get(0).getType().equals("大雨")) MainActivity.imageView_weather.setImageResource(R.drawable.dayu);
                    if(bean.getData().getForecast().get(0).getType().equals("中雨")) MainActivity.imageView_weather.setImageResource(R.drawable.zhongyu);
                    if(bean.getData().getForecast().get(0).getType().equals("小雨")) MainActivity.imageView_weather.setImageResource(R.drawable.xiaoyu);
                    if(bean.getData().getForecast().get(0).getType().equals("雷阵雨")) MainActivity.imageView_weather.setImageResource(R.drawable.leizhenyu);
                    if(bean.getData().getForecast().get(0).getType().equals("阵雨")) MainActivity.imageView_weather.setImageResource(R.drawable.zhenyu);
                    if(bean.getData().getForecast().get(0).getType().equals("阴")) MainActivity.imageView_weather.setImageResource(R.drawable.yin);
                    if(bean.getData().getForecast().get(0).getType().equals("多云")) MainActivity.imageView_weather.setImageResource(R.drawable.duoyun);
                    if(bean.getData().getForecast().get(0).getType().equals("晴")) MainActivity.imageView_weather.setImageResource(R.drawable.sun);
                    if(bean.getData().getForecast().get(0).getType().equals("雾")) MainActivity.imageView_weather.setImageResource(R.drawable.wu);
                    if(bean.getData().getForecast().get(0).getType().equals("沙尘暴")) MainActivity.imageView_weather.setImageResource(R.drawable.shachenbao);
                    if(bean.getData().getForecast().get(0).getType().equals("霜冻")) MainActivity.imageView_weather.setImageResource(R.drawable.shuangdong);
                    if(bean.getData().getForecast().get(0).getType().equals("台风")) MainActivity.imageView_weather.setImageResource(R.drawable.taifeng);
                    if(bean.getData().getForecast().get(0).getType().equals("冰雹")) MainActivity.imageView_weather.setImageResource(R.drawable.bingbao);
                    break;
                case 2:
                    //出错
                    MainActivity.textView_weather.setText("网络错误!!!");
                    MainActivity.imageView_weather.setImageResource(R.drawable.error);
                    Log.d(TAG, "handleMessage: " + msg.obj.toString());
                    break;
            }
            return false;
        }
    });
}
