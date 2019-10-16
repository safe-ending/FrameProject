package coremodel.net3.callback;

import android.content.Context;
import android.view.View;

import com.at.arouter.coremodel.R;
import com.kingja.loadsir.callback.Callback;


/**
 * Description:
 * Create Time:2019/1/24 10:22
 * Author:yangtao
 */

public class HttpCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_http;
    }


    @Override
    protected boolean onRetry(Context context, View view) {
        return true;
    }
}
