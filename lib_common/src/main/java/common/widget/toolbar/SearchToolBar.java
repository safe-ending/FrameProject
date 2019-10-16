package common.widget.toolbar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.at.arouter.common.R;


public class SearchToolBar extends ZhigeToolbar {

    public SearchToolBar(Context context) {
        super(context);
    }

    public SearchToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toolbar_search;
    }
}
