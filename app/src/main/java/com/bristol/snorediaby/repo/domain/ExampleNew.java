package com.bristol.snorediaby.repo.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.bristol.snorediaby.common.constants.DateTimeFormat;
import com.bristol.snorediaby.logic.modules.SnoreRecorder;
import com.bristol.snorediaby.repo.sqllite.CustomSqlLite;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Function: ExampleNew
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
public class ExampleNew extends BaseExample {

    private final String TAG = this.getClass().getSimpleName();

    private static final String[] DATES = {
        "2018.08.13 23:12:33",
        "2018.08.14 00:12:33",
        "2018.08.14 01:12:33",
        "2018.08.14 02:12:33",
        "2018.08.14 03:12:33",
        "2018.08.14 04:12:33",
        "2018.08.14 05:12:33",
        "2018.08.14 06:12:33",
        "2018.08.14 07:12:33",
        "2018.08.14 08:05:23"
    };

    private static final int[] SNORES = { 0, 259, 337, 228, 43, 53, 50, 0, 0, 0 };

    private static final int[] OSAS = { 0, 9, 10, 11, 3, 1, 0, 0, 0, 0 };

    public ExampleNew(Context context) {
        super(context);
    }

    @Override
    protected void run() throws Exception {
        this.makeDate();
        this.saveData();
    }

    @Override
    protected void makeDate() throws Exception {
        Calendar c = Calendar.getInstance();
        DateFormat df = DateTimeFormat.YYYYMMDD_HHMMSS;
        Date beginDate = df.parse("2018.08.13 22:12:33");
        Date endDate = df.parse("2018.08.14 08:05:23");
        Date date = beginDate;

        int x = 0;
        float lux = 0;
        String str;
        LuxStorage luxStorage;
        while (x <= 592) {
            if (x < 408) {
                lux = new Random().nextFloat() * 1;
            } else if (x < 434) {
                lux = (float) Math.random() * (20 - 15 + 1) + 15;
            } else if (x < 460) {
                lux = (float) Math.random() * (25 - 20 + 1) + 20;
            } else if (x < 486) {
                lux = (float) Math.random() * (30 - 25 + 1) + 25;
            } else if (x < 510) {
                lux = (float) Math.random() * (35 - 30 + 1) + 30;
            } else if (x < 534) {
                lux = (float) Math.random() * (40 - 35 + 1) + 35;
            } else if (x < 560) {
                lux = (float) Math.random() * (45 - 40 + 1) + 40;
            } else if (x < 591) {
                lux = (float) Math.random() * (50 - 45 + 1) + 45;
            } else if (x == 592) {
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

        // Prepare lux records
        listLux.parallelStream().forEach(item -> {
            String checkTime = item.getDate();
            switch (checkTime) {
                case "2018.08.14 00:12:33":
                case "2018.08.14 01:34:33":
                case "2018.08.14 02:51:33":
                case "2018.08.14 02:31:33":
                case "2018.08.14 02:33:33":
                case "2018.08.14 04:07:33":
                    item.updateLux(24);
                    break;
                case "2018.08.14 00:13:33":
                case "2018.08.14 01:35:33":
                case "2018.08.14 02:50:33":
                case "2018.08.14 03:30:33":
                case "2018.08.14 03:32:33":
                    item.updateLux(23);
                    break;
                case "2018.08.14 00:14:33":
                case "2018.08.14 04:08:33":
                case "2018.08.14 01:36:33":
                case "2018.08.14 04:06:33":
                    item.updateLux(25);
                    break;
                default:
                    // TODO
                    break;
            }

            luxAlert.getAndIncrement();
        });

        // Prepare snore records
        AtomicInteger index = new AtomicInteger(0);
        listSnore.addAll(Arrays.stream(DATES)
            .map(dateStr -> new SnoreStorage(dateStr, SNORES[index.get()], OSAS[index.getAndIncrement()]))
            .collect(Collectors.toList()));
    }

    @Override
    protected void saveData() throws Exception {
        double timeUseSec = 35580;
        String snoreTimes = "970";
        SnoreRecorder sR = new SnoreRecorder(context, 0, false);
        double vital = sR.snorePercent(timeUseSec, Integer.parseInt(snoreTimes));

        float lux_per = (float) luxAlert.get() / listLux.size() * 100;
        String luxPer = String.format(Locale.CHINA, "%.2f%%", lux_per);
        CustomSqlLite dbHelp = new CustomSqlLite(context);
        final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("start_time", "2018.08.13 22:12:33");
        cv.put("end_time", "2018.08.14 08:05:23");
        cv.put("sleepHour", 9.9);
        cv.put("timeOfSleep", 0);
        cv.put("snore_times", "970");
        cv.put("snore_per", String.format(Locale.CHINA, "%.2f%%", vital));
        cv.put("lux_alert", luxAlert.get());
        cv.put("lux_per", luxPer);
        cv.put("osa_times", 34);
        sqLiteDatabase.insert("records", null, cv);

        if (!CollectionUtils.isEmpty(listLux)) {
            try {
                ByteArrayOutputStream fis = new ByteArrayOutputStream();
                ObjectOutputStream ois = new ObjectOutputStream(fis);
                ois.writeObject(listLux);
                byte[] bytes = fis.toByteArray();
                // 幾個?就在new object就要加入幾個資料值
                String sql = "insert into lux values(null, ?);";
                sqLiteDatabase.execSQL(sql, new Object[] { bytes });
            } catch (Exception e) {
                Log.e(TAG, "lux insert table error");
            }
        }

        if (!CollectionUtils.isEmpty(listSnore)) {
            try {
                ByteArrayOutputStream fis = new ByteArrayOutputStream();
                ObjectOutputStream ois = new ObjectOutputStream(fis);
                ois.writeObject(listSnore);
                byte[] bytes = fis.toByteArray();
                String sql = "insert into snoreblock values(null, ?);";
                sqLiteDatabase.execSQL(sql, new Object[] { bytes });
            } catch (Exception e) {
                Log.e(TAG, "snoreblock insert table error");
            }
        }
    }

}
