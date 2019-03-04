package com.example.mvpdaggerretrofitdemo.ui.main;

import android.app.Activity;

import com.example.mvpdaggerretrofitdemo.base.BaseModel;
import com.example.mvpdaggerretrofitdemo.http.Api;
import com.example.mvpdaggerretrofitdemo.progress.ObserverResponseListener;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * Created by hxd_dary on 2019/3/4.
 */

class MainModel <T> extends BaseModel<T> {

    void getData(Api api, Activity activity,ObservableTransformer<T, T> transformer, ObserverResponseListener<T> observerListener) {
        subscribe(activity,(Observable<T>) api.getData(), observerListener, transformer);
    }

}