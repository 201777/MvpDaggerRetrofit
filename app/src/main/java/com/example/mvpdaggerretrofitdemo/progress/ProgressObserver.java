package com.example.mvpdaggerretrofitdemo.progress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import com.example.mvpdaggerretrofitdemo.http.ExceptionHandle;
import com.example.mvpdaggerretrofitdemo.ui.login.LoginActivity;
import com.example.mvpdaggerretrofitdemo.utils.LogUtil;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 观察者，继承与Observer，监听请求是否成功
 */
public class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {
    private static final String TAG = "ProgressObserver____ ";
    private ObserverResponseListener<T> listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable d;
    private ClickCancel clickCancel;
    private Activity context;

    public ProgressObserver(Activity activity, ObserverResponseListener<T> listener, boolean isDialog, boolean cancelable) {
        this.listener = listener;
        context = activity;
        if (isDialog) {
            mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable);
        } else {
            if (!cancelable) {
                clickCancel = new ClickCancel(this, false);
            }
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        showProgressDialog();
        onClickCancel();
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void onClickCancel() {
        if (clickCancel != null) {
            clickCancel.clickCancel();
        }
    }

    @Override
    public void onNext(T t) {
        if (listener != null) {
            listener.onNext(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.v(TAG, e.toString());
        dismissProgressDialog();
        // 自定义异常处理
        ExceptionHandle exceptionHandle = ExceptionHandle.handleException(e);
        listener.onError(exceptionHandle);
        // 未授权，跳转到登录界面
        Integer unauthorized = 401;
        if (unauthorized.equals(exceptionHandle.statusCode)) {
            String loginActivity = "LoginActivity";
            String splashActivity = "SplashActivity";
            // 如果当前类不是LoginActivity类或是SplashActivity类，就跳转到LoginActivity界面
            if (context != null) {
                if (!loginActivity.equals(context.getClass().getSimpleName())) {
                    if (!splashActivity.equals(context.getClass().getSimpleName())) {
                        Activity activity = (Activity) context;
                        Intent intent = new Intent();
                        intent.setClass(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
            }
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        // 如果处于订阅状态，则取消订阅
        if (d != null) {
            if (!d.isDisposed()) {
                LogUtil.v("==========", "已取消");
                d.dispose();
            }
        }
    }
}