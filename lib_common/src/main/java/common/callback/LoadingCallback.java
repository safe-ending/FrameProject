package common.callback;

import com.at.arouter.common.R;
import com.kingja.loadsir.callback.Callback;


/**
 * Description:
仅当这个工程使用  默认加载中状态
 */

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

}
