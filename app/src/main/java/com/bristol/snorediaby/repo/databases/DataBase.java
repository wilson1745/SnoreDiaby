package com.bristol.snorediaby.repo.databases;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import com.bristol.snorediaby.repo.dao.LuxRecordDao;
import com.bristol.snorediaby.repo.dao.PersonDao;
import com.bristol.snorediaby.repo.dao.RecordDao;
import com.bristol.snorediaby.repo.dao.SnoreRecordDao;
import com.bristol.snorediaby.repo.domains.LuxRecord;
import com.bristol.snorediaby.repo.domains.Person;
import com.bristol.snorediaby.repo.domains.Record;
import com.bristol.snorediaby.repo.domains.SnoreRecord;

/**
 * Function: DataBase
 * Description: https://ithelp.ithome.com.tw/articles/10222906
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
@Database(
    entities = {
        Record.class,
        LuxRecord.class,
        SnoreRecord.class,
        Person.class
    },
    version = 2
)
public abstract class DataBase extends RoomDatabase {

    private static final String DB_NAME = "RoomDB.db";

    private static volatile DataBase instance;

    public static synchronized DataBase getInstance(Context context) {
        if (instance == null) {
            // 創立新的資料庫
            instance = create(context);
        }

        //if (null == instance) {
        //    instance = Room.databaseBuilder(
        //        context.getApplicationContext(),
        //        DataBase.class, DB_NAME).addMigrations(MARGIN_1to2).build();
        //}

        return instance;
    }

    private static DataBase create(final Context context) {
        return Room.databaseBuilder(context, DataBase.class, DB_NAME).build();
    }

    private static Migration MARGIN_1to2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE \"MyTable\"  ADD COLUMN age INTEGER NOT NULL DEFAULT 18");
        }
    };

    // 設置對外接口
    public abstract RecordDao getRecordsDao();

    public abstract LuxRecordDao getLuxRecordDao();

    public abstract SnoreRecordDao getSnoreRecordDao();

    public abstract PersonDao getPersonDao();

}
