package com.fast.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.common.R;

public class TopBar extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {

    private View v;
    private TopBarBtnPressListener topBarBtnPressListener;
    private TittleLongPressListener tittleLongPressListener;
    private TextView titleTv, subtitleTv;
    private ImageView leftBtnFirstIv, leftBtnSecondIv, rightBtnFirstIv, rightBtnSecondIv;
    private TextView leftBtnTv, rightBtnTv;
    private boolean leftBtnClickable, rightBtnClickable, leftBtnFirstIvClickable, leftBtnSecondIvClickable,
            rightBtnFirstIvClickable, rightBtnSecondIvClickable;
    /**
     * 左边第一个图标按钮
     */
    public final static int LEFT_BTN_FIRST = 0;
    /**
     * 左边第二个图标按钮
     */
    public static final int LEFT_BTN_SECOND = 1;
    /**
     * 左边文本按钮
     */
    public static final int LEFT_TV_BTN = 2;
    /**
     * 右边文本按钮
     */
    public static final int RIGHT_TV_BTN = 3;
    /**
     * 右边第一个图标按钮
     */
    public static final int RIGHT_BTN_FIRST = 4;
    /**
     * 右边第二个图标按钮
     */
    public static final int RIGHT_BTN_SECOND = 5;


    public TopBar(Context context) {
        super(context);
        initFromAttributes(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFromAttributes(context, attrs);
    }


    private void initFromAttributes(Context context, AttributeSet attrs) {
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        boolean showBottomLine = ta.getBoolean(R.styleable.TopBar_switch_showBottomLine, false);
        String title = ta.getString(R.styleable.TopBar_text_barTitle);
        String subtitle = ta.getString(R.styleable.TopBar_text_barSubtitle);
        String leftBtnText = ta.getString(R.styleable.TopBar_text_leftButton);
        Drawable leftBtnFirstDrawable = ta.getDrawable(R.styleable.TopBar_image_leftFirstButton);
        Drawable leftBtnSecondDrawable = ta.getDrawable(R.styleable.TopBar_image_leftScondButton);
        String rightBtnText = ta.getString(R.styleable.TopBar_text_rightButton);
        Drawable rightBtnFirstDrawable = ta.getDrawable(R.styleable.TopBar_image_rightFirstButton);
        Drawable rightBtnSecondDrawable = ta.getDrawable(R.styleable.TopBar_image_rightScondButton);
        int bottomLineColor = ta.getColor(R.styleable.TopBar_color_bottomLine, Color.LTGRAY);
        int defaultTextColor = ta.getColor(R.styleable.TopBar_color_defaultText, Color.WHITE);
        int titleTextColor = ta.getColor(R.styleable.TopBar_color_titleText, defaultTextColor);
        int subtitleTextColor = ta.getColor(R.styleable.TopBar_color_subtitleText, defaultTextColor);
        int leftTextColor = ta.getColor(R.styleable.TopBar_color_leftText, defaultTextColor);
        int rightTextColor = ta.getColor(R.styleable.TopBar_color_rightText, defaultTextColor);
        ta.recycle();
        //加载layout
        v = LayoutInflater.from(context).inflate(R.layout.view_top_bar, this);
        if (v == null) {
            return;
        }
        //底部的线
        View lineV = v.findViewById(R.id.v_line);
        lineV.setBackgroundColor(bottomLineColor);
        lineV.setVisibility(showBottomLine ? VISIBLE : GONE);
        //标题
        titleTv = v.findViewById(R.id.tv_title);
        titleTv.setTextColor(titleTextColor);
        if (title == null) {
            titleTv.setVisibility(GONE);
        } else {
            titleTv.setVisibility(VISIBLE);
            titleTv.setText(title);
            titleTv.setOnLongClickListener(this);
        }
        //副标题
        subtitleTv = v.findViewById(R.id.tv_subtitle);
        subtitleTv.setTextColor(subtitleTextColor);
        if (subtitle == null) {
            subtitleTv.setVisibility(GONE);
        } else {
            subtitleTv.setVisibility(VISIBLE);
            subtitleTv.setText(subtitle);
        }
        //左边第一个按钮图片
        leftBtnFirstIv = v.findViewById(R.id.iv_first_btn_left);
        if (leftBtnFirstDrawable == null) {
            leftBtnFirstIv.setVisibility(GONE);
        } else {
            leftBtnFirstIvClickable = true;
            leftBtnFirstIv.setVisibility(VISIBLE);
            leftBtnFirstIv.setImageDrawable(leftBtnFirstDrawable);
        }
        //左边第二个按钮图片
        leftBtnSecondIv = v.findViewById(R.id.iv_second_btn_left);
        if (leftBtnSecondDrawable == null) {
            leftBtnSecondIv.setVisibility(GONE);
        } else {
            leftBtnSecondIvClickable = true;
            leftBtnSecondIv.setVisibility(VISIBLE);
            leftBtnSecondIv.setImageDrawable(leftBtnSecondDrawable);
        }
        //左边按钮文字
        leftBtnTv = v.findViewById(R.id.tv_btn_left);
        leftBtnTv.setTextColor(leftTextColor);
        if (leftBtnText == null) {
            leftBtnTv.setVisibility(GONE);
        } else {
            leftBtnClickable = true;
            leftBtnTv.setVisibility(VISIBLE);
            leftBtnTv.setText(leftBtnText);
        }
        //右边第一个按钮图片
        rightBtnFirstIv = v.findViewById(R.id.iv_first_btn_right);
        if (rightBtnFirstDrawable == null) {
            rightBtnFirstIv.setVisibility(GONE);
        } else {
            rightBtnFirstIvClickable = true;
            rightBtnFirstIv.setVisibility(VISIBLE);
            rightBtnFirstIv.setImageDrawable(rightBtnFirstDrawable);
        }
        //右边第二个按钮图片
        rightBtnSecondIv = v.findViewById(R.id.iv_second_btn_right);
        if (rightBtnSecondDrawable == null) {
            rightBtnSecondIv.setVisibility(GONE);
        } else {
            rightBtnSecondIvClickable = true;
            rightBtnSecondIv.setVisibility(VISIBLE);
            rightBtnSecondIv.setImageDrawable(leftBtnSecondDrawable);
        }
        //右边按钮文字
        rightBtnTv = v.findViewById(R.id.tv_btn_right);
        rightBtnTv.setTextColor(rightTextColor);
        if (rightBtnText == null) {
            rightBtnTv.setVisibility(GONE);
        } else {
            rightBtnClickable = true;
            rightBtnTv.setVisibility(VISIBLE);
            rightBtnTv.setText(rightBtnText);
        }
        //点击监听
        if (leftBtnClickable) {
            v.findViewById(R.id.tv_btn_left).setOnClickListener(this);
        }
        if (rightBtnClickable) {
            v.findViewById(R.id.tv_btn_right).setOnClickListener(this);
        }
        if (leftBtnFirstIvClickable) {
            v.findViewById(R.id.iv_first_btn_left).setOnClickListener(this);
        }
        if (leftBtnSecondIvClickable) {
            v.findViewById(R.id.iv_second_btn_left).setOnClickListener(this);
        }
        if (rightBtnFirstIvClickable) {
            v.findViewById(R.id.iv_first_btn_right).setOnClickListener(this);
        }
        if (rightBtnSecondIvClickable) {
            v.findViewById(R.id.iv_second_btn_right).setOnClickListener(this);
        }
    }

    //设置中间主标题
    public void setTitleText(String title) {
        if (title == null) {
            titleTv.setVisibility(GONE);
        } else {
            titleTv.setVisibility(VISIBLE);
            titleTv.setText(title);
            titleTv.setOnLongClickListener(this);
        }
    }

    //右侧按钮显示与文字
    public void setRightBtnText(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.rightBtnTv.setVisibility(VISIBLE);
            this.rightBtnTv.setText(text);
            this.rightBtnTv.setOnClickListener(this);
        }
    }

    //左侧按钮显示与文字
    public void setLeftBtnText(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.rightBtnTv.setVisibility(VISIBLE);
            this.rightBtnTv.setText(text);
            this.rightBtnTv.setOnClickListener(this);
        }
    }

    //左边第一个按钮图片
    public void setLeftBtnFirstIcon(@DrawableRes int res) {
        leftBtnFirstIv.setVisibility(VISIBLE);
        leftBtnFirstIv.setImageResource(res);
        leftBtnFirstIv.setOnClickListener(this);
    }

    //右边边第一个按钮图片
    public void setRightBtnFirstIcon(@DrawableRes int res) {
        rightBtnFirstIv.setVisibility(VISIBLE);
        rightBtnFirstIv.setImageResource(res);
        rightBtnFirstIv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (topBarBtnPressListener != null) {
            int id = v.getId();
            if (id == R.id.iv_first_btn_left) {
                topBarBtnPressListener.onTopBarBtnPressed(LEFT_BTN_FIRST);

            } else if (id == R.id.iv_second_btn_left) {
                topBarBtnPressListener.onTopBarBtnPressed(LEFT_BTN_SECOND);

            } else if (id == R.id.tv_btn_left) {
                topBarBtnPressListener.onTopBarBtnPressed(LEFT_TV_BTN);

            } else if (id == R.id.tv_btn_right) {
                topBarBtnPressListener.onTopBarBtnPressed(RIGHT_TV_BTN);

            } else if (id == R.id.iv_first_btn_right) {
                topBarBtnPressListener.onTopBarBtnPressed(RIGHT_BTN_FIRST);

            } else if (id == R.id.iv_second_btn_right) {
                topBarBtnPressListener.onTopBarBtnPressed(RIGHT_BTN_SECOND);

            } else {
            }
        }

    }


    /**
     * 设置左边第一个按钮显示隐藏
     */
    public void setLeftBtnFirstIv(int visibility) {
        leftBtnFirstIv.setVisibility(visibility);
    }

    public void setTopBarBtnPressListener(TopBarBtnPressListener topBarBtnPressListener) {
        this.topBarBtnPressListener = topBarBtnPressListener;
    }


    @Override
    public boolean onLongClick(View v) {
        if (tittleLongPressListener != null) {
            tittleLongPressListener.onTittlePressed();
        }
        return true;
    }


    public interface TopBarBtnPressListener {
        /**
         * @param buttonIndex 从左往右，最左边是0
         */
        void onTopBarBtnPressed(int buttonIndex);
    }


    /**
     * 标题长按监听
     *
     * @param tittleLongPressListener
     */
    public void setTittleLongPressListener(TittleLongPressListener tittleLongPressListener) {
        this.tittleLongPressListener = tittleLongPressListener;
    }

    public interface TittleLongPressListener {
        void onTittlePressed();
    }

}