package com.example.mvpdaggerretrofitdemo.progress;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;


import java.lang.ref.WeakReference;

/**
 * Dialog的进度控制
 */
public class ProgressDialogHandler extends Handler {

    static final int SHOW_PROGRESS_DIALOG = 1;
    static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;
    private WeakReference<Context> mWeakReference;

    ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener, boolean cancelable) {
        super();
        this.mWeakReference = new WeakReference<>(context);
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
            default:
                break;
        }
    }

    private void initProgressDialog() {
        if (pd == null) {
            pd = new ProgressDialog(mWeakReference.get());
            String message = "请稍候";
            pd.showProgress(message);
            pd.setCancelable(cancelable);
            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }
}