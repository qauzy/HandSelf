package jt.kundream.base;

/**
 * Created by Administrator on 2016/5/28.
 */
public abstract class BasePresenter<T> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detach() {
        mView = null;
    }



}
