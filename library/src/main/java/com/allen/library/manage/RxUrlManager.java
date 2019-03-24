package com.allen.library.manage;

import com.allen.library.RxHttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2019/03/23
 *      desc    : 多域名管理类
 * </pre>
 */
public class RxUrlManager {

    private volatile static RxUrlManager instance;

    private Map<String, String> urlMap;

    public static String DEFAULT_URL_KEY = "rx_default_url_key";

    public static RxUrlManager getInstance() {
        if (instance == null) {
            synchronized (RxUrlManager.class) {
                if (instance == null) {
                    instance = new RxUrlManager();
                }
            }
        }
        return instance;
    }

    private RxUrlManager() {
        urlMap = new HashMap<>();
    }

    public void setUrl(String urlValue) {
        urlMap.put(DEFAULT_URL_KEY, urlValue);
    }

    public void setMultipleUrl(Map<String, String> urlMap) {
        this.urlMap = urlMap;
    }

    public void addUrl(String urlKey, String urlValue) {
        urlMap.put(urlKey, urlValue);
    }

    public void removeUrl(String urlKey) {
        urlMap.remove(urlKey);
    }

    public String getUrl() {
        return getUrl(DEFAULT_URL_KEY);
    }

    public String getUrl(String urlKey) {
        return urlMap.get(urlKey);
    }

    /**
     * 动态切换生产测试环境时候调用
     */
    public void clear() {
        urlMap.clear();
        RxHttpUtils.removeAllCookie();
    }
}
