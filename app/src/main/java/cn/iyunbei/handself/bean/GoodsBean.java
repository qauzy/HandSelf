package cn.iyunbei.handself.bean;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class GoodsBean {

    /**
     * code : 200
     * msg : ok
     * data : {"goods_id":1,"goods_name":"土豆","barcode":"12312312","goods_price":"1.00","spec":"规格"}
     */

    private String code;
    private String msg;
    private GoodsDataBean data;

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

    public GoodsDataBean getData() {
        return data;
    }

    public void setData(GoodsDataBean data) {
        this.data = data;
    }


}
