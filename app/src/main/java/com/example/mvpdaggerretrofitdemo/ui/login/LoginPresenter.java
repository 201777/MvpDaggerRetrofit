package com.example.mvpdaggerretrofitdemo.ui.login;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.mvpdaggerretrofitdemo.MyApplication;
import com.example.mvpdaggerretrofitdemo.base.BaseModel;
import com.example.mvpdaggerretrofitdemo.bean.LoginResult;
import com.example.mvpdaggerretrofitdemo.http.Api;
import com.example.mvpdaggerretrofitdemo.http.ExceptionHandle;
import com.example.mvpdaggerretrofitdemo.progress.ObserverResponseListener;
import com.example.mvpdaggerretrofitdemo.utils.LogUtil;
import com.example.mvpdaggerretrofitdemo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by hxd_dary on 2019/1/9.
 */

public class LoginPresenter extends LoginContract.LoginContractPresenter {

    private LoginModel<Object> loginModel;
    @Inject
    Activity activity;
    @Inject
    Api api;


    @Inject
    public LoginPresenter(){
        loginModel = new LoginModel<>();
    }


    @Override
    public void login(String phone, String code) {
        loginModel.login(api,activity,phone, code, mView.bindLifecycle(), new ObserverResponseListener<Object>() {
            @Override
            public void onNext(Object o) {
                // 判断view是否已经被销毁
                if (mView != null) {
                    mView.loginResult((String)o);
                }
            }

            @Override
            public void onError(ExceptionHandle e) {
                if (mView != null) {
                    ToastUtil.showLongToast(e.message);
                }
            }
        });

    }

}
