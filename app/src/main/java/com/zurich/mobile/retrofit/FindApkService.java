package com.ahyaobin.mobile.retrofit;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by weixinfei on 16/6/23.
 */
public interface FindApkService {
    @FormUrlEncoded
    @POST("market/api")
    Call<JSONObject> getApk(@FieldMap Map<String, String> map);
}
