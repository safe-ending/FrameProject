package coremodel.net3.callback;

import com.at.arouter.coremodel.R;
import com.kingja.loadsir.callback.Callback;


/**
 * Description:
 * Create Time:2019/1/24 10:22
 * Author:yangtao
 */

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

}
