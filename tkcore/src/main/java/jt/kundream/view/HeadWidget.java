package jt.kundream.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jt.kundream.R;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.InputMethodUtil;
import jt.kundream.utils.OnClickHelper;


/**
 * 版权所有，违法必究！！！
 *
 * @author YangTianK on 2017/11/15 10:02;
 *         邮箱：245086168@qq.com
 *         说明：公共头部
 */
public class HeadWidget {
    private View headBottomLine;
    private TextView headLeftRed;
    private TextView headLeftText;
    private LinearLayout headLeftLayout;
    //
    private TextView headRightText;
    private ImageView headRightBeforeIcon;
    private View headRightLayout;
    //
    private TextView headTitleText;
    private LinearLayout headTitleLayout;

    //    private Toolbar headToolbar;
    //
    private LayoutInflater inflater;
    private FrameLayout contentView;
    private View headView;
    private ImageView headRightAfterIcon;

    //
    public HeadWidget(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public HeadWidget(Context context, int layoutResId, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        contentView = (FrameLayout) inflater.inflate(R.layout.layout_main_container, parent, false);
        initUserView(context, layoutResId);
        initHeadView();
    }

    /**
    * @author YangTianKun
    * @date 创建时间 2017/11/23 9:18
    * Describe：设置隐藏头部所有控件  主要用于一个activity中包含N个fragment的时候  有些fragment显示标题  有些不显示标题
    */
    public void hideWidget(){
        headBottomLine.setVisibility(View.GONE);
        headLeftLayout.setVisibility(View.GONE);
        headTitleLayout.setVisibility(View.GONE);
        headRightLayout.setVisibility(View.GONE);
    }

    /**
    * @author YangTianKun
    * @date 创建时间 2017/11/23 9:20
    * Describe：设置显示headWidget 同上一个方法
    */
    public void showWidget(){
        headBottomLine.setVisibility(View.VISIBLE);
        headLeftLayout.setVisibility(View.VISIBLE);
        headTitleLayout.setVisibility(View.VISIBLE);
        headRightLayout.setVisibility(View.VISIBLE);
    }

    private void initHeadView() {
        headView = inflater.inflate(R.layout.layout_main_head, contentView);
//        headToolbar = ViewUtil.findView(headView, R.id.head_tool_bar);
//        headBackIcon = (ImageView) view.findViewById(R.id.head_back_icon);
        headLeftText = (TextView) headView.findViewById(R.id.head_left_text);
        headLeftRed = (TextView) headView.findViewById(R.id.head_left_red);
        headLeftLayout = (LinearLayout) headView.findViewById(R.id.head_left_layout);
        headTitleText = (TextView) headView.findViewById(R.id.head_title_text);
        headRightText = (TextView) headView.findViewById(R.id.head_right_text);
        headRightBeforeIcon = (ImageView) headView.findViewById(R.id.head_right_before_icon);
        headRightLayout = headView.findViewById(R.id.head_right_layout);
        headTitleLayout = (LinearLayout) headView.findViewById(R.id.head_title_layout);
        headBottomLine = headView.findViewById(R.id.head_bottom_line);
        headRightAfterIcon = (ImageView) headView.findViewById(R.id.head_right_after_icon);
        headRightAfterIcon.setVisibility(View.GONE);
        headRightBeforeIcon.setVisibility(View.GONE);
        setLeftVisibility(View.INVISIBLE);
        setLeftRedVisibility(View.INVISIBLE);
    }

    private void initUserView(Context context, int layoutResID) {
        View userView = inflater.inflate(layoutResID, contentView, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = (int) context.getResources().getDimension(R.dimen.head_height);
        contentView.addView(userView, params);
    }

    // ----------------------- center ----------------------------
    public HeadWidget setTitleText(String text) {
        if (text != null) {
            headTitleText.setText(text);
        }
        return this;
    }

    public HeadWidget setTitleText(int resId) {
        headTitleText.setText(getResString(resId));
        return this;
    }

    public HeadWidget setTitleIcon(int left, int top, int right, int bottom) {
        headTitleText.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    public HeadWidget setTitleOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headTitleText, onClickListener);
        }
        return this;
    }

    public HeadWidget setTitleRightIcon(int resId) {
        headTitleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        return this;
    }

    public TextView getCenterChild() {
        return headTitleText;
    }

    // ----------------------- left ----------------------------
    public HeadWidget setLeftText(String text) {
        if (text != null) {
            headLeftText.setText(text);
        }
        return this;
    }

    public HeadWidget setLeftText(int resId) {
        headLeftText.setText(getResString(resId));
        return this;
    }

    public HeadWidget setLeftRed(String text) {
        if (text != null) {
            headLeftRed.setText(text);
        }
        return this;
    }

    public HeadWidget setLeftRedVisibility(int visibility) {
        headLeftRed.setVisibility(visibility);
        return this;
    }

    public HeadWidget setLeftVisibility(int visibility) {
        headLeftLayout.setVisibility(visibility);
        return this;
    }

    public HeadWidget setLeftOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headLeftLayout, onClickListener);
        }
        return this;
    }

    public HeadWidget setBackClickListener(final Activity activity) {
        OnClickHelper.setOnClickListener(headLeftLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtil.dismiss(activity);
                ActivityUtil.finish(activity);
            }
        });
        return this;
    }

    public HeadWidget setLeftIcon(Bitmap bmp) {
        Drawable drawable = null;
        if (bmp != null) {
            drawable = new BitmapDrawable(bmp);
        }
        headLeftText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//        headTitleText.setImageBitmap(bmp);
        return this;
    }

    public HeadWidget setLeftIcon(int resId) {
        headLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
//        headBackIcon.setImageResource(resId);
        return this;
    }

    public LinearLayout getLeftChild() {
        return headLeftLayout;
    }

    // ----------------------- right ----------------------------
    public HeadWidget setRightText(String text) {
        if (text != null) {
            headRightText.setText(text);
        }
        return this;
    }

    public HeadWidget setRightText(int resId) {
        headRightText.setText(getResString(resId));
        return this;
    }

    public HeadWidget setRightIcon(int left, int top, int right, int bottom) {
        headRightText.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    public HeadWidget setRightIcon(int resId) {
        if (resId > 0) {
            headRightText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        }
        return this;
    }

    public HeadWidget setRightBeforeIcon(int resId) {
        if (resId > 0) {
            headRightBeforeIcon.setImageResource(resId);
            headRightBeforeIcon.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public HeadWidget setRightAfterIcon(int resId) {
        if (resId > 0) {
            headRightAfterIcon.setImageResource(resId);
            headRightAfterIcon.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 头 右边整个点击
     *
     * @param onClickListener
     * @return
     */
    public HeadWidget setRightLayoutOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headRightLayout, onClickListener);
        }
        return this;
    }

    /**
     * 头 右边文字点击
     *
     * @param onClickListener
     * @return
     */
    public HeadWidget setRightOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headRightText, onClickListener);
        }
        return this;
    }

    /**
     * 头 右边图片点击
     *
     * @param onClickListener
     * @return
     */
    public HeadWidget setRightBeforeIconOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headRightBeforeIcon, onClickListener);
        }
        return this;
    }

    public HeadWidget setRightAfterIconOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headRightAfterIcon, onClickListener);
        }
        return this;
    }

    public HeadWidget setRightVisibility(int visibility) {
        headRightLayout.setVisibility(visibility);
        return this;
    }

    public HeadWidget setHeadBottomLineGone() {
        headBottomLine.setVisibility(View.GONE);
        return this;
    }


    // ----------
    private String getResString(int resId) {
        if (resId > 0) {
            return contentView.getContext().getString(resId);
        }
        return "";
    }

    // ----------
//    public Toolbar getToolbar() {
//        return headToolbar;
//    }

    public FrameLayout getContentView() {
        return contentView;
    }

    public View getRightChild() {
        return headRightLayout;
    }

    public ImageView getHeadRightBeforeIcon() {
        return headRightBeforeIcon;
    }

    public TextView getHeadRightText() {
        return headRightText;
    }
}
