package com.qinwang.locationactivity.ui.track.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.qinwang.locationactivity.MyApplication;
import com.qinwang.locationactivity.R;
import com.qinwang.locationactivity.dao.LocatonBean;
import com.qinwang.locationactivity.dao.TrackData;
import com.qinwang.locationactivity.ui.track.model.MyViewModel;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RouteFragment extends Fragment {

    private ListView listView;
    private List<TrackData> trackDataList = new ArrayList<>();
    private MyAdapter myAdapter;

    private MyViewModel mViewModel;
    private static final String TAG = "RouteFragment";

    public static RouteFragment newInstance() {
        return new RouteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getView().findViewById(R.id.track_list);
        mViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
//        final List<LatLng> latLngs1 = new ArrayList<>();
//        latLngs1.add(new LatLng(36.906706, 114.57386));
//        latLngs1.add(new LatLng(36.907197, 114.57388));
//        latLngs1.add(new LatLng(36.907587, 114.57393));
//        latLngs1.add(new LatLng(36.907547, 114.57212));
//        latLngs1.add(new LatLng(36.907587, 114.56944));
//
//        TrackData trackData = new TrackData("2020/9/1-10:20:10",
//                "2020/9/1-11:20:10",
//                "北京市天安门",
//                "北京市长城",
//                latLngs1);
//        trackDataList.clear();
//        trackDataList.add(trackData);
        getData();

    }

    private void ItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewModel.setLatLng(trackDataList.get(position));
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_routeFragment_to_trackFragment);
            }
        });
    }

    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(MyApplication.URI);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");            //请求方式
                    connection.setConnectTimeout(5 * 1000);
                    connection.setReadTimeout(5 * 1000);
                    connection.connect();
                    //获得服务器的响应码
                    int response = connection.getResponseCode();
                    if(response == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        String html = MyApplication.dealResponseResult(inputStream);
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
     * 跨线程更新UI界面
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //成功返回结果
                    Gson gson = new Gson();
                    LocatonBean locatonBean = gson.fromJson(msg.obj.toString(),
                            LocatonBean.class);
                    trackDataList.clear();
                    for (int x = 0; x < locatonBean.getData().size() ; x++){
                        String[] ll = locatonBean.getData().get(x).getPoints().split(",");
                        List<LatLng> list = new ArrayList<>();
                        Log.d(TAG,locatonBean.getData().get(x).getPoints());
                        for (int y = 0; (2 * y + 1 ) < ll.length; y ++){
                            LatLng latLng = new LatLng(Double.valueOf(ll[2 * y])
                                    , Double.valueOf(ll[2 *y + 1]));
                            Log.d(TAG,String.valueOf(latLng));
                            list.add(latLng);

                        }
                        Log.d(TAG,String.valueOf(list));
                        TrackData trackData = new TrackData(locatonBean.getData().get(x).getStart_time(),
                                locatonBean.getData().get(x).getEnd_time(),
                                locatonBean.getData().get(x).getStart_address(),
                                locatonBean.getData().get(x).getEnd_address(),
                                list);
                        trackDataList.add(trackData);
                    }
                    myAdapter = new MyAdapter(getView().getContext(), trackDataList);
                    listView.setAdapter(myAdapter);
                    ItemClick();
                    break;
                case 2:
                    //出错
                    Toast.makeText(getActivity(), "查询失败!!!",Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

    static class ViewHolder {
        public TextView textView_list_start_address, textView_list_end_address,
                textView_list_start_time, textView_list_end_time;
    }

    public class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<TrackData> trackDataList;

        public MyAdapter(Context context, List<TrackData> trackDataList) {
            super();
            this.context = context;
            this.trackDataList = trackDataList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return trackDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TrackData trackData = trackDataList.get(position);
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.track_list, null);
                viewHolder.textView_list_start_address = (TextView) convertView.findViewById(R.id.textView_list_satart_address);
                viewHolder.textView_list_end_address = (TextView) convertView.findViewById(R.id.textView_list_end_address);
                viewHolder.textView_list_start_time = (TextView) convertView.findViewById(R.id.textView_list_satart_time);
                viewHolder.textView_list_end_time = (TextView) convertView.findViewById(R.id.textView_list_end_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView_list_start_address.setText(trackData.getStart_address());
            viewHolder.textView_list_end_address.setText(trackData.getEnd_address());
            viewHolder.textView_list_start_time.setText(trackData.getStart_time());
            viewHolder.textView_list_end_time.setText(trackData.getEnd_time());
            Log.d(TAG, trackData.getStart_address());
            return convertView;
        }
    }
}