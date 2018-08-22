package cn.iyunbei.handself.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.iyunbei.handself.bean.OrderBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORDER_BEAN_DAO".
*/
public class OrderBeanDaoDao extends AbstractDao<OrderBeanDao, Long> {

    public static final String TABLENAME = "ORDER_BEAN_DAO";

    /**
     * Properties of entity OrderBeanDao.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property OrderId = new Property(1, int.class, "orderId", false, "ORDER_ID");
        public final static Property GoodsId = new Property(2, int.class, "goodsId", false, "GOODS_ID");
        public final static Property GoodsNum = new Property(3, int.class, "goodsNum", false, "GOODS_NUM");
    }


    public OrderBeanDaoDao(DaoConfig config) {
        super(config);
    }
    
    public OrderBeanDaoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORDER_BEAN_DAO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ORDER_ID\" INTEGER NOT NULL ," + // 1: orderId
                "\"GOODS_ID\" INTEGER NOT NULL ," + // 2: goodsId
                "\"GOODS_NUM\" INTEGER NOT NULL );"); // 3: goodsNum
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORDER_BEAN_DAO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, OrderBeanDao entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getOrderId());
        stmt.bindLong(3, entity.getGoodsId());
        stmt.bindLong(4, entity.getGoodsNum());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, OrderBeanDao entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getOrderId());
        stmt.bindLong(3, entity.getGoodsId());
        stmt.bindLong(4, entity.getGoodsNum());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public OrderBeanDao readEntity(Cursor cursor, int offset) {
        OrderBeanDao entity = new OrderBeanDao( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // orderId
            cursor.getInt(offset + 2), // goodsId
            cursor.getInt(offset + 3) // goodsNum
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, OrderBeanDao entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOrderId(cursor.getInt(offset + 1));
        entity.setGoodsId(cursor.getInt(offset + 2));
        entity.setGoodsNum(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(OrderBeanDao entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(OrderBeanDao entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(OrderBeanDao entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}