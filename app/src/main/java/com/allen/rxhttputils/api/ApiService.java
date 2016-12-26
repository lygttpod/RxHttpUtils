package com.allen.rxhttputils.api;


import com.allen.rxhttputils.bean.Banner;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by allen on 2016/12/26.
 *
 */

public interface ApiService {

    @GET("/banners")
    Observable<Banner> getBanner();
}
