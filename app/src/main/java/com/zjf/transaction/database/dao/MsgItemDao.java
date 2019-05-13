package com.zjf.transaction.database.dao;

import com.zjf.transaction.msg.model.MsgItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

@Dao
public interface MsgItemDao {
    @Query("select * from msgitem order by timestamp desc")
    Single<List<MsgItem>> getAll();

    //删除的行数
    @Delete
    Single<Integer> delete(MsgItem item);

    //返回插入的行数
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(MsgItem item);

    @Query("select * from msgitem where userId = (:userId)")
    Single<MsgItem> getItemByUserId(String userId);

    //受影响的行数
    @Update
    Single<Integer> updateItemTimestamp(MsgItem item);
}
