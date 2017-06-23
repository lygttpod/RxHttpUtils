package com.allen.rxhttputils.bean;

import com.allen.library.base.BaseResponse;

import java.util.List;

/**
 * Created by allen on 2017/6/23.
 */

public class FreeHeroBean extends BaseResponse {

    /**
     * start_time : 2016-04-22 00:00:00
     * data : [{"en_name":"Nunu","name":"努努 雪人骑士","img":"","money":"1000.0","newmoney":"","newhero":"false","coin":"450.0","id":"20"},{"en_name":"Anivia","name":"艾尼维亚 冰晶凤凰","img":"","money":"3500.0","newmoney":"1750","newhero":"false","coin":"4800.0","id":"34"},{"en_name":"DrMundo","name":"蒙多 祖安狂人","img":"","money":"2000.0","newmoney":"1000","newhero":"false","coin":"1350.0","id":"36"},{"en_name":"Kassadin","name":"卡萨丁 虚空行者","img":"","money":"2500.0","newmoney":"1250","newhero":"false","coin":"3150.0","id":"38"},{"en_name":"Irelia","name":"艾瑞莉娅 刀锋意志","img":"","money":"4000.0","newmoney":"","newhero":"true","coin":"6300.0","id":"39"},{"en_name":"Corki","name":"库奇 英勇投弹手","img":"","money":"3500.0","newmoney":"1750","newhero":"false","coin":"6300.0","id":"42"},{"en_name":"Veigar","name":"维迦 邪恶小法师","img":"","money":"2000.0","newmoney":"","newhero":"false","coin":"1350.0","id":"45"},{"en_name":"Graves","name":"格雷福斯 法外狂徒","img":"","money":"4500.0","newmoney":"","newhero":"false","coin":"6300.0","id":"104"},{"en_name":"Yasuo","name":"亚索 疾风剑豪","img":"","money":"4500.0","newmoney":"2250","newhero":"false","coin":"6300.0","id":"157"},{"en_name":"Jinx","name":"金克丝 暴走萝莉","img":"","money":"4500.0","newmoney":"2250","newhero":"false","coin":"6300.0","id":"222"}]
     * end_time : 2016-04-29 00:00:00
     */

    private String start_time;
    private String end_time;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * en_name : Nunu
         * name : 努努 雪人骑士
         * img :
         * money : 1000.0
         * newmoney :
         * newhero : false
         * coin : 450.0
         * id : 20
         */

        private String en_name;
        private String name;
        private String img;
        private String money;
        private String newmoney;
        private String newhero;
        private String coin;
        private String id;

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNewmoney() {
            return newmoney;
        }

        public void setNewmoney(String newmoney) {
            this.newmoney = newmoney;
        }

        public String getNewhero() {
            return newhero;
        }

        public void setNewhero(String newhero) {
            this.newhero = newhero;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "en_name='" + en_name + '\'' +
                    ", name='" + name + '\'' +
                    ", img='" + img + '\'' +
                    ", money='" + money + '\'' +
                    ", newmoney='" + newmoney + '\'' +
                    ", newhero='" + newhero + '\'' +
                    ", coin='" + coin + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}
