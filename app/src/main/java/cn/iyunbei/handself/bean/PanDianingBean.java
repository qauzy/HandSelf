package cn.iyunbei.handself.bean;

import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/4
 * @e-mail: 245086168@qq.com
 * @desc:盘点中的订单详情
 **/
public class PanDianingBean {

    /**
     * code : 200
     * msg : ok
     * data : {"profit":{"profit_id":43,"profit_no":"PDD0000000666153602450868","member_name":"abc"},"list":{"current_page":1,"data":[{"profits_id":1,"goods_name":"奥利奥巧脆卷巧克力","barcode":"6901668053527","goods_number":"94.00","real_number":"0.00","spec":"100kg"},{"profits_id":2,"goods_name":"45g闲趣酥心卷芝士味","barcode":"6901668005069","goods_number":"100.00","real_number":"0.00","spec":"100kg"},{"profits_id":3,"goods_name":"45g闲趣酥心卷蕃茄味","barcode":"6901668007186","goods_number":"100.00","real_number":"0.00","spec":"100kg"}],"first_page_url":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=1","from":1,"last_page":2,"last_page_url":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=2","next_page_url":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=2","path":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods","per_page":10,"prev_page_url":null,"to":10,"total":11}}
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
         * profit : {"profit_id":43,"profit_no":"PDD0000000666153602450868","member_name":"abc"}
         * list : {"current_page":1,"data":[{"profits_id":1,"goods_name":"奥利奥巧脆卷巧克力","barcode":"6901668053527","goods_number":"94.00","real_number":"0.00","spec":"100kg"},{"profits_id":2,"goods_name":"45g闲趣酥心卷芝士味","barcode":"6901668005069","goods_number":"100.00","real_number":"0.00","spec":"100kg"},{"profits_id":3,"goods_name":"45g闲趣酥心卷蕃茄味","barcode":"6901668007186","goods_number":"100.00","real_number":"0.00","spec":"100kg"}],"first_page_url":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=1","from":1,"last_page":2,"last_page_url":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=2","next_page_url":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=2","path":"http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods","per_page":10,"prev_page_url":null,"to":10,"total":11}
         */

        private ProfitBean profit;
        private ListBean list;

        public ProfitBean getProfit() {
            return profit;
        }

        public void setProfit(ProfitBean profit) {
            this.profit = profit;
        }

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public static class ProfitBean {
            /**
             * profit_id : 43
             * profit_no : PDD0000000666153602450868
             * member_name : abc
             */

            private int profit_id;
            private String profit_no;
            private String member_name;

            public int getProfit_id() {
                return profit_id;
            }

            public void setProfit_id(int profit_id) {
                this.profit_id = profit_id;
            }

            public String getProfit_no() {
                return profit_no;
            }

            public void setProfit_no(String profit_no) {
                this.profit_no = profit_no;
            }

            public String getMember_name() {
                return member_name;
            }

            public void setMember_name(String member_name) {
                this.member_name = member_name;
            }
        }

        public static class ListBean {
            /**
             * current_page : 1
             * data : [{"profits_id":1,"goods_name":"奥利奥巧脆卷巧克力","barcode":"6901668053527","goods_number":"94.00","real_number":"0.00","spec":"100kg"},{"profits_id":2,"goods_name":"45g闲趣酥心卷芝士味","barcode":"6901668005069","goods_number":"100.00","real_number":"0.00","spec":"100kg"},{"profits_id":3,"goods_name":"45g闲趣酥心卷蕃茄味","barcode":"6901668007186","goods_number":"100.00","real_number":"0.00","spec":"100kg"}]
             * first_page_url : http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=1
             * from : 1
             * last_page : 2
             * last_page_url : http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=2
             * next_page_url : http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods?page=2
             * path : http://pda.iyunbei.cn/api/pda/v1/profit/profitgoods
             * per_page : 10
             * prev_page_url : null
             * to : 10
             * total : 11
             */

            private int current_page;
            private String first_page_url;
            private int from;
            private int last_page;
            private String last_page_url;
            private String next_page_url;
            private String path;
            private int per_page;
            private int prev_page_url;
            private int to;
            private int total;
            private List<DataBean> data;

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

            public String getNext_page_url() {
                return next_page_url;
            }

            public void setNext_page_url(String next_page_url) {
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

            public int getPrev_page_url() {
                return prev_page_url;
            }

            public void setPrev_page_url(int prev_page_url) {
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

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * profits_id : 1
                 * goods_name : 奥利奥巧脆卷巧克力
                 * barcode : 6901668053527
                 * goods_number : 94.00
                 * real_number : 0.00
                 * spec : 100kg
                 */

                private int profits_id;
                private String goods_name;
                private String barcode;
                private String goods_number;
                private String real_number;
                private String spec;

                public int getProfits_id() {
                    return profits_id;
                }

                public void setProfits_id(int profits_id) {
                    this.profits_id = profits_id;
                }

                public String getGoods_name() {
                    return goods_name;
                }

                public void setGoods_name(String goods_name) {
                    this.goods_name = goods_name;
                }

                public String getBarcode() {
                    return barcode;
                }

                public void setBarcode(String barcode) {
                    this.barcode = barcode;
                }

                public String getGoods_number() {
                    return goods_number;
                }

                public void setGoods_number(String goods_number) {
                    this.goods_number = goods_number;
                }

                public String getReal_number() {
                    return real_number;
                }

                public void setReal_number(String real_number) {
                    this.real_number = real_number;
                }

                public String getSpec() {
                    return spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }
            }
        }
    }
}
