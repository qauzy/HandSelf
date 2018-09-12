package cn.iyunbei.handself.contract;

import java.util.List;

import cn.iyunbei.handself.bean.PayTypeBean;
import jt.kundream.base.IBaseView;

public interface PayTypeContract {
    interface Model {
    }

    interface View extends IBaseView {
        /**
         * 现金支付成功
         */
        void cashPaySucc();

        /**
         * 显示支付方式的列表
         * 吐槽：动态的，必须让动态获取，无奈之下，三个支付方式写个列表   fuck
         * @param data
         */
        void showPayTypeList(List<PayTypeBean.DataBean> data);

        /**
         * 支付方式的展示
         *
         * @param defaultPayType
         * @param payModeList
         * @param data
         */
//        void showPayType(int defaultPayType, List<Integer> payModeList, List<PayTypeBean.DataBean> data);
    }

    interface Presenter {
    }
}
