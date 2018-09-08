package cn.iyunbei.handself.contract;

import jt.kundream.base.IBaseView;

public interface PayTypeContract {
    interface Model {
    }

    interface View extends IBaseView{
        /**
         * 现金支付成功
         */
        void cashPaySucc();

    }

    interface Presenter {
    }
}
