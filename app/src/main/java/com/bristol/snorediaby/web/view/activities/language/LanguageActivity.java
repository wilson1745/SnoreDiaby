package com.bristol.snorediaby.web.view.activities.language;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.bristol.snorediaby.MainActivity;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.common.constants.SdConstants;
import com.bristol.snorediaby.common.enums.LanguageTypeEnum;
import com.bristol.snorediaby.common.exceptions.SnoreException;
import com.bristol.snorediaby.web.view.AbstractInterface;
import java.util.Locale;
import java.util.Objects;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener, AbstractInterface {

    private final String TAG = this.getClass().getSimpleName();

    private RadioGroup lan_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start LanguageActivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_language);
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.language_);

            this.setBackbutton();
            this.findView();
            this.getPreButton();
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        } finally {
            Log.i(TAG, "End LanguageActivity onCreate");
        }
    }

    private final OnCheckedChangeListener radioRCCL = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton checkedRadioButton = lan_group.findViewById(checkedId);
            int checkedIndex = lan_group.indexOfChild(checkedRadioButton);

            SavePreferences(checkedIndex);
        }
    };

    private void SavePreferences(int value) {
        SharedPreferences spPre = getSharedPreferences(SdConstants.RG_CHECK, MODE_PRIVATE);
        SharedPreferences.Editor editor = spPre.edit();
        editor.putInt(SdConstants.BTN_CHECKED, value);
        editor.apply();
    }

    private void getPreButton() {
        SharedPreferences spPre = getSharedPreferences(SdConstants.RG_CHECK, MODE_PRIVATE);
        int savedRadioIndex = spPre.getInt(SdConstants.BTN_CHECKED, 0);
        RadioButton sCRB = (RadioButton) lan_group.getChildAt(savedRadioIndex);
        sCRB.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confir:
                new AlertDialog.Builder(this)
                    .setMessage(R.string.warning_the_system_will)
                    .setPositiveButton(R.string.yes_, (dialog, which) -> checkGroupButton())
                    .setNegativeButton(R.string.no_, (dialog, which) -> {
                        //Do nothing!!!
                    }).show();
                break;
            case R.id.btn_cancel:
            /*Intent intent = new Intent(LanguageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();*/
                break;
        }
    }

    private void checkGroupButton() {
        switch (lan_group.getCheckedRadioButtonId()) {
            case R.id.lan_english:
                switchLanguage(LanguageTypeEnum.ENGLISH.getValue());
                break;
            case R.id.lan_traditional:
                switchLanguage(LanguageTypeEnum.TRAD_CHINESE.getValue());
                break;
            case R.id.lan_simplified:
                switchLanguage(LanguageTypeEnum.SIMP_CHINESE.getValue());
                break;
        }
    }

    private void switchLanguage(String language) {
        //設置應用語言類型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();

        switch (LanguageTypeEnum.getEnum(language)) {
            case ENGLISH:
                config.locale = Locale.ENGLISH;
                saveLanguage(SdConstants.EN); //存入設置語言
                break;
            case TRAD_CHINESE:
                config.locale = Locale.TAIWAN;
                saveLanguage(SdConstants.ZH_TW);
                break;
            case SIMP_CHINESE:
                config.locale = Locale.TAIWAN;
                saveLanguage(SdConstants.ZH_SI);
                break;
            default:
                config.locale = Locale.getDefault();
                break;
        }

        resources.updateConfiguration(config, dm);

        //更新語言後，destroy當前頁面，重新繪製
        finish();

        Intent it = new Intent(LanguageActivity.this, MainActivity.class);

        //清空任務棧確保當前打開activity為前臺任務棧棧頂
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    private void saveLanguage(String lan) {
        SharedPreferences sp = getSharedPreferences(SdConstants.LANGUAGE, MODE_PRIVATE);
        sp.edit().putString(SdConstants.APP_LANGUAGE, lan).apply();
    }

    @Override
    public void findView() {
        lan_group = findViewById(R.id.lan_group);
        lan_group.setOnCheckedChangeListener(radioRCCL);

        Button btn_confirm = findViewById(R.id.btn_confir);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void setBackbutton() {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void setText() {
        // Do nothing
    }

   /*@Override
   public void onBackPressed() {
      int count = getFragmentManager().getBackStackEntryCount();
      if(count == 0) super.onBackPressed();
      else getFragmentManager().popBackStack();
   }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this, R.string.back_keyDown, Toast.LENGTH_SHORT).show();
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override public void writeBean() {
        // TODO
    }

}