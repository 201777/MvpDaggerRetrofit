package com.example.mvpdaggerretrofitdemo.ui.main;


import android.app.Activity;

import com.example.mvpdaggerretrofitdemo.http.Api;
import com.example.mvpdaggerretrofitdemo.http.ExceptionHandle;
import com.example.mvpdaggerretrofitdemo.progress.ObserverResponseListener;
import com.example.mvpdaggerretrofitdemo.utils.ToastUtil;

import javax.inject.Inject;

/**
 * Created by hxd_dary on 2019/3/4.
 */

public class MainPresenter extends MainContract.MainContractPresenter {

    private final MainModel<Object> mainModel;
    private final Activity activity;

    @Inject
    Api api;

    @Inject
    public MainPresenter(Activity activity){
        mainModel = new MainModel<>();
        this.activity = activity;
    }

    @Override
    void getData() {
        mainModel.getData(api, activity, mView.bindLifecycle(), new ObserverResponseListener<Object>() {
            @Override
            public void onNext(Object o) {
                mView.getDataResult((String) o);
            }

            @Override
            public void onError(ExceptionHandle e) {
                ToastUtil.showLongToast(e.message);
            }
        });
    }
}
