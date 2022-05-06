package com.bristol.snorediaby.web.view.activities.calculate;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.web.view.activities.personal.PersonalActivity;
import com.bristol.snorediaby.common.constants.SdConstants;
import com.bristol.snorediaby.common.enums.PersonEnum;
import com.bristol.snorediaby.repo.sqllite.CustomSqlLite;
import com.bristol.snorediaby.web.view.AbstractInterface;
import java.util.Objects;

public class CalculateActivity extends AppCompatActivity implements AbstractInterface {

    private final String TAG = this.getClass().getSimpleName();

    private EditText per_age, per_sex, per_height, per_weight;
    private Button btn_cancel, btn_save;
    private int age;
    private String sex, evaluation;
    private double height, weight, bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start CalculateAvtivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calculate);

            findView();

            btn_cancel.setOnClickListener((View v) -> {
                Intent intent = new Intent(CalculateActivity.this, PersonalActivity.class);
                startActivity(intent);
                finish();
            });

            btn_save.setOnClickListener((View v) -> {
                if (per_age.getText().toString().isEmpty()
                    || per_sex.getText().toString().isEmpty()
                    || per_weight.getText().toString().isEmpty()
                    || per_height.getText().toString().isEmpty()) {
                    Toast.makeText(CalculateActivity.this, "Toast：Something cannot be null", Toast.LENGTH_LONG).show();
                } else {
                    BmiEvaluation();
                    Log.e(TAG,
                        "age: " + age + " sex: " + sex + " height: " + height + " weight: " + weight + " BMI: " + bmi + " Evaluation: " + evaluation);
                    CustomSqlLite dbHelp = new CustomSqlLite(CalculateActivity.this);
                    final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
                    Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM person Where _id=?", new String[] { String.valueOf(1) });

                    ContentValues cv = new ContentValues();
                    cv.put(PersonEnum.AGE.getValue(), age);
                    cv.put(PersonEnum.SEX.getValue(), sex);
                    cv.put(PersonEnum.HEIGHT.getValue(), height);
                    cv.put(PersonEnum.WEIGHT.getValue(), weight);
                    cv.put(PersonEnum.BMI.getValue(), bmi);
                    cv.put(PersonEnum.EVALUATION.getValue(), evaluation);

                    if (Objects.equals(0, c.getCount())) {
                        sqLiteDatabase.insert(SdConstants.TABLE_PERSON, null, cv);
                    } else if (Objects.equals(1, c.getCount())) {
                        // FsqLiteDatabase.update(SdConstants.TABLE_PERSON, cv, "_id=" + String.valueOf(1), null);
                        sqLiteDatabase.update(SdConstants.TABLE_PERSON, cv, "_id=" + Integer.valueOf(1).toString(), null);
                    }

                    Intent intent = new Intent(CalculateActivity.this, PersonalActivity.class);
                    startActivity(intent);
                    finish();
                    c.close();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "CalculateAvtivity Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End CalculateAvtivity onCreate");
        }
    }

    private void BmiEvaluation() {
        age = Integer.parseInt(per_age.getText().toString());
        sex = per_sex.getText().toString();
        height = Double.parseDouble(per_height.getText().toString());
        weight = Double.parseDouble(per_weight.getText().toString());

        double heights = height / 100;
        bmi = weight / (heights * heights);

        //下面依照BMI給予Evaluation指示
        if (bmi < 18.5) {
            evaluation = getString(R.string.underweight);
        } else if (bmi >= 18.5 && bmi <= 23.9) {
            evaluation = getString(R.string.normal_weight);
        } else if (bmi >= 24 && bmi <= 27.9) {
            evaluation = getString(R.string.overweight);
        } else if (bmi >= 28) evaluation = getString(R.string.obesity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this, R.string.back_keyDown, Toast.LENGTH_SHORT).show();
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

   /*@Override
   public void onBackPressed() {
      Intent intent = new Intent(CalculateAvtivity.this, PersonalActivity.class);
      startActivity(intent);
      finish();
   }*/

    @Override
    public void findView() {
        per_age = findViewById(R.id.per_age);
        per_sex = findViewById(R.id.per_sex);
        per_height = findViewById(R.id.per_height);
        per_weight = findViewById(R.id.per_weight);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
    }

    @Override
    public void setBackbutton() {
        // Do nothing
    }

    @Override
    public void setText() {
        // Do nothing
    }

}
