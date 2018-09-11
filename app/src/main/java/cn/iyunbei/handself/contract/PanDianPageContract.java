package cn.iyunbei.handself.contract;

import android.content.Context;

import cn.iyunbei.handself.activity.PanDianPageActivity;
import cn.iyunbei.handself.bean.PanDianingBean;
import jt.kundream.base.IBaseView;

public interface PanDianPageContract {
    interface Model {
    }

    interface View extends IBaseView {
        void showEmpty();

        /**
         * 显示盘点中的盘点单内容
         *
         * @param bean
         */
        void showContent(PanDianingBean bean);

        /**
         * 盘点的商品数量修改成功
         */
        void editNumSucc();

        /**
         * 显示盘点的数量输入框
         *
         * @param goods_name
         * @param barcode
         */
        void showPdGoodsDlg(String goods_name, String barcode);

        void showNewProfit(String profit_id, String profit_status);
    }

    interface Presenter {
        /**
         * 进入页面之后 盘点id  去获取是否有盘点中的订单
         *
         * @param ctx
         * @param pd_id
         */
        void reqPanDianing(Context ctx, int pd_id, int page);
    }
}
