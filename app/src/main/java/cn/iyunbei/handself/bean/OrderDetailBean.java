package cn.iyunbei.handself.bean;

import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/1
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class OrderDetailBean {

    /**
     * code : 200
     * msg : ok
     * data : {"order":{"order_id":32,"inventory_record_no":"20180820080919685","total_amount":"2.00","order_price":"2.00","num":0,"create_time":"2018-08-20 08:09","ok_time":"2018-08-20 08:09"},"orders":[{"goods_name":"土豆","spec":"规格","barcode":"123","goods_id":1,"goods_price":"1.00","price":"1.00","total_amount":"1.00","goods_num":"1.00"},{"goods_name":"白菜","spec":"规格","barcode":"1234","goods_id":2,"goods_price":"1.00","price":"1.00","total_amount":"1.00","goods_num":"1.00"}]}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * order : {"order_id":32,"inventory_record_no":"20180820080919685","total_amount":"2.00","order_price":"2.00","num":0,"create_time":"2018-08-20 08:09","ok_time":"2018-08-20 08:09"}
         * orders : [{"goods_name":"土豆","spec":"规格","barcode":"123","goods_id":1,"goods_price":"1.00","price":"1.00","total_amount":"1.00","goods_num":"1.00"},{"goods_name":"白菜","spec":"规格","barcode":"1234","goods_id":2,"goods_price":"1.00","price":"1.00","total_amount":"1.00","goods_num":"1.00"}]
         */

        private OrderBean order;
        private List<OrdersBean> orders;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrderBean {
            /**
             * order_id : 32
             * inventory_record_no : 20180820080919685
             * total_amount : 2.00
             * order_price : 2.00
             * num : 0
             * create_time : 2018-08-20 08:09
             * ok_time : 2018-08-20 08:09
             */

            private int order_id;
            private String inventory_record_no;
            private String total_amount;
            private String order_price;
            private int num;
            private String create_time;
            private String ok_time;

            public int getOrder_id() {
                return order_id;
            }

            public void setOrder_id(int order_id) {
                this.order_id = order_id;
            }

            public String getInventory_record_no() {
                return inventory_record_no;
            }

            public void setInventory_record_no(String inventory_record_no) {
                this.inventory_record_no = inventory_record_no;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            public String getOrder_price() {
                return order_price;
            }

            public void setOrder_price(String order_price) {
                this.order_price = order_price;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getOk_time() {
                return ok_time;
            }

            public void setOk_time(String ok_time) {
                this.ok_time = ok_time;
            }
        }

        public static class OrdersBean {
            /**
             * goods_name : 土豆
             * spec : 规格
             * barcode : 123
             * goods_id : 1
             * goods_price : 1.00
             * price : 1.00
             * total_amount : 1.00
             * goods_num : 1.00
             */

            private String goods_name;
            private String spec;
            private String barcode;
            private int goods_id;
            private String goods_price;
            private String price;
            private String total_amount;
            private String goods_num;

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getSpec() {
                return spec;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }

            public String getBarcode() {
                return barcode;
            }

            public void setBarcode(String barcode) {
                this.barcode = barcode;
            }

            public int getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(int goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }
        }
    }
}
