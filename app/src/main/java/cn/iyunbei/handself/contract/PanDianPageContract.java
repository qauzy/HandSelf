package cn.iyunbei.handself.contract;

import android.content.Context;

import cn.iyunbei.handself.activity.PanDianPageActivity;
import jt.kundream.base.IBaseView;

public interface PanDianPageContract {
    interface Model {
    }

    interface View extends IBaseView {
        void showEmpty();

    }

    interface Presenter {
        /**
         * 进入页面之后 盘点id  去获取是否有盘点中的订单
         * @param ctx
         * @param pd_id
         */
        void reqPanDianing(Context ctx, int pd_id);
    }
}
