package com.example.mvpdaggerretrofitdemo.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.mvpdaggerretrofitdemo.R;
import com.example.mvpdaggerretrofitdemo.base.BaseActivity;
import com.example.mvpdaggerretrofitdemo.di.component.DaggerActivityComponent;
import com.example.mvpdaggerretrofitdemo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableTransformer;

/**
 * Created by hxd_dary on 2019/3/4.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainContractView{
    @BindView(R.id.btn_get_data)
    Button btnGetData;
    @BindView(R.id.tv_data)
    TextView tvData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        String loginResult = getIntent().getStringExtra("loginResult");
        tvData.setText(loginResult);
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick(R.id.btn_get_data)
    public void onViewClicked() {
        mPresenter.getData();
    }

    @Override
    public <T> ObservableTransformer<T, T> bindLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void getDataResult(String data) {
        ToastUtil.showLongToast("获取数据成功");
    }
}
