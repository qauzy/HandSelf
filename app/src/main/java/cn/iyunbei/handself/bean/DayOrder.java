package cn.iyunbei.handself.bean;

import java.io.Serializable;

public class DayOrder implements Serializable{
    /**
     * "order_id":63,
     * "inventory_record_no":"20180830022928606",
     * "total_amount":"1.00",
     * "order_price":"1.00",
     * "num":0,
     * "create_time":"2018-08-30 02:29",
     * "ok_time":"2018-08-30 02:29"
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
