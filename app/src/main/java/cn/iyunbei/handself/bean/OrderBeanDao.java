package cn.iyunbei.handself.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import static com.tencent.mm.opensdk.diffdev.a.g.au;

import org.greenrobot.greendao.annotation.Generated;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:用于数据库的订单表实体
 **/
@Entity
public class OrderBeanDao {

    @Id(autoincrement = true)
    private Long id;

    /**
     * 订单id
     */
    private int orderId;

    /**
     * 商品id
     */
    private int goodsId;

    /**
     * 单个商品的数量
     */
    private int goodsNum;

    @Generated(hash = 18325167)
    public OrderBeanDao(Long id, int orderId, int goodsId, int goodsNum) {
        this.id = id;
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.goodsNum = goodsNum;
    }

    @Generated(hash = 1013229871)
    public OrderBeanDao() {
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
}
