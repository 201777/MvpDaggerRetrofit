package com.example.mvpdaggerretrofitdemo.base;

import io.reactivex.ObservableTransformer;

/**
 * Created by hxd_dary on 2019/1/14.
 */

public interface BaseView {
    /**
     * 绑定完成Observable发布的事件和当前组件绑定，实现生命周期同步，从而实现当前组件生命周期结束时，自动取消对Observable订阅
     *
     * @param <T> Object对象
     * @return ObservableTransformer
     */
    <T> ObservableTransformer<T, T> bindLifecycle();
}
