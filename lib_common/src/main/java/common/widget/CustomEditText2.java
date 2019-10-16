package common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.apkfuns.logutils.LogUtils;
import com.at.arouter.common.R;

import common.util.AndroidUtil;


/**
 * 创建者     yangtao
 * 创建时间   2018/9/4 16:34
 * 描述	      输入框切换显示隐藏  右边的发送验证码按钮
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class CustomEditText2 extends LinearLayout {
    private boolean isDisplayLabel;//是否显示上面的label
    private boolean isDisplayRightBtn;//是否显示右边蓝色按钮
    private boolean isDisplayLine;//是否显示底部线
    private boolean isDisplayRightImg;//是否显示右边图片
    private int maxLength;//输入框限定内容长度
    private int textSize;//文字大小
    private int textHintColor;//文字颜色
    private int textColor;//文字颜色
    private String mHintText;//提示的文字
    private String mInputContent = "";//默认输入的文字
    private boolean singleLine;//单行
    private String digst = "";//校验
    private String inputType;
    private boolean showLeftTitle;
    private String label = "";//标题

    private EditText mEtContent;//输入框
    private Context context;
    private TextView mRightBtn;//右边的点击事件
    private OnClickListener onRightBtnClickListener;
    private LinearLayout mRightImg;//右边的图片点击事件
    private ImageView ivEye;//右边的图片
    private View viewLine;
    private TextView tvLabel;//标题

    public CustomEditText2(Context context) {
        this(context, null);
    }

    public CustomEditText2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
        //传入activity的context
        this.context = context;
        //获取资源类型Array
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText2);
        isDisplayLabel = ta.getBoolean(R.styleable.CustomEditText2_display_top_label, false);
        isDisplayRightImg = ta.getBoolean(R.styleable.CustomEditText2_display_right_icon2, true);
        isDisplayRightBtn = ta.getBoolean(R.styleable.CustomEditText2_display_right_btn2, false);
        isDisplayLine = ta.getBoolean(R.styleable.CustomEditText2_display_bootom_line, false);
        maxLength = ta.getInteger(R.styleable.CustomEditText2_maxLength2, 30);
        textSize = ta.getDimensionPixelSize(R.styleable.CustomEditText2_text_size2, AndroidUtil.px2dp(17));
        textHintColor = ta.getColor(R.styleable.CustomEditText2_text_hint_color, ContextCompat.getColor(context, R.color.bg_999999));
        textColor = ta.getColor(R.styleable.CustomEditText2_text_color, ContextCompat.getColor(context, R.color.color_333333));
        mHintText = ta.getString(R.styleable.CustomEditText2_hint_text);
        mInputContent = ta.getString(R.styleable.CustomEditText2_text_content);
        singleLine = ta.getBoolean(R.styleable.CustomEditText2_text_singlle, true);
        digst = ta.getString(R.styleable.CustomEditText2_text_digst);
        inputType = ta.getString(R.styleable.CustomEditText2_text_inputtype);
        showLeftTitle = ta.getBoolean(R.styleable.CustomEditText2_show_left_title, false);

        label = ta.getString(R.styleable.CustomEditText2_label_content);
        ta.recycle();
        initView();
        setView();
    }


    public void setView() {
        if (isDisplayRightImg) {

            mRightImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEtContent.setText("");
                    mEtContent.setSelection(0);
                }
            });

        }

//        if (isDisplayLine) {
//            mEtContent.setOnFocusChangeListener(new OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        viewLine.setBackgroundResource(R.drawable.sl_et_line);
//                    } else {
//                        viewLine.setBackgroundResource(R.drawable.sl_et_line_gray);
//                    }
//                }
//            });
//        }
    }

    private void initView() {
        View view;

        view = LayoutInflater.from(context).inflate(R.layout.item_input_view2, this);
        mEtContent = view.findViewById(R.id.ed_content);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRightImg.setVisibility(isDisplayRightImg && !TextUtils.isEmpty(mEtContent.getText().toString()) ? VISIBLE : GONE);

            }
        });
        mRightBtn = view.findViewById(R.id.gain_verification_code);
        mRightImg = view.findViewById(R.id.gain_verification_code_img);
        tvLabel = view.findViewById(R.id.tvLabel);
        ivEye = view.findViewById(R.id.ivEye);

        initEditText();
        //右按钮展示
        mRightBtn.setVisibility(isDisplayRightBtn ? VISIBLE : GONE);
        mRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDisplayRightBtn) {
                    if (onRightBtnClickListener != null)
                        onRightBtnClickListener.onClick(v);
                }
            }
        });

        tvLabel.setText(TextUtils.isEmpty(label) ? "" : label + "");
        tvLabel.setVisibility(isDisplayLabel && !TextUtils.isEmpty(tvLabel.getText().toString()) ? VISIBLE : GONE);

        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});//限定长度
        mEtContent.setText(TextUtils.isEmpty(mInputContent) ? "" : mInputContent);
        mEtContent.setSelection(TextUtils.isEmpty(mInputContent) ? 0 : mInputContent.length());
        mEtContent.setHint(mHintText + "");
        mEtContent.setTextColor(textColor);
        mEtContent.setHintTextColor(textHintColor);
        mEtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mEtContent.setSingleLine(singleLine);


        if (!TextUtils.isEmpty(inputType) && inputType.equals("textPassword")) {//密码输入有眼睛时
            mEtContent.setTransformationMethod(PasswordTransformationMethod.getInstance());//从密码可见模式变为密码不可见模式
        }

        setDigst();

        viewLine = view.findViewById(R.id.viewLine);
    }

    void setDigst() {
        if (!TextUtils.isEmpty(digst)) {

            if (digst.equals(getResources().getString(R.string.letter1)) ||
                    digst.equals(getResources().getString(R.string.letter4))) {
                mEtContent.setKeyListener(new DigitsKeyListener() {
                    @Override
                    protected char[] getAcceptedChars() {
                        return digst.toCharArray();
                    }

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_CLASS_NUMBER;
                    }
                });
            } else if (digst.equals(getResources().getString(R.string.letter2)) ||
                    digst.equals(getResources().getString(R.string.letter3))) {
                mEtContent.setKeyListener(new DigitsKeyListener() {
                    @Override
                    protected char[] getAcceptedChars() {
                        return digst.toCharArray();
                    }

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                    }
                });
            } else {
                mEtContent.setRawInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
    }
    public EditText getEditext() {
        return mEtContent;
    }

    public void setTvLabel(String label) {
        if (TextUtils.isEmpty(label)) {
            tvLabel.setVisibility(GONE);
            return;
        }
        tvLabel.setVisibility(VISIBLE);
        tvLabel.setText(label);
    }

    public TextView getSendCodeView() {
        return mRightBtn;
    }

    private int getInputType(int inputType) {
        switch (inputType) {
            case 0:
                LogUtils.w(getClass().getName(), "text");
                return InputType.TYPE_CLASS_TEXT;
            case 1:
                LogUtils.w(getClass().getName(), "number");
                return InputType.TYPE_CLASS_NUMBER;
            case 2:
                LogUtils.w(getClass().getName(), "phone");
                return InputType.TYPE_CLASS_PHONE;
            case 5:
                LogUtils.w(getClass().getName(), "num_and_point");
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            default:
                break;
        }
        return 0;
    }


    public CustomEditText2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setText(String content) {
        try {
            mEtContent.setText(content + "");
            mEtContent.setSelection(TextUtils.isEmpty(content) ? 0 : content.length());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLetter(String letter) {
        digst = letter;
        setDigst();
    }

    public synchronized String getText() {
        return mEtContent.getText().toString();
    }

    /**
     * 监听右按钮点击事件
     *
     * @param listener
     */
    public void setOnRightBtnClickListener(OnClickListener listener) {
        onRightBtnClickListener = listener;
    }


    public void setHintText(String mHintText) {
        if (mEtContent != null)
            mEtContent.setHint(mHintText);//设置提示
    }

    public void setRightContent(String str) {
        if (mRightBtn != null)
            mRightBtn.setText(str + "");
    }

    public void showRightbtn(boolean displayRightbtn) {
        mRightBtn.setVisibility(displayRightbtn ? VISIBLE : GONE);
        isDisplayRightBtn = displayRightbtn;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mEtContent.getText());
    }

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;

    // 初始化edittext 控件
    private void initEditText() {
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    cursorPos = mEtContent.getSelectionEnd();
                    // 这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText = s.toString();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                            if (containsEmoji(input.toString())) {
                                resetText = true;
                                Toast.makeText(context, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                setText(inputAfterText);
                                CharSequence text = getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
}
