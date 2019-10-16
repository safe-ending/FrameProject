package coremodel.net3.interceptor;


import android.util.Log;

import coremodel.net3.util.NetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cc.duduhuo.applicationtoast.AppToast;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangtao on 19/1/21.
 * <p>
 * 设置请求头
 * 离线读取本地缓存，在线获取最新数据(读取单个请求的请求头，亦可统一设置)
 * <p>
 * 此处要更换成对应app的请求头
 */
public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oringin = chain.request();

        //这里就是说判读我们的网络条件，要是有网络的话我么就直接获取网络上面的数据，要是没有网络的话我么就去缓存里面取数据
        if (!NetUtils.isNetConnected(AppToast.getApplication())) {
            oringin = oringin.newBuilder()
                    //无网络,检查30天内的缓存,即使是过期的缓存
                    .cacheControl(new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(30, TimeUnit.DAYS)
                            .build())
//                    .header("token", UserDao.getInstance(Utils.getContext()).getUser() != null ? UserDao.getInstance(Utils.getContext()).getUser().token : "")
                    .build();
            Log.d("CacheInterceptor", "no network");
        } else {
            //网络连接下的请求头   //有网络,检查5秒内的缓存
            oringin = oringin.newBuilder()
                    .cacheControl(new CacheControl
                            .Builder()
                            .maxAge(5, TimeUnit.SECONDS)
                            .build())
//                    .header("token", UserDao.getInstance(Utils.getContext()).getUser() != null ? UserDao.getInstance(Utils.getContext()).getUser().token : "")
                    .build();
        }

        //下面接着配置缓存处理
        return  chain.proceed(oringin).newBuilder().build();


    }


}

