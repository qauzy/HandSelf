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
 * @desc:用于数据库的商品表
 **/
@Entity
public class GoodsBeanDao {

    @Id(autoincrement = true)
    private Long id;

    /**
     * 商品id
     */
    private long goodsId;
    /**
     * 商品规格
     */
    private String goodsGuige;

    private String name;

    private double price;

//    private int goodsNum;

    private int goodsBarCode;

    @Generated(hash = 1771103683)
    public GoodsBeanDao(Long id, long goodsId, String goodsGuige, String name,
            double price, int goodsBarCode) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsGuige = goodsGuige;
        this.name = name;
        this.price = price;
        this.goodsBarCode = goodsBarCode;
    }

    @Generated(hash = 1464702812)
    public GoodsBeanDao() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsGuige() {
        return this.goodsGuige;
    }

    public void setGoodsGuige(String goodsGuige) {
        this.goodsGuige = goodsGuige;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGoodsBarCode() {
        return this.goodsBarCode;
    }

    public void setGoodsBarCode(int goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

}
