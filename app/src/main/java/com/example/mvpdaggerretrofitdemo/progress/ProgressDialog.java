package com.example.mvpdaggerretrofitdemo.progress;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.example.mvpdaggerretrofitdemo.R;


/**
 * 自定义progressDialog对话框，用于提示用户当前操作正在运行，让用户等待.
 */
public class ProgressDialog extends Dialog {

    public ProgressDialog(@NonNull Context context) {
        super(context, R.style.progress_dialog);
    }

    public void showProgress(String message) {
        // 自定义的layout样式
        setContentView(R.layout.dialog_progress);
        // 设置不可通过点击外面区域取消
        setCanceledOnTouchOutside(false);
        TextView textView = findViewById(R.id.progress_textView);
        textView.setText(message);
    }
}