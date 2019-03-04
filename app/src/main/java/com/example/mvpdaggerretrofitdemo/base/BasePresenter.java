package com.example.mvpdaggerretrofitdemo.base;

/**
 * Created by hxd_dary on 2019/1/9.
 */

public class BasePresenter<T> {

    public T mView;
    /**
     * 获取XXXContractView接口的实现
     * @param contractView  实现了XXXContractView接口的XXXActivity实例
     */
    void attachView(T contractView){
        mView = contractView;
    }

}
