package com.bristol.snorediaby.web.view.activities.data;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.common.constants.SdConstants;
import com.bristol.snorediaby.repo.domains.LuxStorage;
import com.bristol.snorediaby.repo.domains.SnoreStorage;
import com.bristol.snorediaby.repo.domains.beans.SleepAnalysis;
import com.bristol.snorediaby.web.view.fragments.LuxChartFragment;
import com.bristol.snorediaby.web.view.fragments.OSAChartFragment;
import com.bristol.snorediaby.web.view.fragments.PieLuxFragment;
import com.bristol.snorediaby.web.view.fragments.PieSnoreFragment;
import com.bristol.snorediaby.web.view.fragments.SnoreChartFragment;
import com.bristol.snorediaby.repo.sqllite.CustomSqlLite;
import com.bristol.snorediaby.web.view.AbstractInterface;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;

public class DataActivity extends AppCompatActivity implements View.OnClickListener, AbstractInterface {

    private final String TAG = this.getClass().getSimpleName();

    private TextView start_time_v, end_time_v, hsleep_v, suggestion_v;
    private TextView snoretimes_v, diary_v, snoresug_v, luxsug_v, osa_v, osasug_v;
    private EditText diary_ed;
    private Button btn_rev, btn_save, btn_cancel;
    private FragmentTransaction fragmentTransaction;
    private String start_times, end_time, snore_times, snore_per, lux_per, suggestion, diary, OSA;
    private float snore_percent, lux_percent;
    private long id;
    private double hsleep, tsleep;
    private SQLiteDatabase db;
    private String sugSnore, sugLux, sugOSA;
    private ArrayList<SnoreStorage> snoreList;
    private ArrayList<LuxStorage> luxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start DataActivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_data);

            CustomSqlLite helper = new CustomSqlLite(this, SdConstants.TABLE_DB, null, 1);
            db = helper.getReadableDatabase();
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            id = Long.parseLong(bundle.getString(SdConstants.ID));
            getDBdata(id);
            findView();
            btn_rev.setOnClickListener(this);
            btn_save.setOnClickListener(this);
            btn_cancel.setOnClickListener(this);

            SleepAnalysis sA = new SleepAnalysis(this, tsleep, hsleep);
            sA.analyseSleep();
            suggestion = sA.getSuggest();

            //read and store data
            readLuxList();
            readLuxRecords();
            readSnoreList();
            readSnoreRecords();
            readOsaRecords();
            setText();

            //draw chart
            FragmentManager fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            drawSnorePieChart();
            drawLuxPieChart();
            drawLuxChart();
            drawSnoreChart();
            drawOSAChart();
            fragmentTransaction.commit();

            Objects.requireNonNull(getSupportActionBar()).setTitle(start_times);
            setBackbutton();
        } catch (Exception e) {
            Log.e(TAG, "DataActivity Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End DataActivity onCreate");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rev:
                buttonState(true);
                break;
            case R.id.btn_save:
                String dia = diary_ed.getText().toString();
                updateData(dia);
                diary_v.setText(dia);
                buttonState(false);
                break;
            case R.id.btn_cancel:
                buttonState(false);
                break;
        }
    }

    private void updateData(String str) {
        CustomSqlLite dbHelp = new CustomSqlLite(this);
        SQLiteDatabase db2 = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SdConstants.DIARY, str);
        db2.update(SdConstants.RECORDS, cv, SdConstants._ID + " = " + id, null);
    }

    private void buttonState(Boolean state) {
        if (state) {
            diary_v.setVisibility(View.INVISIBLE);
            diary_ed.setVisibility(View.VISIBLE);
            btn_rev.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
        } else {
            diary_v.setVisibility(View.VISIBLE);
            diary_ed.setVisibility(View.INVISIBLE);
            btn_rev.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.INVISIBLE);
            btn_cancel.setVisibility(View.INVISIBLE);
        }
    }

    private void readLuxList() {
        luxList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM lux WHERE _id=?", new String[] { String.valueOf(id) });
        assert cursor != null;
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ByteArrayInputStream fis = new ByteArrayInputStream(cursor.getBlob(1)); //ID(0) NAME(1)
            ObjectInputStream ois = null;

            try {
                ois = new ObjectInputStream(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ois == null) {
                cursor.moveToNext();
                continue;
            }

            luxList = null;

            try {
                luxList = (ArrayList<LuxStorage>) ois.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            cursor.moveToNext();
        }
        cursor.close();
    }

    private void readLuxRecords() {
        String s = lux_per;
        s = s.substring(0, s.length() - 1);
        lux_percent = Float.parseFloat(s);
        float luxAlert = (lux_percent / 100) * luxList.size();

        //針對夜間睡眠 ()
        if (lux_percent > 0) {
            sugLux += getString(R.string.lux_yes);
        } else if (Objects.equals(0, lux_percent)) {
            sugLux += getString(R.string.lux_no);
        }

        if (luxAlert > 5) {
            sugLux += getString(R.string.lux_up);
        }
    }

    private void readSnoreList() {
        snoreList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM snoreblock WHERE _id=?", new String[] { String.valueOf(id) });
        assert cursor != null;
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ByteArrayInputStream fis = new ByteArrayInputStream(cursor.getBlob(1)); //ID(0) NAME(1)
            ObjectInputStream ois = null;

            try {
                ois = new ObjectInputStream(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ois == null) {
                cursor.moveToNext();
                continue;
            }

            snoreList = null;
            try {
                snoreList = (ArrayList<SnoreStorage>) ois.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            cursor.moveToNext();
        }
        cursor.close();
    }

    private void readSnoreRecords() {
        String s = snore_per;
        s = s.substring(0, s.length() - 1);
        snore_percent = Float.parseFloat(s);

        //取得區間中最大值
        int maxNum = snoreList.get(0).getSnore();
        for (int i = 1; i < snoreList.size(); i++) {
            if (maxNum < snoreList.get(i).getSnore()) {
                maxNum = snoreList.get(i).getSnore();
            }
        }
        //一小時的呼吸次數即為960次為基準
        if (maxNum < 96) {
            sugSnore += getString(R.string.snore_none);
        } else if (maxNum < 288) {
            sugSnore += getString(R.string.snore_slight);
        } else if (maxNum < 480) {
            sugSnore += getString(R.string.snore_medium);
        } else {
            sugSnore += getString(R.string.snore_serious);
        }
    }

    private void readOsaRecords() {
        int night = Integer.parseInt(OSA);

        //取得區間中最大值
        int maxNum = snoreList.get(0).getOSA();
        for (int i = 1; i < snoreList.size(); i++) {
            if (maxNum < snoreList.get(i).getOSA()) {
                maxNum = snoreList.get(i).getOSA();
            }
        }

        if (maxNum < 5 && night < 30) {
            sugOSA += getString(R.string.osa_unlike);
        } else if (maxNum >= 5 && maxNum <= 10 || night >= 30 && night < 50) {
            sugOSA += getString(R.string.osa_maybe);
        } else if (maxNum > 10 && night > 50) {
            sugOSA += getString(R.string.osa_yes);
        } else {
            sugOSA += getString(R.string.osa_bug);
        }
    }

    private void getDBdata(long id) {
        Cursor c = db.rawQuery("SELECT * FROM records Where _id=?", new String[] { String.valueOf(id) });
        c.moveToNext();
        start_times = c.getString(1);
        end_time = c.getString(2);
        hsleep = c.getDouble(3);
        tsleep = c.getDouble(4);
        snore_times = c.getString(5);
        snore_per = c.getString(6);
        lux_per = c.getString(8);
        OSA = c.getString(9);
        diary = c.getString(10);

        if (Objects.equals(null, diary)) diary = getString(R.string.dia_null);
        c.close();
    }

    private void drawSnorePieChart() {
        PieSnoreFragment psFragment = new PieSnoreFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(SdConstants.SNORE_PERCENT, snore_percent);
        psFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.snore_pie, psFragment);
    }

    private void drawLuxPieChart() {
        PieLuxFragment plFragment = new PieLuxFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(SdConstants.LUX_PERCENT, lux_percent);
        plFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lux_pie, plFragment);
    }

    private void drawLuxChart() {
        LuxChartFragment lcFragment = new LuxChartFragment();
        Bundle bundle_f = new Bundle();
        bundle_f.putSerializable(SdConstants.LUX_LIST, luxList);
        lcFragment.setArguments(bundle_f);
        fragmentTransaction.add(R.id.lux_chart, lcFragment);
    }

    private void drawSnoreChart() {
        SnoreChartFragment scFragment = new SnoreChartFragment();
        Bundle bundle_f = new Bundle();
        //bundle_f.putLong("_id", id);
        bundle_f.putSerializable(SdConstants.SNORE_LIST, snoreList);
        scFragment.setArguments(bundle_f);
        fragmentTransaction.add(R.id.snore_chart, scFragment);
    }

    private void drawOSAChart() {
        OSAChartFragment osaFragment = new OSAChartFragment();
        Bundle bundle_f = new Bundle();
        //bundle_f.putLong("_id", id);
        bundle_f.putSerializable(SdConstants.OSA_LIST, snoreList);
        osaFragment.setArguments(bundle_f);
        fragmentTransaction.add(R.id.osa_chart, osaFragment);
    }

   /*@Override
   public void onBackPressed() {
      int count = getFragmentManager().getBackStackEntryCount();

      if(count == 0) super.onBackPressed();
      else getFragmentManager().popBackStack();
   }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this, R.string.back_keyDown, Toast.LENGTH_SHORT).show();
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (android.support.v4.app.Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void findView() {
        start_time_v = findViewById(R.id.start_time_v);
        end_time_v = findViewById(R.id.end_time_v);
        hsleep_v = findViewById(R.id.hsleep_v);
        suggestion_v = findViewById(R.id.suggestion_v);
        snoretimes_v = findViewById(R.id.snoretimes_v);
        snoresug_v = findViewById(R.id.snoresug_v);
        luxsug_v = findViewById(R.id.luxsug_v);
        diary_v = findViewById(R.id.diary_view);
        btn_rev = findViewById(R.id.btn_rev);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        diary_ed = findViewById(R.id.diary_ed);
        osa_v = findViewById(R.id.osatimes_v);
        osasug_v = findViewById(R.id.osasug_v);

        sugSnore = SdConstants.BLANK;
        sugLux = SdConstants.BLANK;
        sugOSA = SdConstants.BLANK;
        diary_ed.setText(diary);
    }

    @Override
    public void setBackbutton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void setText() {
        start_time_v.setText(start_times);
        end_time_v.setText(end_time);
        hsleep_v.setText(String.valueOf(hsleep));
        snoretimes_v.setText(snore_times);
        suggestion_v.setText(suggestion);
        luxsug_v.setText(sugLux);
        snoresug_v.setText(sugSnore);

        diary_v.setText(diary);
        osa_v.setText(OSA);
        osasug_v.setText(sugOSA);
    }

    @Override public void writeBean() {
        // TODO
    }

}
