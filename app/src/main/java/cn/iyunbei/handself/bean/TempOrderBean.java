package cn.iyunbei.handself.bean;

import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class TempOrderBean {

    private int orderId;

    private int totalNum;

    private double totalMoney;

    private List<TempGoodsBean> goodsList;


    public TempOrderBean() {
    }

    public TempOrderBean(int orderId, int totalNum, double totalMoney, List<TempGoodsBean> goodsList) {
        this.orderId = orderId;
        this.totalNum = totalNum;
        this.totalMoney = totalMoney;
        this.goodsList = goodsList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<TempGoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<TempGoodsBean> goodsList) {
        this.goodsList = goodsList;
    }


    public static class TempGoodsBean {

        private int goods_id;

        private String spec;

        private String goods_name;

        private String goods_price;

        private String barcode;

        private int goodsNum;

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getGoodsNum() {
            return goodsNum;
        }

        public void setGoodsNum(int goodsNum) {
            this.goodsNum = goodsNum;
        }

        @Override
        public String toString() {
//            return "TempGoodsBean{" +
//                    "goods_id=" + goods_id +
//                    ", spec='" + spec + '\'' +
//                    ", goods_name='" + goods_name + '\'' +
//                    ", goods_price='" + goods_price + '\'' +
//                    ", barcode='" + barcode + '\'' +
//                    ", goodsNum=" + goodsNum +
//                    '}';
            return "{" +
                    "goods_id=" + goods_id +
                    ", spec='" + spec + '\'' +
                    ", goods_name='" + goods_name + '\'' +
                    ", goods_price='" + goods_price + '\'' +
                    ", barcode='" + barcode + '\'' +
                    ", goodsNum=" + goodsNum +
                    '}';
        }
    }
}
