package cn.iyunbei.handself.bean;

import java.util.List;

public class PanDianBean {

    /**
     * code : 200
     * msg : ok
     * data : [{"profit_id":26,"profit_no":"PDD0000000666153475566942","ok_time":"","profit_status":0,"nums":0,"ying":0,"kui":0}]
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
         * profit_id : 26
         * profit_no : PDD0000000666153475566942
         * ok_time :
         * profit_status : 0
         * nums : 0
         * ying : 0
         * kui : 0
         */

        private int profit_id;
        private String profit_no;
        private String ok_time;
        private int profit_status;
        private int nums;
        private int ying;
        private int kui;

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

        public String getOk_time() {
            return ok_time;
        }

        public void setOk_time(String ok_time) {
            this.ok_time = ok_time;
        }

        public int getProfit_status() {
            return profit_status;
        }

        public void setProfit_status(int profit_status) {
            this.profit_status = profit_status;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public int getYing() {
            return ying;
        }

        public void setYing(int ying) {
            this.ying = ying;
        }

        public int getKui() {
            return kui;
        }

        public void setKui(int kui) {
            this.kui = kui;
        }
    }
}
