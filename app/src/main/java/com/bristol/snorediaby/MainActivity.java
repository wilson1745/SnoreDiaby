package com.bristol.snorediaby;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bristol.snorediaby.fragments.AlarmFragment;
import com.bristol.snorediaby.fragments.ListFragment;
import com.bristol.snorediaby.utils.exception.SnoreException;
import com.bristol.snorediaby.view.advance.AdvanceFragment;
import com.bristol.snorediaby.view.instruct.InstructFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    private final List<Fragment> listFragments = new ArrayList<>();

    private FragmentManager fm;

    private AlarmFragment alarmFragment;

    private ListFragment listFragment;

    private AdvanceFragment advanceFragment;

    private InstructFragment instructFragment;

    private static Boolean isQuit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.activity_main);

            // TODO FIXME
            this.getLanguage();
            this.checkPermission();

            fm = this.getFragmentManager();

            // 添加標籤標籤頁
            BottomNavigationBar bottomNavigationBar = this.findViewById(R.id.bottom);
            bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
            bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
            bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.sleep_pressed, getString(R.string.start_track))
                    .setActiveColorResource(R.color.bottombar).setInactiveIconResource(R.drawable.sleep_normal))
                .addItem(new BottomNavigationItem(R.drawable.analysis_pressed, getString(R.string.sleep_rec))
                    .setActiveColorResource(R.color.bottombar1).setInactiveIconResource(R.drawable.analysis_normal))
                .addItem(new BottomNavigationItem(R.drawable.diary_pressed, getString(R.string.advance_opt))
                    .setActiveColorResource(R.color.bottombar2).setInactiveIconResource(R.drawable.diary_normal))
                .addItem(new BottomNavigationItem(R.drawable.music_pressed, getString(R.string.instruc))
                    .setActiveColorResource(R.color.bottombar3).setInactiveIconResource(R.drawable.music_normal))
                .initialise();

            this.onTabSelected(0);
            bottomNavigationBar.setTabSelectedListener(this);
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        }
    }

    @Override
    public void onTabSelected(int position) {
        try {

            FragmentTransaction transaction = fm.beginTransaction();

            // 先把所有fragment隱藏
            listFragments.parallelStream().forEach(transaction::hide);

            switch (position) {
                case 0:
                    if (alarmFragment == null) {
                        alarmFragment = new AlarmFragment(); //初始化alarmFragment
                        transaction.add(R.id.layFrame, alarmFragment);
                        listFragments.add(alarmFragment);
                    } else {
                        transaction.show(alarmFragment);
                    }
                    break;
                case 1:
                    if (listFragment == null) {
                        listFragment = new ListFragment();
                        transaction.add(R.id.layFrame, listFragment).addToBackStack(null);
                        listFragments.add(listFragment);
                    } else {
                        transaction.remove(listFragment);
                        //listFragment = null;
                        listFragment = new ListFragment();
                        transaction.add(R.id.layFrame, listFragment).addToBackStack(null);
                        int i = checkList(listFragment);
                        listFragments.set(i, listFragment);
                    }
                    break;
                case 2:
                    if (advanceFragment == null) {
                        advanceFragment = new AdvanceFragment();
                        transaction.add(R.id.layFrame, advanceFragment).addToBackStack(null);
                        listFragments.add(advanceFragment);
                    } else {
                        transaction.show(advanceFragment);
                    }
                    break;
                case 3:
                    if (null == instructFragment) {
                        instructFragment = new InstructFragment();
                        transaction.add(R.id.layFrame, instructFragment).addToBackStack(null);
                        listFragments.add(instructFragment);
                    } else {
                        transaction.remove(instructFragment);
                        //instructFragment = null;
                        instructFragment = new InstructFragment();
                        transaction.add(R.id.layFrame, instructFragment).addToBackStack(null);
                        int i = this.checkList(instructFragment);
                        listFragments.set(i, instructFragment);
                    }
                    break;
                default:
                    throw new Exception("Unknown fragment: " + position);

            }

            transaction.commit();
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        }
    }

    @Override
    public void onTabUnselected(int position) {
        // TODO
    }

    @Override
    public void onTabReselected(int position) {
        // TODO
    }

    /* 獲得已保存的語言, 需要在super.onCreate()之前設置語言. */
    private void getLanguage() throws Exception {
        Resources res = this.getResources();
        Configuration cfg = res.getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();

        String language = this.getSharedPreferences("language", MODE_PRIVATE).getString("app_language", "en");
        cfg.locale = language.equals("zh_TW") ? Locale.TAIWAN : Locale.ENGLISH;
        res.updateConfiguration(cfg, dm);
    }

    private void checkPermission() {
        //檢查是否取得權限
        int perAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int perWStor = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int perRStor = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //沒有權限時
        if (perAudio != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO }, 1);
        }

        if (perWStor != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }

        if (perRStor != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
        } else {
            Toast.makeText(this, R.string.get_permission, Toast.LENGTH_SHORT).show();
        }
    }

    private int checkList(Fragment currentFragment) {
        int position = 0;
        for (int i = 0; i < listFragments.size(); i++) {
            if (listFragments.get(i) == currentFragment) {
                position = i;
            }
        }
        return position;
    }

}
