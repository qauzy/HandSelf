package cn.iyunbei.handself.contract;

import android.content.Context;

import java.util.List;

import cn.iyunbei.handself.bean.GoodsListBean;
import cn.iyunbei.handself.bean.PanDianBean;
import jt.kundream.base.IBaseView;

public interface GoodsContract {
    interface Model {
    }

    interface View extends IBaseView {
        /**
         * 显示页面的盘点数据
         * @param list
         */
        void showData(List<GoodsListBean.DataBean> list);

    }

    interface Presenter {
        void getGoodsList(Integer page,Integer pageSize, Context ctx);
    }
}
