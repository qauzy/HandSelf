package cn.iyunbei.handself.bean;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class UserBean {

    /**
     * code : 200
     * msg : ok！
     * data : {"member_id":666,"member_account":"abc","member_name":"张三","member_mobile":"15588888888"}
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
         * member_id : 666
         * member_account : abc
         * member_name : 张三
         * member_mobile : 15588888888
         */

        private int member_id;
        private String member_account;
        private String member_name;
        private String member_mobile;

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public String getMember_account() {
            return member_account;
        }

        public void setMember_account(String member_account) {
            this.member_account = member_account;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getMember_mobile() {
            return member_mobile;
        }

        public void setMember_mobile(String member_mobile) {
            this.member_mobile = member_mobile;
        }
    }
}
