package com.allen.library.manage;

import com.allen.library.RxHttpUtils;
import com.allen.library.factory.ApiFactory;

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

    /**
     * 一次性传入urlMap
     *
     * @param urlMap map
     * @return RxUrlManager
     */
    public RxUrlManager setMultipleUrl(Map<String, String> urlMap) {
        this.urlMap = urlMap;
        return this;
    }

    /**
     * 向map中添加url
     *
     * @param urlKey   key
     * @param urlValue value
     * @return RxUrlManager
     */
    public RxUrlManager addUrl(String urlKey, String urlValue) {
        urlMap.put(urlKey, urlValue);
        return this;
    }

    /**
     * 从map中删除某个url
     *
     * @param urlKey 需要删除的urlKey
     * @return RxUrlManager
     */
    public RxUrlManager removeUrlByKey(String urlKey) {
        urlMap.remove(urlKey);
        return this;
    }

    /**
     * 针对单个baseUrl切换的时候清空老baseUrl的所有信息
     *
     * @param urlValue url
     * @return RxUrlManager
     */
    public RxUrlManager setUrl(String urlValue) {
        urlMap.put(DEFAULT_URL_KEY, urlValue);
        return this;
    }

    /**
     * 获取全局唯一的baseUrl
     *
     * @return url
     */
    public String getUrl() {
        return getUrlByKey(DEFAULT_URL_KEY);
    }

    /**
     * 根据key
     *
     * @param urlKey 获取对应的url
     * @return url
     */
    public String getUrlByKey(String urlKey) {
        return urlMap.get(urlKey);
    }

    /**
     * 清空设置的url相关的所以信息
     * 相当于重置url
     * 动态切换生产测试环境时候调用
     *
     * @return RxUrlManager
     */
    public RxUrlManager clear() {
        urlMap.clear();
        ApiFactory.getInstance().clearAllApi();
        RxHttpUtils.removeAllCookie();
        return this;
    }
}
