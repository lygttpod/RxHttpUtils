package com.allen.rxhttputils.api;


import com.allen.library.bean.BaseData;
import com.allen.rxhttputils.bean.BookBean;
import com.allen.rxhttputils.bean.Top250Bean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by allen on 2016/12/26.
 */

public interface ApiService {

    @GET("v2/book/1220562")
    Observable<BookBean> getBook();

    @GET("v2/movie/top250")
    Observable<Top250Bean> getTop250(@Query("count") int count);

    @GET("v2/book/1220562")
    Observable<String> getBookString();

    //以下是post请求的两种方式demo示例

//    /**
//     * post提交json数据 demo
//     * @param map 键值对
//     * @return
//     */
//    @POST("xxx")
//    Observable<BaseData<T>> getData(@Body Map map);
//
//    /**
//     * post提交表单 demo
//     * @param name
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("xxx")
//    Observable<BaseData<T>> getData(@Field("name") String name);
//

}
