package common.widget.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.at.arouter.common.R;


public class ZhigeToolbar extends Toolbar {

    AppCompatTextView tvTitle;
    AppCompatImageView ivTitleRight;
    AppCompatImageView ivLeft;
    AppCompatTextView tvLeft;
    AppCompatImageView ivRight;
    AppCompatTextView tvRight;
    View line;

    private String mTitleStr;

    public ZhigeToolbar(Context context) {
        super(context);
    }

    public ZhigeToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(getLayoutRes(), this, true);

        setContentInsetsAbsolute(0, 0);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZhigeToolbar);
        tvTitle = findViewById(R.id.tvTitle);
        ivTitleRight  = findViewById(R.id.ivTitleRight);
        ivLeft  = findViewById(R.id.ivLeft);
        tvLeft  = findViewById(R.id.tvLeft);
        tvRight  = findViewById(R.id.tvRight);
        line  = findViewById(R.id.line);

        tvTitle.setText(typedArray.getString(R.styleable.ZhigeToolbar_toolbar_title));
        tvRight.setText(typedArray.getString(R.styleable.ZhigeToolbar_toolbar_text_right));

        ivRight = findViewById(R.id.ivRight);
    }


    protected int getLayoutRes() {
        return R.layout.toolbar_zhige;
    }

    public AppCompatTextView getTvRight() {
        return tvRight;
    }

    public AppCompatImageView getIvRight(){
        return ivRight;
    }

    public void setTvTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setTvTitle(CharSequence resId) {
        tvTitle.setText(resId);
    }

    public void setTvTitle(String strTitle) {
        tvTitle.setText(strTitle);
    }

    public void setTvLeft(int resId) {
        tvLeft.setText(resId);
    }

    public void setTvRight(int resId) {
        tvRight.setText(resId);
    }

    public void setIvLeftClickListener(OnClickListener listener) {
        ivLeft.setOnClickListener(listener);
    }

    public void setIvRight(int resId) {
        ivRight.setImageResource(resId);
    }

    public void setIvRightClickListener(OnClickListener listener) {
        ivRight.setOnClickListener(listener);
    }

    public void setTvRightClickListener(OnClickListener listener) {
        tvRight.setOnClickListener(listener);
    }


    public void setBottomLineVisible(int visible) {
        line.setVisibility(visible);
    }

    public void setShowTitleRightImage(boolean isShow) {
        ivTitleRight.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setShowTitleRightIext(boolean isShow) {
        tvRight.setVisibility(isShow ? VISIBLE : GONE);
    }
}
