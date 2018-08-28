package cn.iyunbei.handself.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import cn.iyunbei.handself.greendao.DaoSession;
import cn.iyunbei.handself.greendao.OrderBeanDaoDao;
import cn.iyunbei.handself.greendao.OrderIdDaoDao;

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

    private double totalMoney;

    @ToMany(referencedJoinProperty = "orderId")
    private List<OrderBeanDao> orderBeanDaoList;


    /** Used to resolve relations */
//    @Generated(hash = 2040040024)
//    public transient DaoSession daoSession;

    @Keep
    public transient DaoSession daoSession;


    /** Used for active entity operations. */
    @Generated(hash = 2086027799)
    private transient OrderIdDaoDao myDao;

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

    public int getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getTotalMoney() {
        return this.totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 918499847)
    public List<OrderBeanDao> getOrderBeanDaoList() {
        if (orderBeanDaoList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderBeanDaoDao targetDao = daoSession.getOrderBeanDaoDao();
            List<OrderBeanDao> orderBeanDaoListNew = targetDao._queryOrderIdDao_OrderBeanDaoList(id);
            synchronized (this) {
                if (orderBeanDaoList == null) {
                    orderBeanDaoList = orderBeanDaoListNew;
                }
            }
        }
        return orderBeanDaoList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1526152910)
    public synchronized void resetOrderBeanDaoList() {
        orderBeanDaoList = null;
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
    @Generated(hash = 1842477094)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderIdDaoDao() : null;
    }

//    /**
//     * To-many relationship, resolved on first access (and after reset).
//     * Changes to to-many relations are not persisted, make changes to the target entity.
//     */
//    @Generated(hash = 918499847)
//    public List<OrderBeanDao> getOrderBeanDaoList() {
//        if (orderBeanDaoList == null) {
//            final DaoSession daoSession = this.daoSession;
//            if (daoSession == null) {
//                throw new DaoException("Entity is detached from DAO context");
//            }
//            OrderBeanDaoDao targetDao = daoSession.getOrderBeanDaoDao();
//            List<OrderBeanDao> orderBeanDaoListNew = targetDao
//                    ._queryOrderIdDao_OrderBeanDaoList(id);
//            synchronized (this) {
//                if (orderBeanDaoList == null) {
//                    orderBeanDaoList = orderBeanDaoListNew;
//                }
//            }
//        }
//        return orderBeanDaoList;
//    }
//
//    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
//    @Generated(hash = 1526152910)
//    public synchronized void resetOrderBeanDaoList() {
//        orderBeanDaoList = null;
//    }
//
//    /**
//     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
//     * Entity must attached to an entity context.
//     */
//    @Generated(hash = 128553479)
//    public void delete() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.delete(this);
//    }
//
//    /**
//     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
//     * Entity must attached to an entity context.
//     */
//    @Generated(hash = 1942392019)
//    public void refresh() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.refresh(this);
//    }
//
//    /**
//     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
//     * Entity must attached to an entity context.
//     */
//    @Generated(hash = 713229351)
//    public void update() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.update(this);
//    }
//
//    /** called by internal mechanisms, do not call yourself. */
//    @Generated(hash = 1842477094)
//    public void __setDaoSession(DaoSession daoSession) {
//        this.daoSession = daoSession;
//        myDao = daoSession != null ? daoSession.getOrderIdDaoDao() : null;
//    }


}
