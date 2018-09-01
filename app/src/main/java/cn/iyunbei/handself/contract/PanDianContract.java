package cn.iyunbei.handself.contract;

import android.content.Context;

import java.util.List;

import cn.iyunbei.handself.bean.PanDianBean;
import jt.kundream.base.IBaseView;

public interface PanDianContract {
    interface Model {
    }

    interface View extends IBaseView {
        /**
         * 显示页面的盘点数据
         * @param list
         */
        void showData(List<PanDianBean.DataBean> list);

    }

    interface Presenter {
        void getPanDianList(Context ctx);
    }
}
