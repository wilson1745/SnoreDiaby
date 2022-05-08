package com.bristol.snorediaby.repo.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.bristol.snorediaby.repo.domains.SnoreRecord;
import java.util.List;

/**
 * Function: SnoreRecordDao
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
public interface SnoreRecordDao {

    String TABLE_NAME = "SNORE_RECORD";

    @Query("SELECT * FROM " + TABLE_NAME)
    List<SnoreRecord> findAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE _ID IN (:ids)")
    List<SnoreRecord> findInIds(int[] ids);

    /** 預設萬一執行出錯怎麼辦，REPLACE為覆蓋 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(SnoreRecord... items);

    @Delete
    void delete(SnoreRecord item);

    /** 簡易更新資料的方法 */
    @Update
    void update(SnoreRecord item);

}
