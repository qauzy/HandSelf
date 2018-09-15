package cn.iyunbei.handself.contract;

import java.util.List;

import cn.iyunbei.handself.bean.DayOrder;
import jt.kundream.base.IBaseView;

public interface MoreOrderListContract {
    interface Model {
    }

    interface View extends IBaseView {
        void addNewData(List<DayOrder> data);

    }

    interface Presenter {
    }
}
