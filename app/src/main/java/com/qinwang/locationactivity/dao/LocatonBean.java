package com.qinwang.locationactivity.dao;

import java.util.List;

/**
 * @Auther:haoyanwang1121@gmail.com
 * @Date:2020/9/2
 * @Description:com.qinwang.locationactivity.dao
 * @Version:1.0
 * @function:
 */
public class LocatonBean {

    private int status;
    private String msg;
    private List<Track> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Track> getData() {
        return data;
    }

    public void setData(List<Track> data) {
        this.data = data;
    }

    public class Track{
        private String start_time;
        private String end_time;
        private String start_address;
        private String end_address;
        private String points;

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

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }

}
