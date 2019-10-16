package com.frameproject;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import cc.duduhuo.applicationtoast.AppToast;
import common.base.BaseApplication;
import common.data.APIHostManager;
import common.data.AppPref;
import common.util.MyClassicsFooter;
import common.util.MyClassicsHeader;
import common.util.Utils;
import common.util.crash.CrashHandlerException;
import coremodel.net3.callback.EmptyCallback;
import coremodel.net3.callback.ErrorCallback;
import coremodel.net3.callback.HttpCallback;
import coremodel.net3.callback.LoadingCallback;
import coremodel.net3.callback.TokenCallback;


/**
 * Created by yangtao on 2018/1/13.
 **
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 * @name BaseApplication
 *
 * 组件moudle/application中的所有引用和初始化都需要加入进来
 */

public class App extends BaseApplication {

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new MyClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new MyClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);

        //third module
        // 初始化AppToast库
        AppToast.init(this);

        //配置测试人员的测试环境
        try {
            if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                String host = new AppPref(this, AppPref.APP_PREF).getHost();
                APIHostManager.Common_Url = host.split(",")[0];
                APIHostManager.IM_Url = host.split(",")[1];
                APIHostManager.Game_Url = host.split(",")[2];

                CrashHandlerException exception = CrashHandlerException.getInstance();
                exception.initCrashHandlerException(getApplicationContext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new common.callback.LoadingCallback())
                .addCallback(new HttpCallback())
                .addCallback(new TokenCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
    }
}
