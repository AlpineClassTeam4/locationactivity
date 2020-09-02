package com.qinwang.locationactivity.ui.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qinwang.locationactivity.MyApplication;
import com.qinwang.locationactivity.R;
import com.qinwang.locationactivity.ui.gps.activity.MainActivity;

/**
 * @Auther:haoyanwang1121@gmail.com
 * @Date:2020/9/2
 * @Description:com.qinwang.locationactivity.ui.wellcome
 * @Version:1.0
 * @function:欢迎界面接收其他APP数据
 */
public class WelcomeActivity extends Activity {
    private final static int MSG_200 =200;

    private String Latitude;
    private String Longitude;
    private SharedPreferences sharedPreferences;

    private static final String TAG = "WelcomeActivity";

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MSG_200 :
                    startActivity(new Intent(WelcomeActivity.this,
                            MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            Latitude = bundle.getString("car_Latitude");
            Longitude = bundle.getString("car_Longitude");
            sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor  =sharedPreferences.edit();
            editor.putString("car_Latitude", Latitude);
            editor.putString("car_Longitude",Longitude);
            editor.apply();
        }
        SharedPreferences latlng = getPreferences(Context.MODE_PRIVATE);
        MyApplication.car_Latitude = Double.parseDouble(latlng.getString("car_Latitude",""));
        MyApplication.car_Longitude = Double.parseDouble(latlng.getString("car_Longitude",""));
        Log.d(TAG,"保存数据："+ MyApplication.car_Latitude + "," + MyApplication.car_Longitude);
        mHandler.sendEmptyMessageDelayed(MSG_200, 300);
    }

    @Override
    public void onBackPressed() {

    }
}
