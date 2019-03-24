package com.allen.rxhttputils.api;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2019/03/23
 *      desc    :
 * </pre>
 */
public interface OtherOpenApi {

    @GET("jokes/list/random")
    Observable<String> getJokesRandom();
}
