package com.bristol.snorediaby.repo.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CustomSqlLite extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getSimpleName();

    private static final String DB_NAME = "DB.db";

    private static final String TABLE_NAME = "records";

    private static final String TABLE_PERSON = "person";

    private static final String TABLE_LUX = "lux";

    private static final String TABLE_SNOREBLOCK = "snoreblock";

    private static final int DB_VERSION = 1;

    public CustomSqlLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public CustomSqlLite(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "myDB 初始化");

        try {
            String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "start_time TEXT,"      //起始時間
                + "end_time TEXT,"        //結束時間
                + "sleepHour DOUBLE,"     //總睡眠小時
                + "timeOfSleep DOUBLE,"   //睡眠起始時間(小時幾點)
                + "snore_times INTEGER,"  //打呼次數
                + "snore_per TEXT,"       //打呼百分比
                + "lux_alert INTERGER, "  //亮度警告
                + "lux_per TEXT,"         //照度百分比
                /*+ "grade DOUBLE, "        //評分*/
                /*+ "suggestion TEXT, "     //建議*/
                + "osa_times TEXT, "      //呼吸中止次數
                + "diary TEXT)";          //日記

            String CREATE_PERSON = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSON
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "age INTEGER,"
                + "sex TEXT,"
                + "height DOUBLE,"
                + "weight DOUBLE,"
                + "bmi DOUBLE,"
                + "evaluation TEXT)";

            String CREATE_LUX = "CREATE TABLE IF NOT EXISTS " + TABLE_LUX
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "luxlist BLOB)";

            String CREATE_SNOREBLOCK = "CREATE TABLE IF NOT EXISTS " + TABLE_SNOREBLOCK
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "snorelist BLOB)";

            db.execSQL(CREATE_PERSON);
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_LUX);
            db.execSQL(CREATE_SNOREBLOCK);
            //Log.e(TAG, "built tables successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.e(TAG, "database update!");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("Drop TABLE IF EXISTS " + TABLE_LUX);
        db.execSQL("Drop TABLE IF EXISTS " + TABLE_SNOREBLOCK);

        onCreate(db);
    }

}
