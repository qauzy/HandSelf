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

    private int goodsId;

    private String goodsGuige;

    private String name;

    private double price;

    @Generated(hash = 1992473408)
    public GoodsBeanDao(long id, int goodsId, String goodsGuige, String name,
            double price) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsGuige = goodsGuige;
        this.name = name;
        this.price = price;
    }

    @Generated(hash = 1464702812)
    public GoodsBeanDao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsGuige() {
        return goodsGuige;
    }

    public void setGoodsGuige(String goodsGuige) {
        this.goodsGuige = goodsGuige;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
