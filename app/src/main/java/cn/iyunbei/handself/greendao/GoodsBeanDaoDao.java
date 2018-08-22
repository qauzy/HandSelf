package cn.iyunbei.handself.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.iyunbei.handself.bean.GoodsBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GOODS_BEAN_DAO".
*/
public class GoodsBeanDaoDao extends AbstractDao<GoodsBeanDao, Long> {

    public static final String TABLENAME = "GOODS_BEAN_DAO";

    /**
     * Properties of entity GoodsBeanDao.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property GoodsId = new Property(1, int.class, "goodsId", false, "GOODS_ID");
        public final static Property GoodsGuige = new Property(2, String.class, "goodsGuige", false, "GOODS_GUIGE");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Price = new Property(4, double.class, "price", false, "PRICE");
    }


    public GoodsBeanDaoDao(DaoConfig config) {
        super(config);
    }
    
    public GoodsBeanDaoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GOODS_BEAN_DAO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"GOODS_ID\" INTEGER NOT NULL ," + // 1: goodsId
                "\"GOODS_GUIGE\" TEXT," + // 2: goodsGuige
                "\"NAME\" TEXT," + // 3: name
                "\"PRICE\" REAL NOT NULL );"); // 4: price
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GOODS_BEAN_DAO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GoodsBeanDao entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getGoodsId());
 
        String goodsGuige = entity.getGoodsGuige();
        if (goodsGuige != null) {
            stmt.bindString(3, goodsGuige);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindDouble(5, entity.getPrice());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GoodsBeanDao entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getGoodsId());
 
        String goodsGuige = entity.getGoodsGuige();
        if (goodsGuige != null) {
            stmt.bindString(3, goodsGuige);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindDouble(5, entity.getPrice());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public GoodsBeanDao readEntity(Cursor cursor, int offset) {
        GoodsBeanDao entity = new GoodsBeanDao( //
            cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // goodsId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // goodsGuige
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.getDouble(offset + 4) // price
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GoodsBeanDao entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setGoodsId(cursor.getInt(offset + 1));
        entity.setGoodsGuige(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPrice(cursor.getDouble(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GoodsBeanDao entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GoodsBeanDao entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GoodsBeanDao entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
