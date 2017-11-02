package com.allen.rxhttputils.bean;

import java.util.List;

/**
 * Created by allen on 2017/6/23.
 */

public class HeroListBean {


    /**
     * total_pages : 26
     * version : 2016-04-12
     * limit : 5
     * total : 130
     * data : [{"en_name":"Annie","name":"安妮 黑暗之女","img":"","money":"2000.0","newmoney":"1000","newhero":"false","coin":"4800.0","id":"1"},{"en_name":"Olaf","name":"奥拉夫 狂战士","img":"","money":"1500.0","newmoney":"750","newhero":"false","coin":"3150.0","id":"2"},{"en_name":"Galio","name":"加里奥 哨兵之殇","img":"","money":"2000.0","newmoney":"","newhero":"false","coin":"3150.0","id":"3"},{"en_name":"TwistedFate","name":"崔斯特 卡牌大师","img":"","money":"3000.0","newmoney":"1500","newhero":"false","coin":"4800.0","id":"4"},{"en_name":"XinZhao","name":"赵信 德邦总管","img":"","money":"2500.0","newmoney":"","newhero":"false","coin":"3150.0","id":"5"}]
     * page : 1
     */

    private int total_pages;
    private String version;
    private int limit;
    private int total;
    private int page;
    private List<DataBean> data;

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * en_name : Annie
         * name : 安妮 黑暗之女
         * img :
         * money : 2000.0
         * newmoney : 1000
         * newhero : false
         * coin : 4800.0
         * id : 1
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
