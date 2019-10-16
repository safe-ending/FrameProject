//package coremodel.viewmodel.base;
//
//import android.app.Application;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.databinding.ObservableField;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import coremodel.net3.model.RequestResult;
//import coremodel.net3.util.TAGUtils;
//
//import java.lang.reflect.ParameterizedType;
//import io.reactivex.disposables.CompositeDisposable;
//
///**
// * Created by yangtao on 2019/1/12.
// * <p>
// * 自定义域名访问时的请求结果绑定监听
// */
//
//public class BaseViewModel<T> extends AndroidViewModel {
//
//    //生命周期观察的数据
//    public LiveData<RequestResult<T>> mLiveObservableData;
//    //UI使用可观察的数据 ObservableField是一个包装类
//    public ObservableField<T> uiObservableData = new ObservableField<>();
//
//    public final CompositeDisposable mDisposable = new CompositeDisposable();
//
//    public final MutableLiveData ABSENT = new MutableLiveData();
//
//    {
//        //以后可以在这里对缓存进行处理
//        ABSENT.setValue(null);//暂未用到
//    }
//
//
//    public BaseViewModel(@NonNull Application application) {
//        super(application);
//        Log.i(TAGUtils.LOG_TAG,"创建BaseViewModel生命周期连接");
//    }
//
//
//    /**
//     * LiveData支持了lifecycle生命周期检测
//     *
//     * @return
//     */
//    public LiveData<RequestResult<T>> getLiveObservableData() {
//        return mLiveObservableData;
//    }
//
//    /**
//     * 当主动改变数据时重新设置被观察的数据
//     *
//     * @param product
//     */
//    public void setUiObservableData(T product) {
//        this.uiObservableData.set(product);
//    }
//
//    public Class<RequestResult<T>> getTClass() {
//        Class<RequestResult<T>> tClass = (Class<RequestResult<T>>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        return tClass;
//    }
//
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        mDisposable.clear();
//        Log.i(TAGUtils.LOG_TAG,"清除BaseViewModel生命周期连接");
//    }
//}
//
