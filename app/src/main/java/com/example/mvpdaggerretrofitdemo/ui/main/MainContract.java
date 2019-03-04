package com.example.mvpdaggerretrofitdemo.ui.main;

import com.example.mvpdaggerretrofitdemo.base.BasePresenter;
import com.example.mvpdaggerretrofitdemo.base.BaseView;
import com.example.mvpdaggerretrofitdemo.ui.login.LoginContract;

/**
 * Created by hxd_dary on 2019/3/4.
 */

public interface MainContract {
    interface MainContractView extends BaseView {
        void getDataResult(String data);
    }

    abstract class MainContractPresenter extends BasePresenter<MainContract.MainContractView> {
        abstract void getData();
    }
}
