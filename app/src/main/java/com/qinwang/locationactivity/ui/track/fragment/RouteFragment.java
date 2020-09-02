package com.qinwang.locationactivity.ui.track.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.qinwang.locationactivity.R;
import com.qinwang.locationactivity.dao.TrackData;
import com.qinwang.locationactivity.ui.track.model.MyViewModel;

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
        final List<LatLng> latLngs1 = new ArrayList<>();
        latLngs1.add(new LatLng(36.906706, 114.57386));
        latLngs1.add(new LatLng(36.907197, 114.57388));
        latLngs1.add(new LatLng(36.907587, 114.57393));
        latLngs1.add(new LatLng(36.907547, 114.57212));
        latLngs1.add(new LatLng(36.907587, 114.56944));

        TrackData trackData = new TrackData("2020/9/1-10:20:10",
                "2020/9/1-11:20:10",
                "北京市天安门",
                "北京市长城",
                latLngs1);
        trackDataList.clear();
        trackDataList.add(trackData);
        myAdapter = new MyAdapter(getView().getContext(), trackDataList);
        listView.setAdapter(myAdapter);
        ItemClick();

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