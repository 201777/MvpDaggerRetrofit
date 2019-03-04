package com.example.mvpdaggerretrofitdemo.http;

import com.example.mvpdaggerretrofitdemo.bean.LoginResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hxd_dary on 2019/1/11.
 */

public interface Api {

    /**
     * 模拟登陆接口
     */
    @GET(".")
    Observable<String> getLoginCall();

    /**
     * 模拟获取数据接口
     */
    @GET(".")
    Observable<String> getData();



}
