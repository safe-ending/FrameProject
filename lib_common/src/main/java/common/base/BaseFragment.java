package common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import com.apkfuns.logutils.LogUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import common.callback.LoadingCallback;

public abstract class BaseFragment<VB extends ViewDataBinding> extends Fragment {
    protected String TAG = this.getClass().getSimpleName();
    protected Activity mActivity;// mActivity替代getActivity方法
    protected VB mBinding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 解决应用（内存）重启时getActivity()空指针
        this.mActivity = (Activity) context;
    }

    //* 是否第一次加载
    protected boolean mIsFirstLoad = true;

    /**
     * 标志位，View已经初始化完成。
     * 用isAdded()属性代替
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean mIsPrepared;

    //* 是否可见状态
    protected boolean mIsVisible;

    //* 是否已经加载数据
    protected boolean mIsLoadData;

    //* 容器是否为ViewPager
    protected boolean mIsViewPager;

    /**
     * 容器为ViewPager，点击按钮显示不相邻的Fragment会重走onActivityCreated方法
     * 从而执行lazyLoad()的logicBusiness()afterViews()加载数据
     * 执行过程：
     * setUserVisibleHint()->afterViews()->onActivityCreated()->lazyLoad()->logicBusiness()->afterViews()
     * <p>
     * 是否显示旧数据：logicBusiness()初始化全局数据(ArrayList||Adapter(内含有数据源))其一非空判断
     * <p>
     * afterViews()：
     * 刷新数据无控件特效(需在logicBusiness()初始化)：略过 (拦截afterViews()执行2次)；
     * 刷新数据有控件特效(需在logicBusiness()初始化)：子类修改 mVPFragmentDrawCompletion 为负int最大值，
     * ----第一次afterViews()会因重走logicBusiness()而取消，执行第二次afterViews()
     */
    protected int mVPFragmentDrawCompletion;

    @LayoutRes
    protected abstract int contentLayout();

    //* 加载数据
    protected abstract void afterViews();

    //* 初始化数据
    protected abstract void beforeViews();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mIsFirstLoad = false;
        View rootView = getLayoutInflater().inflate(contentLayout(), null, false);
        mBinding = DataBindingUtil.bind(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, "onActivityCreated");
        if (mIsViewPager) mVPFragmentDrawCompletion += 1;
        mIsPrepared = true;
        lazyLoad();
    }

    public LoadService loadService;

    public void setLoadSuccessView(View view) {
        loadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                // 重新加载逻辑
                reload();
            }
        });
        loadService.showCallback(LoadingCallback.class);
    }

    /**
     * 与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d(TAG, "setUser......" + isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.d(TAG, "show and hide");
        if (!hidden) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见时调用
     */
    protected void onVisible() {
        LogUtils.d(TAG, "可见时调用 : onVisible");
//        lazyLoad();
    }

    /**
     * 原fragment开启新activity，remove后返回当前的fragment执行
     */
    @Override
    public void onStart() {
        super.onStart();
        // case : mIsViewPager = true
        if (!mIsFirstLoad && !mIsFirstLoad && !mIsVisible) return;
        if (!mIsLoadData) {
            LogUtils.d(TAG, "执行onStart -- > 懒加载");
            lazyLoad();
        }
    }

    //弹出错误界面时点击重新加载
    public abstract void reload();

    /**
     * 再次调用可见方法
     * //再次修改此方法的时候,注意主界面的线程调度
     */
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    //再次修改此方法的时候,注意主界面的线程调度
    @Override
    public void onPause() {
        //可见时先设置界面的将不可见,可避免fragment生命周期的混乱
        if (getUserVisibleHint()) {
            onInvisible();
        }
        super.onPause();
        LogUtils.d(TAG, "不可见 -- onPause");

        mIsLoadData = false;
    }

    /**
     * 不可见时调用
     */
    protected void onInvisible() {
        LogUtils.d(TAG, "不可见时调用 : onInvisible");
        mIsLoadData = false;
    }

    /**
     * 可见时调用(懒加载)
     */
    protected void lazyLoad() {

        LogUtils.d(TAG,
                "mIsFirstLoad : " + mIsFirstLoad +
                        " mIsPrepared : " + mIsPrepared +
                        " mIsVisible : " + mIsVisible);

        if (mIsViewPager && !mIsVisible) {
            mIsViewPager = false;
        } else if (mIsFirstLoad && mIsVisible && !mIsPrepared) {
            LogUtils.d(TAG, "略过......");
        } else if (!mIsFirstLoad && mIsPrepared) {
            LogUtils.d(TAG, "先初始化，再懒加载");
            beforeViews();
            mIsFirstLoad = false;
            mIsPrepared = false;
            if (!mIsViewPager || mVPFragmentDrawCompletion <= 1) {
                LogUtils.d(TAG, "执行懒加载");
                afterViews();

            }
            mIsLoadData = true;
        } else {
            LogUtils.d(TAG, "直接执行懒加载");
            afterViews();
            mIsLoadData = true;
        }

    }

}
