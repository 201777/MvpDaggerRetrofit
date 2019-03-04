package com.example.mvpdaggerretrofitdemo.http;

import android.net.ParseException;
import android.text.TextUtils;

import com.example.mvpdaggerretrofitdemo.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 失败处理类：1.异常2.错误
 * 1.异常：请求异常，解析数据出错，网络异常等等
 * 2.错误：某一次请求逻辑错误，（例如：登录错误）
 */
public class ExceptionHandle {
    /**
     * 错误请求:
     * 因发送的请求语法错误,服务器无法正常读取
     */
    private static final int BAD_REQUEST = 400;
    /**
     * 未授权:
     * 需要身份验证后才能获取所请求的内容,类似于403错误.不同点是.401错误后,只要正确输入帐号密码,验证即可通过
     */
    private static final int UNAUTHORIZED = 401;
    /**
     * 禁止访问:
     * 客户端没有权利访问所请求内容,服务器拒绝本次请求
     */
    private static final int FORBIDDEN = 403;
    /**
     * 未找到:
     * 服务器找不到所请求的资源.由于经常发生此种情况,所以该状态码在上网时是非常常见的
     */
    private static final int NOT_FOUND = 404;
    /**
     * 该请求使用的方法被服务器端禁止使用,RFC2616中规定, GET 和 HEAD 方法不能被禁止
     */
    private static final int METHOD_NOT_ALLOWED = 405;
    /**
     * 请求超时:
     * 客户端没有在服务器预备等待的时间内完成一个请求的发送.这意味着服务器将会切断和客户端的连接
     * 在其他浏览器中,这种响应更常见一些, 例如Chrome 和 IE9, 目的是为了使用HTTP 预连机制加快浏览速度
     * 同时注意,一些服务器不发送此种响应就直接切断连接
     */
    private static final int REQUEST_TIMEOUT = 408;
    /**
     * 内部服务器错误:
     * 服务器遇到未知的无法解决的问题
     */
    private static final int INTERNAL_SERVER_ERROR = 500;
    /**
     * 未实现:
     * 服务器不支持该请求中使用的方法,比如POST 和 PUT.只有GET 和 HEAD 是RFC2616规范中规定服务器必须实现的方法
     */
    private static final int IMPLEMENTED = 501;
    /**
     * 网关错误:
     * 服务器作为网关且从上游服务器获取到了一个无效的HTTP响应
     */
    private static final int BAD_GATEWAY = 502;
    /**
     * 服务不可用:
     * 由于临时的服务器维护或者过载,服务器当前无法处理请求.这个状况是临时的,并且将在一段时间以后恢复
     * 如果能够预计延迟时间,那么响应中可以包含一个Retry-After:头用以标明这个延迟时间
     * 如果没有给出这个Retry-After:信息，那么客户端应当以处理500响应的方式处理它.同时,这种情况下,
     * 一个友好的用于解释服务器出现问题的页面应当被返回,并且,缓存相关的HTTP头信息也应该包含,因为通常这种错误提示网页不应当被客户端缓存
     */
    private static final int SERVICE_UNAVAILABLE = 503;
    /**
     * 网关超时:
     * 服务器作为网关且不能从上游服务器及时的得到响应返回给客户端
     */
    private static final int GATEWAY_TIMEOUT = 504;
    /**
     * HTTP版本不受支持:
     * 服务器不支持客户端发送的HTTP请求中所使用的HTTP协议版本
     */
    private static final int HTTP_VERSION_NOT_SUPPORTED = 505;
    private static final String TAG = "ExceptionHandle+++++++++++++";
    public int statusCode;
    public String message;

    public ExceptionHandle(String message) {
        this.message = message;
    }

    public ExceptionHandle(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public static ExceptionHandle handleException(Throwable e) {
        ExceptionHandle exceptionHandle = null;
        if (e instanceof UnknownHostException) {
            exceptionHandle = new ExceptionHandle("请打开网络");
            return exceptionHandle;
        } else if (e instanceof ConnectException) {
            exceptionHandle = new ExceptionHandle("连接失败");
            return exceptionHandle;
        } else if (e instanceof SocketTimeoutException) {
            exceptionHandle = new ExceptionHandle("请求超时");
            return exceptionHandle;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            exceptionHandle = new ExceptionHandle("解析错误");
            return exceptionHandle;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            exceptionHandle = new ExceptionHandle("证书验证失败");
            return exceptionHandle;
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ResponseBody responseBody = httpException.response().errorBody();
            if (responseBody != null) {
                try {
                    //经测试表明，responseBody.string()的值只能获取一次，获取一次之后再获取都为空
                    String json = responseBody.string();//第一次获取就保存下来
                    LogUtil.d(TAG,json);
//                    LogUtil.d(TAG,responseBody.string()+"1");
//                    LogUtil.d(TAG,responseBody.string()+"2");
                    exceptionHandle = new Gson().fromJson(json, ExceptionHandle.class);
                    switch (httpException.code()) {
                       /* case BAD_REQUEST:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;*/
                        case UNAUTHORIZED:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message, httpException.response().code());
                            break;
                        /*case FORBIDDEN:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case NOT_FOUND:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case METHOD_NOT_ALLOWED:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case REQUEST_TIMEOUT:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case INTERNAL_SERVER_ERROR:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case IMPLEMENTED:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case BAD_GATEWAY:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case SERVICE_UNAVAILABLE:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case GATEWAY_TIMEOUT:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;
                        case HTTP_VERSION_NOT_SUPPORTED:
                            exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            break;*/
                        // 其它均视为网络错误
                        default:
                            if (TextUtils.isEmpty(exceptionHandle.message)){
                                exceptionHandle = new ExceptionHandle("服务器异常");
                            }else {
                                exceptionHandle = new ExceptionHandle(exceptionHandle.message);
                            }
                            break;
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    exceptionHandle = new ExceptionHandle("服务器异常");
                }
            }
            return exceptionHandle;
        } else {
            exceptionHandle = new ExceptionHandle("未知错误");
            return exceptionHandle;
        }
    }
}

