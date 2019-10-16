//package coremodel.viewmodel;
//
//import android.app.Application;
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.arch.core.util.Function;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.Transformations;
//
//import coremodel.net3.callback.EmptyCallback;
//import coremodel.net3.callback.ErrorCallback;
//import coremodel.net3.callback.HttpCallback;
//import coremodel.net3.callback.ObserverCallback;
//import coremodel.net3.callback.TokenCallback;
//import coremodel.net3.err.ExceptionUtils;
//import coremodel.net3.err.RxHelper;
//import coremodel.net3.err.RxjavaFactory;
//import coremodel.net3.model.RequestResult;
//import coremodel.net3.util.NetUtils;
//import coremodel.net3.util.TAGUtils;
//import coremodel.net3.callback.BaseObserverResult;
//import coremodel.viewmodel.base.BaseViewModel;
//import com.kingja.loadsir.core.LoadService;
//
//import cc.duduhuo.applicationtoast.AppToast;
//import io.reactivex.Observable;
//
///**
// * desc:  基本请求封装ViewModel
// * author:  yangtao
// * <p>
// * creat:  2019/1/11 16:37
// *
// * T 为请求结果中的实体类  此类处理普通请求  列表请求请调用ListViewModel
// */
//
//public class CommonViewModel<T> extends BaseViewModel<T> {
//
//    public CommonViewModel(@NonNull Application application) {
//        super(application);
//    }
//
//
//    public void request(Context context,
//                        LoadService loadService,
//                        Observable<RequestResult<T>> observable,
//                        ObserverCallback<T> observer) {
//        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
//        Transformations.switchMap(NetUtils.netConnected(context), new Function<Boolean, LiveData<T>>() {
//            @Override
//            public LiveData<T> apply(Boolean isNetConnected) {
//                Log.i(TAGUtils.LOG_TAG, "当前apply模块->" + context.getPackageName() + "");
//                MutableLiveData<T> applyData = new MutableLiveData<>();
//                if (!isNetConnected) {
//                    if (loadService != null) {
//                        loadService.showCallback(HttpCallback.class);
//                    }
//                    observer.failure(null);
//                    AppToast.showToast("网络不稳定，请稍后重试。");
//                    return applyData; //网络未连接返回空
//                }
//
//                observable
//                        //验证正确结果以及线程调度
//                        .compose(RxHelper.handleResult())
//                        // 获取异常并进行判断，优雅的返回
//                        .onErrorResumeNext(new RxjavaFactory.HttpResponseFunc<>())
//                        //监听结果
//                        .subscribe(new BaseObserverResult<T>() {
//                            @Override
//                            public void onNext(T value) {
//                                Log.i(TAGUtils.LOG_TAG, "结果返回对象类-》" + value.getClass().getSimpleName() + "");
//                                //这里将会执行下面的onChanged()方法通知界面更新
//                                applyData.setValue(value);
//                                if (loadService != null) {
//                                    loadService.showSuccess();
//                                }
//
//                            }
//
//                            @Override
//                            public void onErr(Throwable t) {
//                                Log.i(TAGUtils.LOG_TAG, t.getMessage() + "");
//                                //请求页面传递过来的状态页  若不为空  展示对应状态页
//                                if (loadService != null) {
//                                    if (t.getCause().getMessage().equals(String.valueOf(ExceptionUtils.CodeUtil.EMPTY))) {
//                                        loadService.showCallback(EmptyCallback.class);
//                                    } else if (t.getCause().getMessage().equals(String.valueOf(ExceptionUtils.CodeUtil.ERROR))) {
//                                        loadService.showCallback(HttpCallback.class);
//                                    } else if (t.getCause().getMessage().equals(String.valueOf(ExceptionUtils.CodeUtil.NET_FAIL))) {
//                                        loadService.showCallback(HttpCallback.class);
//                                    } else if (t.getCause().getMessage().equals(String.valueOf(ExceptionUtils.CodeUtil.TOKEN_FAIL))) {
//                                        loadService.showCallback(TokenCallback.class);
//                                    } else if (t.getCause().getMessage().equals(String.valueOf(ExceptionUtils.CodeUtil.MSG))) {
//                                        loadService.showCallback(ErrorCallback.class);
//                                    }
//                                }
//                                observer.failure(t);
//                            }
//                        });
//                return applyData;
//            }
//        }).observe((LifecycleOwner) context, new Observer<T>() {
//            @Override
//            public void onChanged(@Nullable T object) {
//                //请求结果
//                Log.i(TAGUtils.LOG_TAG, "位置-》"+context.getClass().getSimpleName() + "通知请求成功");
//                observer.success(object);
//            }
//        });
//
//
//    }
//
//}
