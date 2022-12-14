package cn.iyunbei.handself.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.iyunbei.handself.bean.OrderIdDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORDER_ID_DAO".
*/
public class OrderIdDaoDao extends AbstractDao<OrderIdDao, Long> {

    public static final String TABLENAME = "ORDER_ID_DAO";

    /**
     * Properties of entity OrderIdDao.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property OrderId = new Property(1, long.class, "orderId", false, "ORDER_ID");
        public final static Property TotalNum = new Property(2, int.class, "totalNum", false, "TOTAL_NUM");
        public final static Property TotalMoney = new Property(3, double.class, "totalMoney", false, "TOTAL_MONEY");
    }

    private DaoSession daoSession;


    public OrderIdDaoDao(DaoConfig config) {
        super(config);
    }
    
    public OrderIdDaoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORDER_ID_DAO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ORDER_ID\" INTEGER NOT NULL ," + // 1: orderId
                "\"TOTAL_NUM\" INTEGER NOT NULL ," + // 2: totalNum
                "\"TOTAL_MONEY\" REAL NOT NULL );"); // 3: totalMoney
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORDER_ID_DAO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, OrderIdDao entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getOrderId());
        stmt.bindLong(3, entity.getTotalNum());
        stmt.bindDouble(4, entity.getTotalMoney());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, OrderIdDao entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getOrderId());
        stmt.bindLong(3, entity.getTotalNum());
        stmt.bindDouble(4, entity.getTotalMoney());
    }

    @Override
    protected final void attachEntity(OrderIdDao entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public OrderIdDao readEntity(Cursor cursor, int offset) {
        OrderIdDao entity = new OrderIdDao( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // orderId
            cursor.getInt(offset + 2), // totalNum
            cursor.getDouble(offset + 3) // totalMoney
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, OrderIdDao entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOrderId(cursor.getLong(offset + 1));
        entity.setTotalNum(cursor.getInt(offset + 2));
        entity.setTotalMoney(cursor.getDouble(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(OrderIdDao entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(OrderIdDao entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(OrderIdDao entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
