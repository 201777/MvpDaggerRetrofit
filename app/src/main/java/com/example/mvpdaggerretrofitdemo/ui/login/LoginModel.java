package com.example.mvpdaggerretrofitdemo.ui.login;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.mvpdaggerretrofitdemo.MyApplication;
import com.example.mvpdaggerretrofitdemo.base.BaseModel;
import com.example.mvpdaggerretrofitdemo.http.Api;
import com.example.mvpdaggerretrofitdemo.progress.ObserverResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import okhttp3.MediaType;
import okhttp3.RequestBody;


@SuppressWarnings("unchecked")
class LoginModel<T> extends BaseModel<T> {

    void login(Api api,Activity activity,String mobilePhone, String captcha,
               ObservableTransformer<T, T> transformer, ObserverResponseListener<T> observerListener) {
//        JSONObject result = new JSONObject();
        /*try {
            result.put("mobilePhone", mobilePhone);
            result.put("captcha", captcha);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
//        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), result.toString());
        subscribe(activity,(Observable<T>) api.getLoginCall(), observerListener, transformer);
    }

}