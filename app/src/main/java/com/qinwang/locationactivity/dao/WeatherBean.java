package com.qinwang.locationactivity.dao;

import java.util.List;

/**
 * @Auther:haoyanwang1121@gmail.com
 * @Date:2020/8/24
 * @Description:com.qinwang.locationactivity.model
 * @Version:1.0
 * @function:
 */
public class WeatherBean {

    /**
     * data : {"yesterday":{"date":"6日星期三","high":"高温 19℃","fx":"东北风","low":"低温 11℃","fl":"<![CDATA[4-5级]]>","type":"晴"},"city":"大连","aqi":"58","forecast":[{"date":"7日星期四","high":"高温 18℃","fengli":"<![CDATA[5-6级]]>","low":"低温 10℃","fengxiang":"东南风","type":"多云"},{"date":"8日星期五","high":"高温 14℃","fengli":"<![CDATA[6-7级]]>","low":"低温 10℃","fengxiang":"东风","type":"小到中雨"},{"date":"9日星期六","high":"高温 17℃","fengli":"<![CDATA[5-6级]]>","low":"低温 11℃","fengxiang":"东北风","type":"多云"},{"date":"10日星期天","high":"高温 20℃","fengli":"<![CDATA[5-6级]]>","low":"低温 12℃","fengxiang":"西北风","type":"晴"},{"date":"11日星期一","high":"高温 22℃","fengli":"<![CDATA[5-6级]]>","low":"低温 13℃","fengxiang":"西南风","type":"晴"}],"ganmao":"天冷风大且空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。","wendu":"13"}
     * status : 1000
     * desc : OK
     */

    private DataBean data;
    private int status;
    private String desc;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static class DataBean {
        /**
         * yesterday : {"date":"6日星期三","high":"高温 19℃","fx":"东北风","low":"低温 11℃","fl":"<![CDATA[4-5级]]>","type":"晴"}
         * city : 大连
         * aqi : 58
         * forecast : [{"date":"7日星期四","high":"高温 18℃","fengli":"<![CDATA[5-6级]]>","low":"低温 10℃","fengxiang":"东南风","type":"多云"},
         * {"date":"8日星期五","high":"高温 14℃","fengli":"<![CDATA[6-7级]]>","low":"低温 10℃","fengxiang":"东风","type":"小到中雨"},
         * {"date":"9日星期六","high":"高温 17℃","fengli":"<![CDATA[5-6级]]>","low":"低温 11℃","fengxiang":"东北风","type":"多云"},
         * {"date":"10日星期天","high":"高温 20℃","fengli":"<![CDATA[5-6级]]>","low":"低温 12℃","fengxiang":"西北风","type":"晴"},
         * {"date":"11日星期一","high":"高温 22℃","fengli":"<![CDATA[5-6级]]>","low":"低温 13℃","fengxiang":"西南风","type":"晴"}]
         * ganmao : 天冷风大且空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。
         * wendu : 13
         */

        private YesterdayBean yesterday;
        private String city;
        private String aqi;
        private String ganmao;
        private String wendu;
        private List<ForecastBean> forecast;

        public YesterdayBean getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayBean yesterday) {
            this.yesterday = yesterday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public List<ForecastBean> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastBean> forecast) {
            this.forecast = forecast;
        }

        public static class YesterdayBean {
            /**
             * date : 6日星期三
             * high : 高温 19℃
             * fx : 东北风
             * low : 低温 11℃
             * fl : <![CDATA[4-5级]]>
             * type : 晴
             */

            private String date;
            private String high;
            private String fx;
            private String low;
            private String fl;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class ForecastBean {
            /**
             * date : 7日星期四
             * high : 高温 18℃
             * fengli : <![CDATA[5-6级]]>
             * low : 低温 10℃
             * fengxiang : 东南风
             * type : 多云
             */

            private String date;
            private String high;
            private String fengli;
            private String low;
            private String fengxiang;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFengli() {
                return fengli;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFengxiang() {
                return fengxiang;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
