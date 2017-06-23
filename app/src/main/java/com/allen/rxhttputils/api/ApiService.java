package com.allen.rxhttputils.api;


import com.allen.rxhttputils.bean.FreeHeroBean;
import com.allen.rxhttputils.bean.HeroListBean;
import com.allen.rxhttputils.bean.Octocat;
import com.allen.rxhttputils.bean.ServerListBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by allen on 2016/12/26.
 */

public interface ApiService {

    @GET("users/octocat")
    Observable<Octocat> getOctocat();

    @GET("lol/server")
    Observable<ServerListBean> getServerList();


    @GET("lol/free")
    Observable<FreeHeroBean> getFreeHero();

    @GET("lol/heros")
    Observable<HeroListBean> getHeroList(@Query("limit") String limit);

}
