package com.example.mvpdaggerretrofitdemo.di.module;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.mvpdaggerretrofitdemo.ConStant;
import com.example.mvpdaggerretrofitdemo.MyApplication;
import com.example.mvpdaggerretrofitdemo.http.Api;
import com.example.mvpdaggerretrofitdemo.utils.LogUtil;
import com.example.mvpdaggerretrofitdemo.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by hxd_dary on 2019/1/10.
 */

@Module
public class HttpModule {
    /**
     * 连接时长，单位：秒
     */
    private static final int CONNECT_TIME_OUT = 12;
    /**
     * 读超时长，单位：秒
     */
    private static final int READ_TIME_OUT = 60;
    /**
     * 写超时长，单位：秒
     */
    private static final int WRITE_TIME_OUT = 60;
    /**
     * 设缓存有效期为两天
     */
    private static final int CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            // 拦截器获取请求
            Request request = chain.request();
            // 服务器的缓存策略
            String cacheControl = request.cacheControl().toString();
            // 断网时配置缓存策略
            if (!NetWorkUtil.isNetConnected(MyApplication.getContext())) {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            // 在线缓存
            if (NetWorkUtil.isNetConnected(MyApplication.getContext())) {
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        // 应用服务端配置的缓存策略
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                //离线缓存
                /*
                 * only-if-cached:(仅为请求标头)
                 *　 请求:告知缓存者,我希望内容来自缓存，我并不关心被缓存响应,是否是新鲜的.
                 * max-stale:
                 *　 请求:意思是,我允许缓存者，发送一个,过期不超过指定秒数的,陈旧的缓存.
                 *　 响应:同上.
                 * max-age:
                 *   请求:强制响应缓存者，根据该值,校验新鲜性.即与自身的Age值,与请求时间做比较.如果超出max-age值,
                 *   则强制去服务器端验证.以确保返回一个新鲜的响应.
                 *   响应:同上.
                 */
                //需要服务端配合处理缓存请求头，不然会抛出： HTTP 504 Unsatisfiable Request (only-if-cached)
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .build();
            }
        }
    };

    private MyApplication application;


    public HttpModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    MyApplication provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Api providesApi(Retrofit retrofit){
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(Retrofit.Builder builder,OkHttpClient client){
        return builder.client(client)
                .baseUrl(ConStant.baseUrl)
                // 请求结果转换为基本类型，一般为String
                .addConverterFactory(ScalarsConverterFactory.create())
                // 请求的结果转为实体类
                .addConverterFactory(GsonConverterFactory.create())
                //适配RxJava2.0
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 无缓存策略的Retrofit
     * @return Retrofit的实例
     */
   /* @Provides
    @Singleton
    Retrofit providesSimpleRetrofit(Retrofit.Builder builder,OkHttpClient.Builder okHttpClientBuilder){
        return builder.baseUrl(ConStant.baseUrl)
                .client(getOkHttpClient(okHttpClientBuilder))
                // 请求结果转换为基本类型，一般为String
                .addConverterFactory(ScalarsConverterFactory.create())
                // 请求的结果转为实体类
                .addConverterFactory(GsonConverterFactory.create())
                // 适配RxJava2.0
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }*/

    @Provides
    @Singleton
    Retrofit.Builder providesRetrofitBuilder(){
        return new Retrofit.Builder();
    }

    @Provides
    @Singleton
    OkHttpClient provideClient(OkHttpClient.Builder builder) {
        // 缓存
        File cacheFile = new File(MyApplication.getContext().getCacheDir(), "cache");
        // 100Mb
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        // 增加头部信息
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        // 设置允许请求json数据
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(build);
            }
        };
        return builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                // 没网的情况下
                .addInterceptor(mRewriteCacheControlInterceptor)
                // 有网的情况下
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(getLevel())
                .cache(cache)
                .build();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    private OkHttpClient getOkHttpClient(OkHttpClient.Builder httpClientBuilder) {
        // 定制OkHttp
        // OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(getLevel());
        httpClientBuilder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
        return httpClientBuilder.build();
    }

    private HttpLoggingInterceptor getLevel() {
        // 日志级别分为4类：NONE、BASIC、HEADERS、BODY；NONE无；BASIC请求/响应行；HEADERS请求/响应行+头；BODY请求/响应行+头+体
        // 日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        // 新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                LogUtil.v("message", "OkHttp====Message:" + message);
            }
        });
        return loggingInterceptor.setLevel(level);
    }

}
