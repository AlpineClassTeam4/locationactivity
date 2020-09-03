package com.qinwang.locationactivity.ui.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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
    private boolean isFiest = true;

    private String Latitude;
    private String Longitude;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String TAG = "WelcomeActivity";

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MSG_200 :
                    finish();
                    startActivity(new Intent(WelcomeActivity.this,
                            MainActivity.class));
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

        sharedPreferences = getSharedPreferences("Lalo",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString("car_Latitude", "null");
        editor.putString("car_Longitude","null");
        editor.apply();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            String ll = bundle.getString("points");
            String points[] = ll.split(",");
            Latitude = points[0];
            Longitude = points[1];
            editor.putString("car_Latitude", Latitude);
            editor.putString("car_Longitude",Longitude);
            editor.commit();
        }
        if (sharedPreferences.getString("car_Latitude",null).equals("null") ||
                sharedPreferences.getString("car_Longitude",null).equals("null")){
            Toast.makeText(WelcomeActivity.this,
                    "请先获取汽车位置信息",
                    Toast.LENGTH_LONG);
            finish();
        }else {
            MyApplication.car_Latitude = Double.parseDouble(sharedPreferences.getString("car_Latitude",null));
            MyApplication.car_Longitude = Double.parseDouble(sharedPreferences.getString("car_Longitude",null));
            Log.d(TAG,"保存数据："+ MyApplication.car_Latitude + "," + MyApplication.car_Longitude);
            mHandler.sendEmptyMessageDelayed(MSG_200, 300);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
