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
import com.bristol.snorediaby.common.constants.SdConstants;
import com.bristol.snorediaby.common.enums.PersonEnum;
import com.bristol.snorediaby.common.exceptions.SnoreException;
import com.bristol.snorediaby.repo.sqllite.CustomSqlLite;
import com.bristol.snorediaby.web.view.AbstractInterface;
import com.bristol.snorediaby.web.view.activities.calculate.mvp.CalculateModel;
import com.bristol.snorediaby.web.view.activities.personal.PersonalActivity;
import java.util.Objects;

/**
 * Function: CalculateActivity
 * Description:
 * Author: wilso
 * Date: 2022/5/6
 * MaintenancePersonnel: wilso
 */
public class CalculateActivity extends AppCompatActivity implements AbstractInterface {

    private final String TAG = this.getClass().getSimpleName();

    private EditText perAge;
    private EditText perSex;
    private EditText perHeight;
    private EditText perWeight;

    private Button cancelBtn;
    private Button saveBtn;

    private CalculateModel model;

    private CalculatePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Start CalculateAvtivity onCreate");
        try {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.activity_calculate);

            model = new CalculateModel();
            presenter = new CalculatePresenter(this, model);

            this.findView();
            this.initListener();
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        } finally {
            Log.d(TAG, "End CalculateAvtivity onCreate");
        }
    }

    private void initListener() {
        cancelBtn.setOnClickListener((View v) -> {
            Intent intent = new Intent(CalculateActivity.this, PersonalActivity.class);
            startActivity(intent);
            finish();
        });

        saveBtn.setOnClickListener((View v) -> {
            try {
                this.validate();

                this.writeBean();

                CustomSqlLite dbHelp = new CustomSqlLite(CalculateActivity.this);
                final SQLiteDatabase sqLiteDatabase = dbHelp.getWritableDatabase();
                Cursor c =
                    sqLiteDatabase.rawQuery("SELECT * FROM person Where _id=?", new String[] { String.valueOf(1) });

                ContentValues cv = new ContentValues();
                cv.put(PersonEnum.AGE.getValue(), model.getAge());
                cv.put(PersonEnum.SEX.getValue(), model.getSex());
                cv.put(PersonEnum.HEIGHT.getValue(), model.getHeight());
                cv.put(PersonEnum.WEIGHT.getValue(), model.getWeight());
                cv.put(PersonEnum.BMI.getValue(), model.getBmi());
                cv.put(PersonEnum.EVALUATION.getValue(), model.getEvaluation());

                if (Objects.equals(0, c.getCount())) {
                    sqLiteDatabase.insert(SdConstants.TABLE_PERSON, null, cv);
                } else if (Objects.equals(1, c.getCount())) {
                    // FsqLiteDatabase.update(SdConstants.TABLE_PERSON, cv, "_id=" + String.valueOf(1), null);
                    sqLiteDatabase.update(SdConstants.TABLE_PERSON, cv, "_id=" + Integer.valueOf(1).toString(), null);
                }

                Intent intent = new Intent(CalculateActivity.this, PersonalActivity.class);
                this.startActivity(intent);
                this.finish();
                c.close();
            } catch (IllegalStateException e) {
                Toast.makeText(CalculateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                SnoreException.getErrorException(TAG, e);
                Toast.makeText(CalculateActivity.this, "System fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validate() throws IllegalStateException {
        if (perAge.getText().toString().isEmpty()
            || perSex.getText().toString().isEmpty()
            || perWeight.getText().toString().isEmpty()
            || perHeight.getText().toString().isEmpty()) {
            throw new IllegalStateException("Some fields cannot be null!");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this, R.string.back_keyDown, Toast.LENGTH_SHORT).show();
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    public void findView() {
        perAge = this.findViewById(R.id.per_age);
        perSex = this.findViewById(R.id.per_sex);
        perHeight = this.findViewById(R.id.per_height);
        perWeight = this.findViewById(R.id.per_weight);
        cancelBtn = this.findViewById(R.id.btn_cancel);
        saveBtn = this.findViewById(R.id.btn_save);
    }

    @Override
    public void setBackbutton() {
        // Do nothing
    }

    @Override
    public void setText() {
        // Do nothing
    }

    @Override
    public void writeBean() {
        model.setAge(Integer.parseInt(perAge.getText().toString()));
        model.setSex(perSex.getText().toString());
        model.setHeight(Double.parseDouble(perHeight.getText().toString()));
        model.setWeight(Double.parseDouble(perWeight.getText().toString()));

        double heights = model.getHeight() / 100;
        double bmi = model.getWeight() / (heights * heights);
        model.setBmi(bmi);

        // 下面依照BMI給予Evaluation指示
        if (bmi < 18.5) {
            model.setEvaluation(this.getString(R.string.underweight));
        } else if (bmi >= 18.5 && bmi <= 23.9) {
            model.setEvaluation(this.getString(R.string.normal_weight));
        } else if (bmi >= 24 && bmi <= 27.9) {
            model.setEvaluation(this.getString(R.string.overweight));
        } else if (bmi >= 28) {
            model.setEvaluation(this.getString(R.string.obesity));
        }

        Log.d(TAG, model.toString());
    }

    public EditText getPerAge() {
        return perAge;
    }

    public void setPerAge(EditText perAge) {
        this.perAge = perAge;
    }

    public EditText getPerSex() {
        return perSex;
    }

    public void setPerSex(EditText perSex) {
        this.perSex = perSex;
    }

    public EditText getPerHeight() {
        return perHeight;
    }

    public void setPerHeight(EditText perHeight) {
        this.perHeight = perHeight;
    }

    public EditText getPerWeight() {
        return perWeight;
    }

    public void setPerWeight(EditText perWeight) {
        this.perWeight = perWeight;
    }

}
