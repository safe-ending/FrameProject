package common.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.afollestad.materialdialogs.MaterialDialog;
import com.at.arouter.common.R;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import common.callback.DataCallBack;
import common.callback.LoadingCallback;
import common.widget.toolbar.ZhigeToolbar;


/**
 * <p>Activity基类 </p>
 *
 * @name BaseActivity
 */
public abstract class BaseActivity<VB extends ViewDataBinding> extends BaseCompatActivity {

    private static final String TAG = BaseActivity.class.getName();
    protected Activity mActivity;
    private ZhigeToolbar zhigeToolbar;
    protected VB mBinding;


    @LayoutRes
    protected abstract int contentLayout();

    protected abstract void beforeViews();

    protected abstract void afterViews();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        beforeViews();
        View rootView = getLayoutInflater().inflate(contentLayout(), null, false);
        try {
            mBinding = DataBindingUtil.bind(rootView);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.setContentView(rootView);
        zhigeToolbar = findViewById(R.id.zhigeToolbar);
        if (zhigeToolbar != null) {
            zhigeToolbar.setIvLeftClickListener(l -> onBackPressed());
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod();
            }
        });
        afterViews();

    }

    protected ZhigeToolbar getZhigeToolbar() {
        return zhigeToolbar;
    }

    /**
     * 重写 getResource 方法，防止系统字体影响
     */
    @Override
    public Resources getResources() {//禁止app字体大小跟随系统字体大小调节
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }


    public LoadService loadService;

    public void setLoadSuccessView(View view, DataCallBack callBack) {
        loadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                // 重新加载逻辑
                callBack.reload();
            }
        });
        loadService.showCallback(LoadingCallback.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }


    public void showLoadingDialog() {
        showLoadingDialog(getString(R.string.loadtext));
    }

    MaterialDialog dialog;

    public void showLoadingDialog(String msg) {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(mActivity)
                    .content(msg)
                    .progress(true, 100)
                    .cancelable(false)
                    .build();
        } else {
            dialog.setContent(msg);
        }
        dialog.show();
    }

    public void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }


}
