package com.example.mvpdaggerretrofitdemo.di.component;

import android.app.Activity;

import com.example.mvpdaggerretrofitdemo.MyApplication;
import com.example.mvpdaggerretrofitdemo.di.module.ActivityModule;
import com.example.mvpdaggerretrofitdemo.di.module.HttpModule;
import com.example.mvpdaggerretrofitdemo.http.Api;
import com.example.mvpdaggerretrofitdemo.ui.login.LoginActivity;
import com.example.mvpdaggerretrofitdemo.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hxd_dary on 2019/1/9.
 */

@Singleton
@Component(modules = {ActivityModule.class,HttpModule.class})
public interface ActivityComponent {
    //不需要暴露方法
/*
    Activity getActivity();
    MyApplication getMyApplication();
    Api getApi();*/

    void inject(LoginActivity activity);
    void inject(MainActivity activity);

}
