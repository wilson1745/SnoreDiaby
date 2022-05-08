package com.bristol.snorediaby.repo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.bristol.snorediaby.repo.domains.Record;
import java.util.List;

/**
 * Function: RecordsDao
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
@Dao
public interface RecordDao {

    String TABLE_NAME = "RECORD";

    @Query("SELECT * FROM " + TABLE_NAME)
    List<Record> findAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE _ID IN (:ids)")
    List<Record> findInIds(int[] ids);

    //@Query("SELECT * FROM RECORDS WHERE first_name LIKE :first AND " +
    //    "last_name LIKE :last LIMIT 1")
    //User findByName(String first, String last);

    /** 預設萬一執行出錯怎麼辦，REPLACE為覆蓋 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Record... items);

    @Delete
    void delete(Record item);

    /** 簡易更新資料的方法 */
    @Update
    void update(Record item);

}
