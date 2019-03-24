package com.allen.rxhttputils.api;

import com.allen.library.RxHttpUtils;
import com.allen.rxhttputils.url.AppUrlConfig;

/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2019/03/23
 *      desc    : demo这里创建ApiHelper是为了方便管理不同的baseUrl，开发者可根据自己代码风格去写即可
 *
 *      多baseUrl的底层API的底层代码如下，RxHttpUtils只是为通用方法又包装了一层而已
 *      如果RxHttpUtils现有方式不能满足你，完全可以使用如下方式自定义自己的配置信息
 *
 *      RxHttpUtils.createApi("baseUrlKey1", "baseUrlValue1", XXXApi1.class);
 *      这两种写法作用相同
 *      ApiFactory.getInstance().createApi("baseUrlKey1", "baseUrlValue1", XXXApi1.class);
 *      ApiFactory.getInstance().createApi("baseUrlKey2", "baseUrlValue2", XXXApi2.class);
 *      ApiFactory.getInstance().createApi("baseUrlKey3", "baseUrlValue3", XXXApi3.class);
 *
 *      如果项目中只有一个baseUrl除了上边方式之外可以使用封装好的方法
 *      ApiFactory.getInstance().createApi(XXXApi.class);
 * </pre>
 */
public class ApiHelper {

    /**
     * 豆瓣url相关接口
     * 注意：DouBanApi里边是baseUrl为豆瓣的所有请求,这样写就可以为不同的baseUrl创建不同的retrofit对象
     * 可以为不同baseUrl设置不同的配置属性
     *
     * @return
     */
    public static DouBanApi getDouBanApi() {
        return RxHttpUtils.createApi(AppUrlConfig.DOUBAN_KEY, AppUrlConfig.DOUBAN_URL, DouBanApi.class);
    }

    /**
     * 玩安卓url相关接口
     * 注意：WanAndroidApi里边是baseUrl为玩安卓的所有请求,这样写就可以为不同的baseUrl创建不同的retrofit对象
     *
     * @return
     */
    public static WanAndroidApi getWanAndroidApi() {
        return RxHttpUtils.createApi(AppUrlConfig.WANANDROID_KET, AppUrlConfig.WANANDROID_URL, WanAndroidApi.class);
    }


    /**
     * 其他开放的api接口
     * 注意：OtherOpenApi里边是baseUrl为其他开放接口的所有请求,这样写就可以为不同的baseUrl创建不同的retrofit对象
     *
     * @return
     */
    public static OtherOpenApi getOtherOpenApi() {
        return RxHttpUtils.createApi(AppUrlConfig.OTHER_OPEN_KEY, AppUrlConfig.OTHER_OPEN_URL, OtherOpenApi.class);
    }
}
