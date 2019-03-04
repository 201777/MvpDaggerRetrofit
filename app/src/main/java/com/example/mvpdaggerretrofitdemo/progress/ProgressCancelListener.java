package com.example.mvpdaggerretrofitdemo.progress;

/**
 * 请求监听取消时
 */
public interface ProgressCancelListener {
    /**
     * 取消进度
     */
    void onCancelProgress();
}