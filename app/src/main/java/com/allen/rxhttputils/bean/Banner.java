package com.allen.rxhttputils.bean;


import com.allen.library.base.BaseResponse;

import java.util.List;

/**
 * Created by allen on 2016/11/30.
 */

public class Banner extends BaseResponse {

    /**
     * banners : [{"linkUrl":"","picUrl":"http://www.hoomxb.com/media/banners/2.jpg","title":"五层安全防护全方位资金保护"},{"linkUrl":"","picUrl":"http://www.hoomxb.com/media/banners/banner1.jpg","title":"理财红小宝，财务没烦恼"}]
     * code : 0
     */

    private List<BannersBean> banners;

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public static class BannersBean {
        /**
         * linkUrl :
         * picUrl : http://www.hoomxb.com/media/banners/2.jpg
         * title : 五层安全防护全方位资金保护
         */

        private String linkUrl;
        private String picUrl;
        private String title;

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
