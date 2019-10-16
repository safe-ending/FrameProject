package coremodel.net3.callback;

import android.text.TextUtils;
import android.util.Log;

import cc.duduhuo.applicationtoast.AppToast;
import coremodel.net3.util.TAGUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * desc:  封装的Observer  减少代码量
 * author:  yangtao
 * <p> T 为请求结果中的result泛型对象
 * creat:  2019/1/17 15:03
 */

public abstract class BaseObserverResult<T> implements Observer<T> {


    @Override
    public void onSubscribe(Disposable d) {
        //这里可以显示进度框

    }

    @Override
    public abstract void onNext(T t);


    public abstract void onErr(Throwable t);

    @Override
    public void onError(Throwable e) {

        //这里提供外部方法用来隐藏进度框或在onComplete()中进行
        onErr(e);
    }

    @Override
    public void onComplete() {
        Log.i(TAGUtils.LOG_TAG, "请求完成-》BaseObserverResult onComplete");
        //这里用来隐藏进度框
    }
}
