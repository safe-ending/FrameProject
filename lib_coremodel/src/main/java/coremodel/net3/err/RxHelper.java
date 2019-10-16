package coremodel.net3.err;


import androidx.annotation.NonNull;

import coremodel.net3.model.RequestResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 对接口返回states = 200的处理，code不为1时转到failure否则执行success
 *
 *
 */
public class RxHelper {
    /**
     * 普通请求拦截处理
     * @param <T> 返回的实体类
     * @return
     */
    //处理请求结果 针对 结果中有code message data 的json
    //对请求状态吗进行分析，如果成功获取result 中的data
    public static <T> ObservableTransformer<RequestResult<T>, T> handleResult() {
        return upstream -> upstream.flatMap(requestResult -> {
            //为1时  并且data结果不为null  才视为正确结果
            if (requestResult.code == RequestResult.CODE_REQUEST_SUCCESS) {
                if (requestResult.Data == null) {
                    //此处要对null做处理不然会抛出异常 --》onNext called with null. Null values are generally not allowed in 2.x operators and sources.
                    return createSuccessData((T) "");
                }
                return createSuccessData(requestResult.Data);
            } else {
                //非 code = 1 情况弹出提示
                Exception e = new Exception(requestResult.info, new Throwable(ExceptionUtils.CodeUtil.MSG + ""));
                return Observable.error(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 列表请求拦截处理
     * @param <T> 返回的实体类
     * @return
     */
    //处理请求结果 针对 有code message data 的json
    //对请求状态吗进行分析，如果成功获取result 中的data
    public static <T> ObservableTransformer<RequestResult<T>, T> handleResultList() {
        return upstream -> upstream.flatMap(requestResult -> {
            //正确结果
            if (requestResult.code == RequestResult.CODE_REQUEST_SUCCESS) {
                if (requestResult.Data == null) {
                    //此处要对null做处理不然会抛出异常 --》onNext called with null. Null values are generally not allowed in 2.x operators and sources.
                    return createSuccessData((T) "");
                }
                if ((requestResult.Data instanceof ArrayList && ((ArrayList) requestResult.Data).size() == 0)
                        || (requestResult.Data instanceof List && ((List) requestResult.Data).size() == 0)) {
                    Exception e = new Exception("列表为空", new Throwable(ExceptionUtils.CodeUtil.EMPTY + ""));
                    return Observable.error(e);
                }
                return createSuccessData(requestResult.Data);
            } else {
                //非 code = 1 情况弹出提示
                Exception e = new Exception(requestResult.info, new Throwable(ExceptionUtils.CodeUtil.MSG + ""));
                return Observable.error(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 返回正确的实体类结果
     * @param t 实体类模型
     * @param <T> 实体类模型
     * @return 实体类模型
     */
    //创建成功返回的数据
    private static <T> Observable<T> createSuccessData(T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
                try {
                    //通知页面更新
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception ex) {
                    //若出现解析等错误或其他异常 弹出提示
                    emitter.onError(ex);
                }
            }
        });
    }


}

