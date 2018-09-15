package cn.iyunbei.handself.presenter;

import android.content.Context;

import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.DayOrder;
import cn.iyunbei.handself.contract.MoreOrderListContract;
import cn.iyunbei.handself.model.MoreOrderListModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

public class MoreOrderListPresenter extends BasePresenter<MoreOrderListContract.View> implements MoreOrderListContract.Presenter {

    private RequestCallback.MoreListCallback moreListCallback = new RequestCallback.MoreListCallback() {
        @Override
        public void succ(List<DayOrder> data) {
            mView.addNewData(data);
        }

        @Override
        public void Fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };

    public void reqMoreListData(Context ctx, String date, int page) {
        new MoreOrderListModel().reqMoreListData(CommonUtil.getString(ctx, "token"), date, page, moreListCallback);
    }
}
