package coremodel.net3.err;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * 错误code判断类
 * <p>
 * 遇到新的再添加
 * <p>
 * 针对直接跑出OnError的错误码
 */
public class ExceptionUtils {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int TOO_LARGE = 413;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static Exception handleException(Throwable e) {
        ResponseExcetion responseExcetion = null;
        String ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex = "登录过期,请重新登录";  //失效
                    responseExcetion = new ResponseExcetion(ex,CodeUtil.TOKEN_FAIL);
                    break;
                case TOO_LARGE:
                    ex = "图片过大，请压缩后重试";  //压缩
                    responseExcetion = new ResponseExcetion(ex,CodeUtil.LARGE_IMAGE);
                    break;

                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex = "网络不稳定，请稍后重试。";  //均视为网络错误
                    responseExcetion = new ResponseExcetion(ex,CodeUtil.NET_FAIL);
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = "解析异常";            //均视为解析错误
            responseExcetion = new ResponseExcetion(ex,CodeUtil.PRASE);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            ex = "网络不稳定，请稍后重试。";  //均视为网络错误
            responseExcetion = new ResponseExcetion(ex,CodeUtil.NET_FAIL);
        } else if (e instanceof SSLHandshakeException) {
            ex = "证书验证异常";  //证书验证异常
            responseExcetion = new ResponseExcetion(ex,CodeUtil.ERR_SSL);
        } else if (e instanceof ConnectTimeoutException || e instanceof java.net.SocketTimeoutException) {
            ex = "网络不稳定，请稍后重试。";//请求超时
            responseExcetion = new ResponseExcetion(ex,CodeUtil.NET_FAIL);
        } else {
            ex = e.getMessage();         //未知错误或服务器返回
            responseExcetion = new ResponseExcetion(ex,CodeUtil.ERROR);
        }
        return new Exception(responseExcetion.msg,new Throwable(responseExcetion.code+""));
    }

    /**
     * 约定异常  可结合后台情况约定所有code
     */
    public class CodeUtil {
        /**
         * 网络问题
         */
        public static final int NET_FAIL = 400;

        /**
         * 登陆失效
         */
        public static final int TOKEN_FAIL = 401;

        /**
         * 图片太大- 一般不会出现
         */
        public static final int LARGE_IMAGE = 413;

        /**
         * 空数据
         */
        public static final int EMPTY = -1;
        /**
         * 解析- 一般不会出现
         */
        public static final int PRASE = -2;
        /**
         * 证书- 一般不会出现
         */
        public static final int ERR_SSL = -3;

        /**
         * 服务器错误
         */
        public static final int ERROR = 500;

        /**
         * 请求结果为state = 200,code = 1 中服务器返回的提示
         */
        public static final int MSG = 1000;
    }
}