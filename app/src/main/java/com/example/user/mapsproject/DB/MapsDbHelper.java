package com.example.user.mapsproject.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.user.mapsproject.MarkerItem;

import java.util.List;

import nl.qbusict.cupboard.annotation.Index;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.internal.IndexStatement;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MapsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MapsNotify.db";
    private static final int DATABASE_VERSION = 1;

    static {
        cupboard().register(MarkerItem.class);
    }

    public MapsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            EntityConverter<MarkerItem> converter = cupboard().getEntityConverter(MarkerItem.class);
            createNewTable(db, converter.getTable(), converter.getColumns());
        } else {
            cupboard().withDatabase(db).dropAllTables();
            onCreate(db);
        }
    }

    boolean createNewTable(SQLiteDatabase db, String table, List<EntityConverter.Column> cols) {
        StringBuilder sql = new StringBuilder("create table '").append(table).append("' (_id integer primary key autoincrement");

        IndexStatement.Builder builder = new IndexStatement.Builder();
        for (EntityConverter.Column col : cols) {
            if (col.type == EntityConverter.ColumnType.JOIN) {
                continue;
            }
            String name = col.name;
            if (!name.equals(BaseColumns._ID)) {
                sql.append(", '").append(name).append("'");
                sql.append(" ").append(col.type.toString());
            }
            Index index = col.index;
            if (index != null) {
                builder.addIndexedColumn(table, name, index);
            }
        }
        sql.append(");");
        db.execSQL(sql.toString());

        for (IndexStatement stmt : builder.build()) {
            db.execSQL(stmt.getCreationSql(table));
        }
        return true;
    }
}
