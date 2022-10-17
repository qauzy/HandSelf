package cn.iyunbei.handself.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.greendao.GreenDaoHelper;
import cn.iyunbei.handself.presenter.SpeechUtils;
import jt.kundream.utils.JsonUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class MainModel implements MainContract.Model {
    private static final String TAG = MainModel.class.getSimpleName();
    public static void requestGoods(String barCode, SpeechUtils spk, final RequestCallback.GetGoodsCallback callback) {
        Cursor cursor =  GreenDaoHelper.getDb().rawQuery("select * from goods where barcode=?",new String[]{barCode});
//        if (cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            GoodsBean goodsBean = new GoodsBean();
//            GoodsDataBean dataBean = new GoodsDataBean();
//            dataBean.setBarcode(barCode);
//            dataBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
//            dataBean.setGoodsName(cursor.getString(cursor.getColumnIndex("goods_name")));
//            dataBean.setSpec(cursor.getString(cursor.getColumnIndex("spec")));
//            dataBean.setPrice(cursor.getString(cursor.getColumnIndex("price")));
//            dataBean.setSupplier(cursor.getString(cursor.getColumnIndex("supplier")));
////            dataBean.setBrand(cursor.getString(cursor.getColumnIndex("brand")));

//            goodsBean.setData(dataBean);
//            callback.succ(goodsBean);
//            return;
//        }
        OkGo.<String>get(Constants.GET_GOODS)
                .params("barcode", barCode)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body().toString();
                        if (JsonUtils.checkToken(result) == 200) {
                            GoodsBean bean = new Gson().fromJson(result, GoodsBean.class);
                            GoodsDataBean data = bean.getData();

                            //只有价格不是空的才保存到本地数据库
//                            if(data.getPrice()!= null && !data.getPrice().isEmpty()){
//                                //实例化常量值
//                                ContentValues cValue = new ContentValues();
//                                cValue.put("id",data.getId());
//                                cValue.put("goods_name",data.getGoodsName());
//                                cValue.put("goods_name",data.getGoodsName());
//                                cValue.put("barcode",data.getBarcode());
//                                cValue.put("price",data.getPrice());
//                                cValue.put("spec",data.getSpec());
//                                cValue.put("supplier",data.getSupplier());
//                                GreenDaoHelper.getDb().insert("goods",null,cValue);
//                            }

                            callback.succ(bean);
                        }  else if(JsonUtils.checkToken(result) == 10005){
                            callback.fial(JsonUtils.checkToken(result),barCode);
                        }else{
                            callback.fial(JsonUtils.checkToken(result),JsonUtils.getMsg(result));
                        }
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callback.fial(500," 获取商品属性信息时发生网络错误");
                    }
                });
    }
}
