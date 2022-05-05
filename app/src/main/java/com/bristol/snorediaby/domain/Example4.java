package com.bristol.snorediaby.domain;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.bristol.snorediaby.modules.SnoreRecorder;
import com.bristol.snorediaby.sqllite.CustomSqlLite;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Example4 {

    private static final String TAG = "Example3";
    private final Context context;
    private ArrayList<LuxStorage> listLux;
    private ArrayList<SnoreStorage> listSnore;
    private int luxAlert;

    public Example4(Context context) {
        this.context = context;
    }

    public void run() throws ParseException {
        luxAlert = 0;
        makeDate();
        saveData();
    }

    private void makeDate() throws ParseException {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        int x = 0;
        float lux = 0;
        listLux = new ArrayList<>();
        listSnore = new ArrayList<>();
        String str;
        String check;

        Date beginDate = df.parse("2018.08.15 23:37:46");
        Date endDate = df.parse("2018.08.16 07:27:19");
        Date date = beginDate;

        while (x <= 470) {
            LuxStorage luxStorage;
            if (x < 323) {
                lux = new Random().nextFloat() * 1;
            } else if (x < 344) {
                lux = (float) Math.random() * (20 - 15 + 1) + 15;
            } else if (x < 365) {
                lux = (float) Math.random() * (25 - 20 + 1) + 20;
            } else if (x < 386) {
                lux = (float) Math.random() * (30 - 25 + 1) + 25;
            } else if (x < 407) {
                lux = (float) Math.random() * (35 - 30 + 1) + 30;
            } else if (x < 428) {
                lux = (float) Math.random() * (40 - 35 + 1) + 35;
            } else if (x < 449) {
                lux = (float) Math.random() * (45 - 40 + 1) + 40;
            } else if (x < 469) {
                lux = (float) Math.random() * (50 - 45 + 1) + 45;
            } else if (x == 470) {
                lux = (float) Math.random() * (55 - 50 + 1) + 50;
                str = df.format(endDate);
                luxStorage = new LuxStorage(str, lux);
                listLux.add(luxStorage);
                break;
            }

            str = df.format(date);
            luxStorage = new LuxStorage(str, lux);
            listLux.add(luxStorage);

            c.setTime(date);
            c.add(Calendar.MINUTE, 1); // +1
            date = c.getTime();
            x++;
        }

        for (int i = 0; i < listLux.size(); i++) {
            check = listLux.get(i).getDate();

            switch (check) {
                case "2018.08.16 02:14:46":
                    listLux.get(i).updateLux(27);
                    luxAlert++;
                    break;
                case "2018.08.16 02:15:46":
                    listLux.get(i).updateLux(27);
                    luxAlert++;
                    break;

                case "2018.08.16 03:51:46":
                    listLux.get(i).updateLux(23);
                    luxAlert++;
                    break;
                case "2018.08.16 03:52:46":
                    listLux.get(i).updateLux(27);
                    luxAlert++;
                    break;
            }
        }

        String[] date1 = {
            "2018.08.16 00:37:46",
            "2018.08.16 01:37:46",
            "2018.08.16 02:37:46",
            "2018.08.16 03:37:46",
            "2018.08.16 04:37:46",
            "2018.08.16 05:37:46",
            "2018.08.16 06:37:46",
            "2018.08.16 07:27:19"
        };

        int[] snore = { 0, 201, 157, 59, 78, 0, 0, 0 };
        int[] osa = { 0, 2, 3, 0, 1, 0, 0, 0 };

        for (int i = 0; i < snore.length; i++) {
            SnoreStorage snoreStorage = new SnoreStorage(date1[i], snore[i], osa[i]);
            listSnore.add(snoreStorage);
        }
    }

    private void saveData() {
        double timeUseSec = 28200;
        String start_time = "2018.08.15 23:37:46";
        String end_time = "2018.08.16 07:27:19";
        double sleepHour = 7.8;
        int timeToBed = 0;
        String snoreTimes = "495";
        String osaTimes = "6";

        int snore = Integer.parseInt(snoreTimes);
        SnoreRecorder sR = new SnoreRecorder(context, 0, false);
        double vital = sR.snorePercent(timeUseSec, snore);
        String snorePer = String.format(Locale.CHINA, "%.2f%%", vital);

        float lux_per = (float) luxAlert / listLux.size() * 100;
        String luxPer = String.format(Locale.CHINA, "%.2f%%", lux_per);

        CustomSqlLite dbHelp = new CustomSqlLite(context);
        final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("start_time", start_time);
        cv.put("end_time", end_time);
        cv.put("sleepHour", sleepHour);
        cv.put("timeOfSleep", timeToBed);
        cv.put("snore_times", snoreTimes);
        cv.put("snore_per", snorePer);
        cv.put("lux_alert", luxAlert);
        cv.put("lux_per", luxPer);
        cv.put("osa_times", osaTimes);
        sqLiteDatabase.insert("records", null, cv);

        if (listLux != null) {
            try {
                ByteArrayOutputStream fis = new ByteArrayOutputStream();
                ObjectOutputStream ois = new ObjectOutputStream(fis);
                ois.writeObject(listLux);
                byte[] tbyte2 = fis.toByteArray();
                String sql = "insert into lux values(null, ?);"; // 幾個？就在new object就要加入幾個資料值
                Object[] args = new Object[] { tbyte2 };
                sqLiteDatabase.execSQL(sql, args);
            } catch (Exception e) {
                Log.e(TAG, "lux insert table error");
            }
        }
        if (listSnore != null) {
            try {
                ByteArrayOutputStream fis = new ByteArrayOutputStream();
                ObjectOutputStream ois = new ObjectOutputStream(fis);
                ois.writeObject(listSnore);
                byte[] tbyte3 = fis.toByteArray();
                String sql = "insert into snoreblock values(null, ?);";
                Object[] args = new Object[] { tbyte3 };
                sqLiteDatabase.execSQL(sql, args);
            } catch (Exception e) {
                Log.e(TAG, "snoreblock insert table error");
            }
        }
    }
}
