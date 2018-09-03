package cn.iyunbei.handself.presenter;

import android.content.Context;

import cn.iyunbei.handself.contract.PanDianPageContract;
import jt.kundream.base.BasePresenter;

public class PanDianPagePresenter extends BasePresenter<PanDianPageContract.View> implements PanDianPageContract.Presenter {

    @Override
    public void reqPanDianing(Context ctx, int pd_id) {
        if (pd_id == -1) {
            mView.showEmpty();
        } else {
//            new PanDianPageModel().reqPanDianing(CommonUtil.getString(ctx,"_token"),pd_id,netCallback);
        }
    }
}
