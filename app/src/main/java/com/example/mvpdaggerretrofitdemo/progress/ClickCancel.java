package com.example.mvpdaggerretrofitdemo.progress;

public class ClickCancel {

    private static boolean clickCancel;
    private static ClickCancel instance = null;
    private ProgressCancelListener mProgressCancelListener;

    private ClickCancel(ProgressCancelListener mProgressCancelListener) {
        this.mProgressCancelListener = mProgressCancelListener;
    }

    ClickCancel(ProgressCancelListener mProgressCancelListener, boolean cancelable) {
        this.mProgressCancelListener = mProgressCancelListener;
        clickCancel = cancelable;
        instance = new ClickCancel(mProgressCancelListener);
    }

    public static void setClickCancel(boolean cancelable) {
        clickCancel = cancelable;
        instance.clickCancel();
    }

    void clickCancel() {
        if (clickCancel) {
            if (mProgressCancelListener != null) {
                mProgressCancelListener.onCancelProgress();
            }
        }
    }

    /**
     * 在当前activity类调用setClickCancel()方法后，一定要在当前activity类的onDestroy方法中此方法
     */
    public static void destroy() {
        if (instance != null) {
            instance = null;
        }
    }
}