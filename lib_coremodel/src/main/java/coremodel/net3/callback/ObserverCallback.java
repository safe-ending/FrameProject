package coremodel.net3.callback;

/**
 * desc:  监听结果
 *
 * author:  yangtao
 * <p>
 * creat:  2019/1/17 11:41
 */

public interface ObserverCallback<T> {
    void success(T t);
    void failure(Throwable e);
}
