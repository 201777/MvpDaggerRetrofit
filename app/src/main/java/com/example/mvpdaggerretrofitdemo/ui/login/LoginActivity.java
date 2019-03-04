package com.example.mvpdaggerretrofitdemo.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mvpdaggerretrofitdemo.MyApplication;
import com.example.mvpdaggerretrofitdemo.R;
import com.example.mvpdaggerretrofitdemo.base.BaseActivity;
import com.example.mvpdaggerretrofitdemo.di.component.DaggerActivityComponent;
import com.example.mvpdaggerretrofitdemo.di.module.ActivityModule;
import com.example.mvpdaggerretrofitdemo.di.module.HttpModule;
import com.example.mvpdaggerretrofitdemo.ui.main.MainActivity;
import com.example.mvpdaggerretrofitdemo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableTransformer;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.LoginContractView {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {

    }

    @OnClick({R.id.btn_get_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:


                break;
            case R.id.btn_login:
                String phone = etPhone.getText().toString();
                String code = etCode.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    ToastUtil.showLongToast("电话不能为空");
                }else if (TextUtils.isEmpty(code)){
                    ToastUtil.showLongToast("验证码不能为空");
                }else {
                    mPresenter.login(phone,code);
                }

                break;
        }
    }

    @Override
    public void loginResult(String loginResult) {
        ToastUtil.showLongToast("登录成功");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("loginResult",loginResult);
        startActivity(intent);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindLifecycle() {
        return bindToLifecycle();//测试
    }
}
