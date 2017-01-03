package com.allen.rxhttputils.api;


import com.allen.library.base.BaseResponse;
import com.allen.rxhttputils.bean.Banner;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by allen on 2016/12/26.
 *
 */

public interface ApiService {

    @GET("/banners")
    Observable<Banner> getBanner();

    @POST("/upload")
    Observable<BaseResponse> upLoadImg(@Body String jsonString);
}
