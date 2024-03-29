package com.bristol.snorediaby.fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.beans.Calendars;
import com.bristol.snorediaby.common.enums.ButtonStateEnum;
import com.bristol.snorediaby.common.enums.DrawInstantEnum;
import com.bristol.snorediaby.domain.LuxStorage;
import com.bristol.snorediaby.domain.SnoreStorage;
import com.bristol.snorediaby.modules.LuxRecorder;
import com.bristol.snorediaby.modules.SnoreRecorder;
import com.bristol.snorediaby.services.AlarmService;
import com.bristol.snorediaby.sqllite.CustomSqlLite;
import com.bristol.snorediaby.utils.StringUtil;
import com.bristol.snorediaby.utils.exception.SnoreException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

public class AlarmFragment extends Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private View view;

    private TextView word1, hour_v, mint_v, sec_v, time_v, snore_v, lux_v, sound_v, l_view, s_view, v_view;
    private TextView mao1, mao2, mao3, /*move_v,*/
        osa_v, stopsnore_v;
    private EditText sound_e, luxrange_e, luxcheck_e;
    private Button start, reset, timeBtn;
    private LinearLayout jishi;
    private RadioGroup lux_group, sleep_group, vibra_group;
    private FrameLayout lux_dy;

    private Context context;
    private PendingIntent pending_intent;
    private Intent my_intent;
    //獲取日曆實例;
    private final Calendar calendar = Calendar.getInstance();
    private Date mStartDate;

    private int blockSnore = 0, blockOSA = 0, snoreTatal = 0, valueDB;
    private double setValue = 40, luxRange = 30, luxCheck = 5;
    private float valueLux;
    private double timeToBed;
    private long timeusedinsec;
    private String snorePer, luxPer, start_time, end_time;
    private boolean isstop = false, useVibra = false, luxRanValue = false, sleepMode = false, addEntry = false;

    private Calendars calend;
    private LuxRecorder luxRecorder;
    //   private VibrateRecorder vibrateRecorder;
    private SnoreRecorder snoreRecorder;

    private LuxDyFragment ldFragment;
    private SoundDyFragment sdFragment;
    private FragmentManager fm;

    private final Handler luxHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (Objects.equals(1, msg.what)) {
                setLux("view", valueLux);
            }
            return false;
        }
    });

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            valueLux = luxRecorder.getCurrentLux();
            setLux("add", valueLux); //此處執行任務
            ldFragment.addEntry(valueLux);
            luxHandler.sendEmptyMessage(1);
            luxHandler.postDelayed(this, (long) (luxRange * 1000)); //預設延遲30秒,再次執行task本身,實現了迴圈的效果
        }
    };

    private final Handler vibHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //         move_v.setText(String.valueOf(vibrateRecorder.getShake()));
            if (Objects.equals(1, msg.what)) {
                sound_v.setText(String.valueOf(valueDB));
                if (addEntry) sdFragment.addEntry(valueDB);
            }
            return false;
        }
    });

    private final Runnable task2 = new Runnable() {
        @Override
        public void run() {
            valueDB = snoreRecorder.getVolume();
            vibHandler.sendEmptyMessage(1);
            vibHandler.postDelayed(this, 0);
        }
    };

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!isstop) { // 添加更新ui的代碼
                        updateTimeView();
                        countSnore();
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
                case 0:
                    break;
            }
            return false;
        }
    });

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Start AlarmFragment onCreateView");

        try {
            this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //不讓螢幕熄滅 WakeLock替代品
            view = inflater.inflate(R.layout.fragment_alarm, container, false);
            context = this.getActivity();
            calend = new Calendars();
            fm = getFragmentManager();
            this.init(view);
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        } finally {
            Log.i(TAG, "End AlarmFragment onCreateView");
        }

        return view;
    }

    @Override public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.timeBtn:
                    // 設定on time監聽器
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    new TimePickerDialog(getActivity(), (arg0, hour, minute) -> {
                        String formatTime = calend.formatZero(hour) + ":" + calend.formatZero(minute);
                        timeBtn.setText(formatTime);
                        // 設置日曆的時間，主要是讓日曆的年月日和當前同步
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        // 設置日曆的小時和分鐘
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        // 將秒和毫秒歸0
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                    break;

                case R.id.start:
                    // 開始記錄
                    // this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //不讓螢幕熄滅 WakeLock替代品
                    this.checkFieldValue();
                    this.checkGroupButton();
                    // 開始時間
                    start_time = calend.findCalendar();

                    luxRecorder = new LuxRecorder(context, luxRanValue, calendar.get(Calendar.HOUR_OF_DAY), sleepMode, luxCheck);
                    luxRecorder.setLuxsensor();
                    // 畫即時圖
                    this.drawInstant(DrawInstantEnum.LUX);
                    luxHandler.postDelayed(task, 0); //第一次調用,延遲0秒後執行task

                    //vibrateRecorder = new VibrateRecorder(context);
                    //vibrateRecorder.setVibrasensor();
                    vibHandler.postDelayed(task2, 0);

                    snoreRecorder = new SnoreRecorder(context, setValue, useVibra);
                    snoreRecorder.resolveRecord();
                    // 畫即時圖
                    this.drawInstant(DrawInstantEnum.SOUND);

                    mStartDate = new Date();
                    // 關閉鬧鐘，鬧鐘控制函數
                    this.alarmControl(true);

                    mHandler.removeMessages(1);
                    isstop = false;
                    mHandler.sendEmptyMessage(1);
                    timeToBed = calend.getBedHour();
                    buttonState(ButtonStateEnum.START);
                    break;

                case R.id.reset:
                    // 結束紀錄
                    // this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //關閉螢幕不暗
                    // 結束時間
                    end_time = calend.findCalendar();
                    // 關閉鬧鐘
                    this.alarmControl(false);

                    luxHandler.removeCallbacks(task);
                    // 初始時圖
                    this.drawInstant(DrawInstantEnum.INIT);
                    vibHandler.removeCallbacks(task2);

                    this.stopSnore();
                    this.saveData();

                    luxRecorder.stop(context); //終止LuxRecorder
                    //vibrateRecorder.stop(context);

                    // 參數全初始化
                    isstop = true;
                    useVibra = false;
                    blockSnore = 0;
                    blockOSA = 0;
                    timeusedinsec = 0;

                    this.buttonState(ButtonStateEnum.STOP);
                    break;
                default:
                    throw new Exception("Unknown view.getId()");
            }
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        }
    }

    private void init(View view) {
        jishi = view.findViewById(R.id.jishi);
        hour_v = view.findViewById(R.id.hourt);
        mint_v = view.findViewById(R.id.mint);
        sec_v = view.findViewById(R.id.sec);
        start = view.findViewById(R.id.start);
        reset = view.findViewById(R.id.reset);
        word1 = view.findViewById(R.id.word1);
        // 獲取時間按鈕
        timeBtn = view.findViewById(R.id.timeBtn);
        lux_v = view.findViewById(R.id.lux_view);
        sound_v = view.findViewById(R.id.sound_view);
        time_v = view.findViewById(R.id.time_view);
        snore_v = view.findViewById(R.id.snore_view);
        sound_e = view.findViewById(R.id.sound_ed);
        luxrange_e = view.findViewById(R.id.luxrange_ed);
        lux_group = view.findViewById(R.id.group_lux);
        sleep_group = view.findViewById(R.id.group_mode);
        l_view = view.findViewById(R.id.l_view);
        s_view = view.findViewById(R.id.s_view);
        luxcheck_e = view.findViewById(R.id.luxcheck_ed);
        vibra_group = view.findViewById(R.id.group_vibra);
        v_view = view.findViewById(R.id.v_view);
        // move_v = view.findViewById(R.id.move_view);
        mao1 = view.findViewById(R.id.textView38);
        mao2 = view.findViewById(R.id.textView40);
        mao3 = view.findViewById(R.id.textView37);
        osa_v = view.findViewById(R.id.osa_view);
        stopsnore_v = view.findViewById(R.id.stopsnore_view);
        lux_dy = view.findViewById(R.id.lux_instant);

        timeBtn.setOnClickListener(this);
        start.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    private void checkFieldValue() {
        setValue = StringUtil.isEmpty(sound_e.getText().toString()) ? 50 : Double.parseDouble(sound_e.getText().toString());
        luxRange = StringUtil.isEmpty(luxrange_e.getText().toString()) ? 30 : Double.parseDouble(luxrange_e.getText().toString());
        luxCheck = StringUtil.isEmpty(luxcheck_e.getText().toString()) ? 5 : Double.parseDouble(luxcheck_e.getText().toString());
    }

    private void checkGroupButton() {
        switch (lux_group.getCheckedRadioButtonId()) {
            case R.id.lux_normal:
                luxRanValue = false;
                break;
            case R.id.lux_random:
                luxRanValue = true;
                break;
        }

        switch (sleep_group.getCheckedRadioButtonId()) {
            case R.id.mod_night:
                sleepMode = false;
                break;
            case R.id.mode_any:
                sleepMode = true;
                break;
        }

        switch (vibra_group.getCheckedRadioButtonId()) {
            case R.id.vibra_no:
                useVibra = false;
                break;
            case R.id.vibra_yes:
                useVibra = true;
                break;
        }
    }

    private void buttonState(ButtonStateEnum state) {
        switch (state) {
            case START:
                start.setVisibility(View.GONE);
                reset.setVisibility(View.VISIBLE);
                jishi.setVisibility(View.VISIBLE); //開始計時的字樣
                sound_e.setVisibility(View.GONE);
                luxrange_e.setVisibility(View.GONE);
                // luxcheck_e.setVisibility(View.GONE);
                lux_group.setVisibility(View.GONE);
                mao1.setVisibility(View.GONE);
                l_view.setVisibility(View.GONE);
                sleep_group.setVisibility(View.GONE);
                s_view.setVisibility(View.GONE);
                v_view.setVisibility(View.GONE);
                vibra_group.setVisibility(View.GONE);
                mao2.setVisibility(View.GONE);
                mao3.setVisibility(View.GONE);
                word1.setText(R.string.count_start);
                lux_dy.setVisibility(View.VISIBLE);
                break;
            case STOP:
                reset.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                sound_e.setVisibility(View.VISIBLE);
                luxrange_e.setVisibility(View.VISIBLE);
                // luxcheck_e.setVisibility(View.VISIBLE);
                lux_group.setVisibility(View.VISIBLE);
                mao1.setVisibility(View.VISIBLE);
                l_view.setVisibility(View.VISIBLE);
                sleep_group.setVisibility(View.VISIBLE);
                s_view.setVisibility(View.VISIBLE);
                vibra_group.setVisibility(View.VISIBLE);
                v_view.setVisibility(View.VISIBLE);
                mao2.setVisibility(View.VISIBLE);
                mao3.setVisibility(View.VISIBLE);
                word1.setText(R.string.count_finish);
                lux_dy.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    private void drawInstant(DrawInstantEnum state) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        switch (state) {
            case LUX:
                ldFragment = new LuxDyFragment();
                fragmentTransaction.add(R.id.lux_instant, ldFragment);
                break;

            case SOUND:
                addEntry = true;
                sdFragment = new SoundDyFragment();
                fragmentTransaction.add(R.id.db_instant, sdFragment);
                break;

            case INIT:
                addEntry = false;
                // 初始lux即時圖
                ldFragment.removeDataSet();
                fragmentTransaction.remove(ldFragment);
                fragmentTransaction.remove(sdFragment);
                ldFragment = null;
                sdFragment = null;
                break;
        }

        fragmentTransaction.commit();
    }

    /* 設置鬧鐘. */
    private void alarmControl(boolean state) throws Exception {
        // 獲取鬧鐘管理器
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (null == alarmManager) {
            throw new Exception("(AlarmManager) context.getSystemService(ALARM_SERVICE) is null");
        } else {
            if (state) {
                my_intent = new Intent(context, AlarmService.class);
                my_intent.putExtra("extra", "alarm on");
                pending_intent = PendingIntent.getService(context, 100, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // 超出現在時間就再多加一天
                if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                // 設置鬧鐘start
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            } else {
                // AlarmManager移除
                my_intent.putExtra("extra", "alarm off");
                context.startService(my_intent);
                alarmManager.cancel(pending_intent);
            }
        }
    }

    private void updateTimeView() {
        timeusedinsec += 1;
        time_v.setText(String.valueOf(timeusedinsec));
        int hour = (int) (timeusedinsec / 60 / 60) % 60;
        int minute = (int) (timeusedinsec / 60) % 60;
        int seconds = (int) (timeusedinsec % 60);
        hour_v.setText(calend.formatZero(hour));
        mint_v.setText(calend.formatZero(minute));
        sec_v.setText(calend.formatZero(seconds));
    }

    private void setLux(String cases, Float currentLux) {
        String current_time = calend.findCalendar();

        switch (cases) {
            case "add":
                LuxStorage luxStorage;
                luxStorage = new LuxStorage(current_time, currentLux);
                luxRecorder.inputList(luxStorage);
                break;
            case "view":
                lux_v.setText(String.valueOf(currentLux));
                luxRecorder.checkLuxAlert();
                break;
        }
    }

    private void setSnore(String cases, int cSnore, int cOSA) {
        String current_time = calend.findCalendar();

        switch (cases) {
            case "add":
                SnoreStorage sS = new SnoreStorage(current_time, cSnore, cOSA);
                snoreRecorder.inputList(sS);
                break;
            case "view":
                snore_v.setText(String.valueOf(cSnore));
                osa_v.setText(String.valueOf(cOSA));
                stopsnore_v.setText(String.valueOf(snoreRecorder.getNormalBreath()));
                break;
        }
    }

    private void countSnore() {
        int cS = snoreRecorder.getCountSnoring();
        int bA = snoreRecorder.getBreathAlert();

        setSnore("view", cS, bA); //每1秒改變view一次
        if ((timeusedinsec % 3600) == 0) { //每小時記錄一次
            if (blockSnore == 0) {
                blockSnore = cS; //初始化
            } else {
                blockSnore = cS - blockSnore;
            }

            if (blockOSA == 0) {
                blockOSA = bA;
            } else {
                blockOSA = bA - blockOSA;
            }

            setSnore("add", blockSnore, blockOSA);
            blockSnore = cS; //記錄此時段的size為下一時段做準備
            blockOSA = bA;
        }
    }

    private void stopSnore() {
        snoreRecorder.stopSnoreRecorder();

        int cS = snoreRecorder.getCountSnoring();
        int bA = snoreRecorder.getBreathAlert();
        blockSnore = cS - blockSnore;
        blockOSA = bA - blockOSA;
        setSnore("view", cS, bA);
        setSnore("add", blockSnore, blockOSA);

        float cL = luxRecorder.getCurrentLux();
        setLux("view", cL);
        setLux("add", cL);

        snoreTatal = cS;
        Date mEndDate = new Date(); //結束日期
        double dur = mEndDate.getTime() - mStartDate.getTime(); //睡眠時長(毫秒)
        dur = dur / 1000;
        //打呼百分比
        double vital = snoreRecorder.snorePercent(dur, snoreTatal);
        snorePer = String.format(Locale.CHINA, "%.2f%%", vital); //換算小數點
        //亮度百分比
        float lux_per = (float) luxRecorder.getLuxAlert() / luxRecorder.getListSize() * 100;
        luxPer = String.format(Locale.CHINA, "%.2f%%", lux_per);

        snoreRecorder.clearCountSnoring();
    }

    private void saveData() {
        int lA = luxRecorder.getLuxAlert();
        int bA = snoreRecorder.getBreathAlert();
        double sleepHour = (double) (timeusedinsec / 60 / 60); //get the sleepHour

        CustomSqlLite dbHelp = new CustomSqlLite(getActivity()); //insert into sqlite
        final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("start_time", start_time);
        cv.put("end_time", end_time);
        cv.put("sleepHour", sleepHour);
        cv.put("timeOfSleep", timeToBed);
        cv.put("snore_times", snoreTatal);
        cv.put("snore_per", snorePer);
        cv.put("lux_alert", lA);
        cv.put("lux_per", luxPer);
        cv.put("osa_times", bA);
        sqLiteDatabase.insert("records", null, cv);

        ArrayList<LuxStorage> luxList = luxRecorder.getLuxList();
        try { //單一ArrayList存進sqlite裡
            ByteArrayOutputStream fis = new ByteArrayOutputStream();
            ObjectOutputStream ois = new ObjectOutputStream(fis);
            ois.writeObject(luxList);
            byte[] tbyte2 = fis.toByteArray();

            String sql = "insert into lux values(null, ?);"; // 幾個？就在new object就要加入幾個資料值
            Object[] args = new Object[] { tbyte2 };
            sqLiteDatabase.execSQL(sql, args);
        } catch (Exception e) {
            Log.e(TAG, "lux insert table error");
        }

        ArrayList<SnoreStorage> snoreList = snoreRecorder.getSnoreList();
        try {
            ByteArrayOutputStream fis = new ByteArrayOutputStream();
            ObjectOutputStream ois = new ObjectOutputStream(fis);
            ois.writeObject(snoreList);
            byte[] tbyte3 = fis.toByteArray();

            String sql = "insert into snoreblock values(null, ?);";
            Object[] args = new Object[] { tbyte3 };
            sqLiteDatabase.execSQL(sql, args);
        } catch (Exception e) {
            Log.e(TAG, "snoreblock insert table error");
        }
    }

}
