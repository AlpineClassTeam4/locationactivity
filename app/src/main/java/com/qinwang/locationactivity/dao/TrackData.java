package com.qinwang.locationactivity.dao;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * @Auther:haoyanwang1121@gmail.com
 * @Date:2020/8/31
 * @Description:com.qinwang.locationactivity.model
 * @Version:1.0
 * @function:单次行驶记录数据
 */
public class TrackData {

    private String start_time;
    private String end_time;
    private String start_address;
    private String end_address;

    private List<LatLng> latLngs;

    public TrackData(String start_time, String end_time,
                     String start_address, String end_address,
                     List<LatLng> latLngs){
        this.start_time = start_time;
        this.end_time = end_time;
        this.start_address = start_address;
        this.end_address = end_address;
        this.latLngs = latLngs;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }
}
