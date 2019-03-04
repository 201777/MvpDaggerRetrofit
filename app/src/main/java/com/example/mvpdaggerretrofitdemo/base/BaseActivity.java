package com.example.mvpdaggerretrofitdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.mvpdaggerretrofitdemo.MyApplication;
import com.example.mvpdaggerretrofitdemo.di.component.ActivityComponent;
import com.example.mvpdaggerretrofitdemo.di.component.DaggerActivityComponent;
import com.example.mvpdaggerretrofitdemo.di.module.ActivityModule;
import com.example.mvpdaggerretrofitdemo.di.module.HttpModule;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hxd_dary on 2019/1/8.
 */

public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity {

    @Inject
    protected T mPresenter;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        initInject();//必须在mPresenter使用之前，否则mPresenter会报空指针
        //ButterKnife插件的绑定
        // TODO: add setContentView(...) invocation
        mUnbinder = ButterKnife.bind(this);
        if (mPresenter != null){
            mPresenter.attachView(this);//将实现了XXXContractView接口的XXXActivity实例传递给XXXPresenter
        }
        init();

    }

    public ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .httpModule(new HttpModule(MyApplication.getContext()))
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife插件的解绑
        mUnbinder.unbind();
    }

    /**
     * 获取布局资源id
     * @return 返回布局资源id
     */
    public abstract int getLayoutId();

    /**
     * 初始化数据
     */
    public abstract void init();

    /**
     * 初始化mPresenter
     */
    public abstract void initInject();



}
