package com.zjf.transaction.database;

import com.zjf.transaction.app.AppConfig;
import com.zjf.transaction.database.entity.MsgItemDao;
import com.zjf.transaction.msg.model.MsgItem;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MsgItem.class}, version = 1, exportSchema = false)
public abstract class TransactionDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "transaction";

    private static volatile TransactionDatabase instance;

    public static synchronized TransactionDatabase getInstance() {
        if (instance == null) {
            synchronized (TransactionDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(AppConfig.context(), TransactionDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract MsgItemDao getMsgItemDao();
}
