package com.bristol.snorediaby.activities.personal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.activities.calculate.CalculateActivity;
import com.bristol.snorediaby.sqllite.CustomSqlLite;
import com.bristol.snorediaby.view.AbstractInterface;
import java.util.Objects;

public class PersonalActivity extends AppCompatActivity implements AbstractInterface {

    private final String TAG = this.getClass().getSimpleName();

    private Cursor cursor;

    private Button btn_revise;

    private TextView age_v, sex_v, height_v, weight_v, bmi_v, evaluation_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start PersonalActivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_personal);

            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.personal_info);
            setBackbutton();
            findView();

            btn_revise.setOnClickListener(v -> {
                Intent intent = new Intent(PersonalActivity.this, CalculateActivity.class);
                startActivity(intent);
                finish();
            });
        } catch (Exception e) {
            Log.e(TAG, "PersonalActivity Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End PersonalActivity onCreate");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void findView() {
        age_v = findViewById(R.id.age_view);
        sex_v = findViewById(R.id.sex_view);
        height_v = findViewById(R.id.height_view);
        weight_v = findViewById(R.id.weight_view);
        bmi_v = findViewById(R.id.bmi_view);
        evaluation_v = findViewById(R.id.evalution_view);
        btn_revise = findViewById(R.id.btn_revise);

        CustomSqlLite dbHelp = new CustomSqlLite(PersonalActivity.this);
        final SQLiteDatabase db = dbHelp.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM person Where _id=?", new String[] { String.valueOf(1) });

        if (!Objects.equals(0, cursor.getCount())) {
            setText();
        }
        cursor.close();
    }

    @Override
    public void setText() {
        cursor.moveToNext(); // 移到第1筆資料
        age_v.setText(cursor.getString(1));
        sex_v.setText(cursor.getString(2));
        height_v.setText(cursor.getString(3));
        weight_v.setText(cursor.getString(4));
        bmi_v.setText(cursor.getString(5));
        evaluation_v.setText(cursor.getString(6));
    }

    @Override
    public void setBackbutton() {
        if (!Objects.equals(null, getSupportActionBar())) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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

}