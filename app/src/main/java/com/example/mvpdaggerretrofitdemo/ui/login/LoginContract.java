package com.example.mvpdaggerretrofitdemo.ui.login;

import com.example.mvpdaggerretrofitdemo.base.BasePresenter;
import com.example.mvpdaggerretrofitdemo.base.BaseView;

/**
 * Created by hxd_dary on 2019/1/9.
 */

public interface LoginContract {

    interface LoginContractView extends BaseView{
        void loginResult(String data);

    }

    abstract class LoginContractPresenter extends BasePresenter<LoginContractView>{
        abstract void login(String phone, String code);
    }

}
