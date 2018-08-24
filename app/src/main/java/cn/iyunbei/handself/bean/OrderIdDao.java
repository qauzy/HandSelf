package cn.iyunbei.handself.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:此处是为了存储临时生成的订单id所使用的本地数据库
 **/
@Entity
public class OrderIdDao {

    @Id(autoincrement = true)
    private Long id;


    private long orderId;

    private int totalNum;

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

    private double totalMoney;

    @Generated(hash = 1715473809)
    public OrderIdDao(Long id, long orderId, int totalNum, double totalMoney) {
        this.id = id;
        this.orderId = orderId;
        this.totalNum = totalNum;
        this.totalMoney = totalMoney;
    }

    @Generated(hash = 798168123)
    public OrderIdDao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
