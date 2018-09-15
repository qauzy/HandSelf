package cn.iyunbei.handself.bean;

import java.util.List;

public class OrderListPageBean {

    /**
     * code : 200
     * msg : ok
     * data : {"order":{"current_page":1,"data":[{"order_id":32,"inventory_record_no":"20180820080919685","total_amount":"2.00","order_price":"2.00","num":0,"create_time":"2018-08-20 08:09","ok_time":"2018-08-20 08:09"}],"first_page_url":"http://pda.iyunbei.cn/api/pda/v1/order/orderpage?page=1","from":1,"last_page":1,"last_page_url":"http://pda.iyunbei.cn/api/pda/v1/order/orderpage?page=1","next_page_url":null,"path":"http://pda.iyunbei.cn/api/pda/v1/order/orderpage","per_page":10,"prev_page_url":null,"to":1,"total":1}}
     */

    private String code;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * order : {"current_page":1,"data":[{"order_id":32,"inventory_record_no":"20180820080919685","total_amount":"2.00","order_price":"2.00","num":0,"create_time":"2018-08-20 08:09","ok_time":"2018-08-20 08:09"}],"first_page_url":"http://pda.iyunbei.cn/api/pda/v1/order/orderpage?page=1","from":1,"last_page":1,"last_page_url":"http://pda.iyunbei.cn/api/pda/v1/order/orderpage?page=1","next_page_url":null,"path":"http://pda.iyunbei.cn/api/pda/v1/order/orderpage","per_page":10,"prev_page_url":null,"to":1,"total":1}
         */

        private OrderBean order;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public static class OrderBean {
            /**
             * current_page : 1
             * data : [{"order_id":32,"inventory_record_no":"20180820080919685","total_amount":"2.00","order_price":"2.00","num":0,"create_time":"2018-08-20 08:09","ok_time":"2018-08-20 08:09"}]
             * first_page_url : http://pda.iyunbei.cn/api/pda/v1/order/orderpage?page=1
             * from : 1
             * last_page : 1
             * last_page_url : http://pda.iyunbei.cn/api/pda/v1/order/orderpage?page=1
             * next_page_url : null
             * path : http://pda.iyunbei.cn/api/pda/v1/order/orderpage
             * per_page : 10
             * prev_page_url : null
             * to : 1
             * total : 1
             */

            private int current_page;
            private String first_page_url;
            private int from;
            private int last_page;
            private String last_page_url;
            private Object next_page_url;
            private String path;
            private int per_page;
            private Object prev_page_url;
            private int to;
            private int total;
            private List<DayOrder> data;

            public int getCurrent_page() {
                return current_page;
            }

            public void setCurrent_page(int current_page) {
                this.current_page = current_page;
            }

            public String getFirst_page_url() {
                return first_page_url;
            }

            public void setFirst_page_url(String first_page_url) {
                this.first_page_url = first_page_url;
            }

            public int getFrom() {
                return from;
            }

            public void setFrom(int from) {
                this.from = from;
            }

            public int getLast_page() {
                return last_page;
            }

            public void setLast_page(int last_page) {
                this.last_page = last_page;
            }

            public String getLast_page_url() {
                return last_page_url;
            }

            public void setLast_page_url(String last_page_url) {
                this.last_page_url = last_page_url;
            }

            public Object getNext_page_url() {
                return next_page_url;
            }

            public void setNext_page_url(Object next_page_url) {
                this.next_page_url = next_page_url;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getPer_page() {
                return per_page;
            }

            public void setPer_page(int per_page) {
                this.per_page = per_page;
            }

            public Object getPrev_page_url() {
                return prev_page_url;
            }

            public void setPrev_page_url(Object prev_page_url) {
                this.prev_page_url = prev_page_url;
            }

            public int getTo() {
                return to;
            }

            public void setTo(int to) {
                this.to = to;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<DayOrder> getData() {
                return data;
            }

            public void setData(List<DayOrder> data) {
                this.data = data;
            }

//            public static class DayOrder {
//                /**
//                 * order_id : 32
//                 * inventory_record_no : 20180820080919685
//                 * total_amount : 2.00
//                 * order_price : 2.00
//                 * num : 0
//                 * create_time : 2018-08-20 08:09
//                 * ok_time : 2018-08-20 08:09
//                 */
//
//                private int order_id;
//                private String inventory_record_no;
//                private String total_amount;
//                private String order_price;
//                private int num;
//                private String create_time;
//                private String ok_time;
//
//                public int getOrder_id() {
//                    return order_id;
//                }
//
//                public void setOrder_id(int order_id) {
//                    this.order_id = order_id;
//                }
//
//                public String getInventory_record_no() {
//                    return inventory_record_no;
//                }
//
//                public void setInventory_record_no(String inventory_record_no) {
//                    this.inventory_record_no = inventory_record_no;
//                }
//
//                public String getTotal_amount() {
//                    return total_amount;
//                }
//
//                public void setTotal_amount(String total_amount) {
//                    this.total_amount = total_amount;
//                }
//
//                public String getOrder_price() {
//                    return order_price;
//                }
//
//                public void setOrder_price(String order_price) {
//                    this.order_price = order_price;
//                }
//
//                public int getNum() {
//                    return num;
//                }
//
//                public void setNum(int num) {
//                    this.num = num;
//                }
//
//                public String getCreate_time() {
//                    return create_time;
//                }
//
//                public void setCreate_time(String create_time) {
//                    this.create_time = create_time;
//                }
//
//                public String getOk_time() {
//                    return ok_time;
//                }
//
//                public void setOk_time(String ok_time) {
//                    this.ok_time = ok_time;
//                }
//            }
        }
    }
}
