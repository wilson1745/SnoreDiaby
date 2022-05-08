package com.bristol.snorediaby.repo.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.bristol.snorediaby.repo.domains.LuxRecord;
import java.util.List;

/**
 * Function: ListRecordDao
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
public interface LuxRecordDao {

    String TABLE_NAME = "LUX_RECORD";

    @Query("SELECT * FROM " + TABLE_NAME)
    List<LuxRecord> findAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE _ID IN (:ids)")
    List<LuxRecord> findInIds(int[] ids);

    /** 預設萬一執行出錯怎麼辦，REPLACE為覆蓋 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(LuxRecord... items);

    @Delete
    void delete(LuxRecord item);

    /** 簡易更新資料的方法 */
    @Update
    void update(LuxRecord item);

}
