package com.qinwang.locationactivity.ui.track.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qinwang.locationactivity.dao.TrackData;

public class MyViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final String TAG = "RouteViewModel";

    private MutableLiveData<TrackData> latLng = new MutableLiveData<>();

    public void setLatLng(TrackData trackDataList){

        latLng.setValue(trackDataList);
        Log.d(TAG, String.valueOf(latLng.getValue()));
    }

    public MutableLiveData<TrackData> getLatLng(){
        return latLng;
    }

}