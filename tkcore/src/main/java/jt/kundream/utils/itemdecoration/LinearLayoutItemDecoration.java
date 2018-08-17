package jt.kundream.utils.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 版权所有,违法必究！！！
 *
 * @Company:NanYangYunBeiTeac
 * @Copyright:©2017-2018
 * @Author:YangTianKun Created at 2018/5/14 17
 * @e-mail:245086168@qq.com
 * @desc:此包下就是存储设置recyclerView的item间隔的封装。此类为LinearLayout的时候
 */

public class LinearLayoutItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 使用方法：
     * mRecyclerView.addItemDecoration(new SpacesItemDecoration(5));
     */

    private int space;

    public LinearLayoutItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
