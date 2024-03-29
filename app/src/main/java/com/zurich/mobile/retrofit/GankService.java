package com.ahyaobin.mobile.retrofit;


import com.ahyaobin.mobile.entity.GankResult;
import com.ahyaobin.mobile.net.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Retrofit service
 * Created by weixinfei on 16/5/27.
 */
public interface GankService {
    @GET("api/data/{type}/{count}/{page}")
    Observable<HttpResult<List<GankResult>>> getGankInfo(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );
}
