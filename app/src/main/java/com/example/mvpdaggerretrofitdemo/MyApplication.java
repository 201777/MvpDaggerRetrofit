package com.example.mvpdaggerretrofitdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by hxd_dary on 2019/1/8.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    /**
     * 获取全局上下文对象
     *
     * @return mContext
     */
    public static MyApplication getContext(){
        return myApplication;
    }

}
