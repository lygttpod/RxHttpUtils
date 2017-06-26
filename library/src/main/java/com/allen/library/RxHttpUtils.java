package com.allen.library;

import com.allen.library.constant.SPKeys;
import com.allen.library.download.DownloadRetrofit;
import com.allen.library.http.GlobalRxHttp;
import com.allen.library.http.SingleRxHttp;
import com.allen.library.upload.UploadRetrofit;
import com.allen.library.utils.SPUtils;


import java.util.HashSet;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by allen on 2017/6/22.
 * 网络请求
 */

public class RxHttpUtils {
    private static RxHttpUtils instance;

    public static RxHttpUtils getInstance() {
        if (instance == null) {
            synchronized (RxHttpUtils.class) {
                if (instance == null) {
                    instance = new RxHttpUtils();
                }
            }

        }
        return instance;
    }

    public GlobalRxHttp config() {
        return GlobalRxHttp.getInstance();
    }


    /**
     * 使用全局参数创建请求
     *
     * @param cls
     * @param <K>
     * @return
     */
    public static <K> K createApi(Class<K> cls) {
        return GlobalRxHttp.createGApi(cls);
    }

    /**
     * 获取单个请求配置实例
     *
     * @return
     */
    public static SingleRxHttp getSInstance() {

        return SingleRxHttp.getInstance();
    }


    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    public static Observable<ResponseBody> downloadFile(String fileUrl) {
        return DownloadRetrofit.downloadFile(fileUrl);
    }

    /**
     * 上传单张图片
     *
     * @param uploadUrl
     * @param filePath
     * @return
     */
    public static Observable<ResponseBody> uploadImg(String uploadUrl, String filePath) {
        return UploadRetrofit.uploadImg(uploadUrl, filePath);

    }

    /**
     * 获取Cookie
     *
     * @return
     */
    public static HashSet<String> getCookie() {
        HashSet<String> preferences = (HashSet<String>) SPUtils.get(SPKeys.COOKIE, new HashSet<String>());
        return preferences;
    }

}
