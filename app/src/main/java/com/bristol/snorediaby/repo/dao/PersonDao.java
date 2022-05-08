package com.bristol.snorediaby.repo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.bristol.snorediaby.repo.domains.Person;
import com.bristol.snorediaby.repo.domains.Record;
import java.util.List;

/**
 * Function: PersonDao
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
@Dao
public interface PersonDao {

    String TABLE_NAME = "PERSON";

    @Query("SELECT * FROM " + TABLE_NAME)
    List<Person> findAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE _ID IN (:ids)")
    List<Person> findInIds(int[] ids);

    //@Query("SELECT * FROM RECORDS WHERE first_name LIKE :first AND " +
    //    "last_name LIKE :last LIMIT 1")
    //User findByName(String first, String last);

    /** 預設萬一執行出錯怎麼辦，REPLACE為覆蓋 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Person... items);

    @Delete
    void delete(Person item);

    /** 簡易更新資料的方法 */
    @Update
    void update(Person item);

}
