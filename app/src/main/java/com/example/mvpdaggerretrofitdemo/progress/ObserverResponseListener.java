package com.example.mvpdaggerretrofitdemo.progress;


import com.example.mvpdaggerretrofitdemo.http.ExceptionHandle;

/**
 * 请求监听成功或者失败
 */
public interface ObserverResponseListener<T> {
    /**
     * 响应成功
     *
     * @param t 返回T的对象
     */
    void onNext(T t);

    /**
     * 响应失败
     *
     * @param e 返回ExceptionHandle的对象
     */
    void onError(ExceptionHandle e);
}