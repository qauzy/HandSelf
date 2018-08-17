package jt.kundream.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 版权所有，违法必究！
 * Created by YangTianKun on 2017.4.25.
 * childListView  主要是用于外层嵌套ScrollView之后，解决嵌套之间的冲突问题的
 */

public class ChildListView extends ListView {

    private boolean haveScrollbar = true;

    public ChildListView(Context context) {
        super(context);
    }

    public ChildListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHaveScrollbar(boolean haveScrollbar) {
        this.haveScrollbar = haveScrollbar;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
