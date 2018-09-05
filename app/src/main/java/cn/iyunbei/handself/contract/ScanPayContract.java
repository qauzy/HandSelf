package cn.iyunbei.handself.contract;

import jt.kundream.base.IBaseView;

public interface ScanPayContract {
    interface Model {
    }

    interface View extends IBaseView {
        /**
         * 支付成功 跳转到支付成功界面
         */
        void paySucc();

    }

    interface Presenter {
    }
}
