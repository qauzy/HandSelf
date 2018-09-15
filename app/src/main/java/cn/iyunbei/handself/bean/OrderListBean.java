package cn.iyunbei.handself.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/31
 * @e-mail: 245086168@qq.com
 * @desc:订单列表
 **/
public class OrderListBean {

    /**
     * code : 200
     * msg : ok
     * data : [{"total_amount":0,"order_count":0,"order":[],"date":"2018-08-31"},
     * {"total_amount":"4.00","order_count":4,
     * "order":[{"order_id":58,"inventory_record_no":"20180830020208831","total_amount":"1.00","order_price":"1.00","num":0,
     * "create_time":"2018-08-30 02:02","ok_time":"2018-08-30 02:02"},
     * {"order_id":59,"inventory_record_no":"20180830020455320","total_amount":"1.00",
     * "order_price":"1.00","num":0,"create_time":"2018-08-30 02:04","ok_time":"2018-08-30 02:04"},
     * {"order_id":60,"inventory_record_no":"20180830020654840","total_amount":"1.00","order_price":"1.00","num":0,"create_time":"2018-08-30 02:06","ok_time":"2018-08-30 02:06"},
     * {"order_id":63,"inventory_record_no":"20180830022928606","total_amount":"1.00","order_price":"1.00","num":0,"create_time":"2018-08-30 02:29","ok_time":"2018-08-30 02:29"}],"date":"2018-08-30"},
     * {"total_amount":0,"order_count":0,"order":[],"date":"2018-08-29"}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * total_amount : 0
         * order_count : 0
         * order : []
         * date : 2018-08-31
         */

        private String total_amount;
        private int order_count;
        private String date;
        private List<DayOrder> order;

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public int getOrder_count() {
            return order_count;
        }

        public void setOrder_count(int order_count) {
            this.order_count = order_count;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<DayOrder> getOrder() {
            return order;
        }

        public void setOrder(List<DayOrder> order) {
            this.order = order;
        }

//        public static class DayOrder {
//            /**
//             * "order_id":63,
//             * "inventory_record_no":"20180830022928606",
//             * "total_amount":"1.00",
//             * "order_price":"1.00",
//             * "num":0,
//             * "create_time":"2018-08-30 02:29",
//             * "ok_time":"2018-08-30 02:29"
//             */
//            private int order_id;
//
//            private String inventory_record_no;
//
//            private String total_amount;
//
//            private String order_price;
//
//            private int num;
//
//            private String create_time;
//
//            private String ok_time;
//
//            public int getOrder_id() {
//                return order_id;
//            }
//
//            public void setOrder_id(int order_id) {
//                this.order_id = order_id;
//            }
//
//            public String getInventory_record_no() {
//                return inventory_record_no;
//            }
//
//            public void setInventory_record_no(String inventory_record_no) {
//                this.inventory_record_no = inventory_record_no;
//            }
//
//            public String getTotal_amount() {
//                return total_amount;
//            }
//
//            public void setTotal_amount(String total_amount) {
//                this.total_amount = total_amount;
//            }
//
//            public String getOrder_price() {
//                return order_price;
//            }
//
//            public void setOrder_price(String order_price) {
//                this.order_price = order_price;
//            }
//
//            public int getNum() {
//                return num;
//            }
//
//            public void setNum(int num) {
//                this.num = num;
//            }
//
//            public String getCreate_time() {
//                return create_time;
//            }
//
//            public void setCreate_time(String create_time) {
//                this.create_time = create_time;
//            }
//
//            public String getOk_time() {
//                return ok_time;
//            }
//
//            public void setOk_time(String ok_time) {
//                this.ok_time = ok_time;
//            }
//        }
    }
}
