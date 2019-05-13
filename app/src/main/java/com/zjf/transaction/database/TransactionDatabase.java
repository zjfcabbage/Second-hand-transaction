package com.zjf.transaction.database;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.database.dao.MsgDao;
import com.zjf.transaction.database.dao.MsgItemDao;
import com.zjf.transaction.database.entity.Msg;
import com.zjf.transaction.msg.model.MsgItem;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {MsgItem.class, Msg.class}, version = 3, exportSchema = false)
public abstract class TransactionDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "transaction";

    private static volatile TransactionDatabase instance;

    public static synchronized TransactionDatabase getInstance() {
        if (instance == null) {
            synchronized (TransactionDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(AppConfig.context(), TransactionDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return instance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("create table msg(from_id varchar(20), " +
                    "to_id varchar(20), message varchar(500), timestamp bigint)");
        }
    };
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table msg add column to_id varchar(20)");
        }
    };

    public abstract MsgItemDao getMsgItemDao();

    public abstract MsgDao getMsgDao();
}
