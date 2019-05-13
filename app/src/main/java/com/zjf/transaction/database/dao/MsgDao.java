package com.zjf.transaction.database.dao;

import com.zjf.transaction.database.entity.Msg;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface MsgDao {

    @Query("select * from msg where (from_id = :fromId and to_id = :toId)" +
            "or (from_id= :toId and to_id=:fromId) order by id asc")
    Single<List<Msg>> getAll(String fromId, String toId);

    @Insert
    Single<List<Long>> insert(Msg... msg);

    @Query("delete from msg where from_id=:id or to_id=:id")
    Single<Integer> deleteAllMsg(String id);
}
