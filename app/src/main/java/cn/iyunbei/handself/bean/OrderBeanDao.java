package cn.iyunbei.handself.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import static com.tencent.mm.opensdk.diffdev.a.g.au;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import cn.iyunbei.handself.greendao.DaoSession;
import cn.iyunbei.handself.greendao.GoodsBeanDaoDao;
import org.greenrobot.greendao.annotation.NotNull;
import cn.iyunbei.handself.greendao.OrderBeanDaoDao;

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
    private long orderId;

    /**
     * 商品id
     */
    private long goodsId;

    /**
     * 单个商品的数量
     */
    private int goodsNum;

    @ToOne(joinProperty = "goodsId")
    private GoodsBeanDao goodsBeanDao;
//    private int totalNum;
//
//    private double totalMoney;  public int getTotalNum() {
////        return totalNum;
////    }
////
////    public void setTotalNum(int totalNum) {
////        this.totalNum = totalNum;
////    }
////
////    public double getTotalMoney() {
////        return totalMoney;
////    }
////
////    public void setTotalMoney(double totalMoney) {
////        this.totalMoney = totalMoney;
////    }

    /** Used to resolve relations */
//    @Generated(hash = 2040040024)
//    public transient DaoSession daoSession;
    @Keep
    public transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 16599192)
    private transient OrderBeanDaoDao myDao;

    @Generated(hash = 87666959)
    public OrderBeanDao(Long id, long orderId, long goodsId, int goodsNum) {
        this.id = id;
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.goodsNum = goodsNum;
    }

    @Generated(hash = 1013229871)
    public OrderBeanDao() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsNum() {
        return this.goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    @Generated(hash = 211346335)
    private transient Long goodsBeanDao__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 97285688)
    public GoodsBeanDao getGoodsBeanDao() {
        long __key = this.goodsId;
        if (goodsBeanDao__resolvedKey == null
                || !goodsBeanDao__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GoodsBeanDaoDao targetDao = daoSession.getGoodsBeanDaoDao();
            GoodsBeanDao goodsBeanDaoNew = targetDao.load(__key);
            synchronized (this) {
                goodsBeanDao = goodsBeanDaoNew;
                goodsBeanDao__resolvedKey = __key;
            }
        }
        return goodsBeanDao;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 168528230)
    public void setGoodsBeanDao(@NotNull GoodsBeanDao goodsBeanDao) {
        if (goodsBeanDao == null) {
            throw new DaoException(
                    "To-one property 'goodsId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.goodsBeanDao = goodsBeanDao;
            goodsId = goodsBeanDao.getId();
            goodsBeanDao__resolvedKey = goodsId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1382339006)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderBeanDaoDao() : null;
    }


}
