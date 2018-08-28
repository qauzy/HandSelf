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

    private List<OrderBean> orderBeanList;

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

    public List<OrderBean> getOrderBeanList() {
        return orderBeanList;
    }

    public void setOrderBeanList(List<OrderBean> orderBeanList) {
        this.orderBeanList = orderBeanList;
    }


    public class OrderBean {


        private int orderId;

        private long goodsId;

        private int goodsNum;

        private List<TempGoodsBean> goodsBeans;

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(long goodsId) {
            this.goodsId = goodsId;
        }

        public int getGoodsNum() {
            return goodsNum;
        }

        public void setGoodsNum(int goodsNum) {
            this.goodsNum = goodsNum;
        }

        public List<TempGoodsBean> getGoodsBeans() {
            return goodsBeans;
        }

        public void setGoodsBeans(List<TempGoodsBean> goodsBeans) {
            this.goodsBeans = goodsBeans;
        }

       public class TempGoodsBean {

            private long goodsId;

            private String goodsGuige;

            private String name;

            private double price;

            private int goodsBarCode;

            public long getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(long goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsGuige() {
                return goodsGuige;
            }

            public void setGoodsGuige(String goodsGuige) {
                this.goodsGuige = goodsGuige;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getGoodsBarCode() {
                return goodsBarCode;
            }

            public void setGoodsBarCode(int goodsBarCode) {
                this.goodsBarCode = goodsBarCode;
            }
        }
    }
}
