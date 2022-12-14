package jt.kundream.adapter.listviewadapter;

import android.content.Context;

import java.util.List;

import jt.kundream.adapter.listviewadapter.base.ItemViewDelegate;

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {

            }


        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

}
