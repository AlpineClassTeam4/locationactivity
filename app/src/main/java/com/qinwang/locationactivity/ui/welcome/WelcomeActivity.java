package com.qinwang.locationactivity.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
 * @function:
 */
public class WelcomeActivity extends Activity {
    private final static int MSG_200 =200;

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
        if ((bundle != null)){
            String Latitude = bundle.getString("car_Latitude");
            String Longitude = bundle.getString("car_Longitude");
            if (Latitude != null && Longitude != null){
                MyApplication.car_Latitude = Double.parseDouble(Latitude);
                MyApplication.car_Longitude = Double.parseDouble(Longitude);
            }
        }
        mHandler.sendEmptyMessageDelayed(MSG_200, 300);
    }

    @Override
    public void onBackPressed() {

    }
}
