package coremodel.net3.err;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * desc:  错误结果的拦截器
 * author:  yangtao
 * <p>
 * creat:  2019/1/17 16:34
 */

public class RxjavaFactory {

    public static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            //将返回的结果先进行转化为具体的错误（是登录失效还是500）
            return Observable.error(ExceptionUtils.handleException(throwable));
        }
    }


}