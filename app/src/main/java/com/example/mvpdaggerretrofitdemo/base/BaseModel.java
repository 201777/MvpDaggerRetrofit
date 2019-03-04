package com.example.mvpdaggerretrofitdemo.base;

import android.app.Activity;


import com.example.mvpdaggerretrofitdemo.progress.ObserverResponseListener;
import com.example.mvpdaggerretrofitdemo.progress.ProgressObserver;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于封装线程管理和订阅的过程，也可以封装数据库的操作
 */
public class BaseModel<T> {

    /**
     * 封装观察者订阅
     *
     * @param observable  被观察者的对象
     * @param listener    请求监听的对象
     * @param transformer transformer
     */
    public void subscribe(Activity activity, final Observable<T> observable, ObserverResponseListener<T> listener, ObservableTransformer<T, T> transformer) {
        subscribe(activity, observable, listener, transformer, true, true);
    }

    /**
     * 封装观察者订阅
     *
     * @param observable  被观察者的对象
     * @param listener    请求监听的对象
     * @param transformer transformer
     * @param isDialog    是否显示dialog
     * @param cancelable  是否取消dialog
     */
    public void subscribe(Activity activity, final Observable<T> observable, ObserverResponseListener<T> listener, ObservableTransformer<T, T> transformer, boolean isDialog, boolean cancelable) {
        final Observer<T> observer = new ProgressObserver<>(activity,listener, isDialog, cancelable);
        observable.compose(transformer)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}