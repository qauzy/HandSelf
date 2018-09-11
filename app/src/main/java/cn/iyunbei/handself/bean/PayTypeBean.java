package cn.iyunbei.handself.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayTypeBean {


    /**
     * code : 200
     * msg : ok
     * data : [{"payment_type":1,"payment_mode":0,"payment_type_name":"现金1","default":1},{"payment_type":3,"payment_mode":2,"payment_type_name":"支付宝1","default":0}]
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

    public static class DataBean {
        /**
         * payment_type : 1
         * payment_mode : 0
         * payment_type_name : 现金1
         * default : 1
         */

        private int payment_type;
        private int payment_mode;
        private String payment_type_name;
        @SerializedName("default")
        private int defaultX;

        public int getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(int payment_type) {
            this.payment_type = payment_type;
        }

        public int getPayment_mode() {
            return payment_mode;
        }

        public void setPayment_mode(int payment_mode) {
            this.payment_mode = payment_mode;
        }

        public String getPayment_type_name() {
            return payment_type_name;
        }

        public void setPayment_type_name(String payment_type_name) {
            this.payment_type_name = payment_type_name;
        }

        public int getDefaultX() {
            return defaultX;
        }

        public void setDefaultX(int defaultX) {
            this.defaultX = defaultX;
        }
    }
}
