package com.bristol.snorediaby.logic.modules;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.bristol.snorediaby.repo.domain.LuxStorage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static android.hardware.Sensor.TYPE_LIGHT;

public class LuxRecorder {

    private final String TAG = this.getClass().getSimpleName();

    private final ArrayList<LuxStorage> luxList;

    private final Context context;

    private SensorEventListener sensorListener = null;

    private Float currentLux = 0f;

    private final boolean luxRanValue;

    private final boolean sleepMode;

    private int luxAlert = 0;

    private final int hourTime;

    private final double luxCheck;

    public LuxRecorder(Context context, boolean luxRanValue, int hourTime, boolean sleepMode, double luxCheck) {
        super();

        this.context = context;
        this.luxRanValue = luxRanValue;
        this.hourTime = hourTime;
        this.sleepMode = sleepMode;
        this.luxCheck = luxCheck;
        this.luxList = new ArrayList<>();
    }

    public void checkLuxAlert() {
        float check = this.getCurrentLux();

        //目前只適合做為夜間睡眠使用
        if (!sleepMode) {
            if (hourTime >= 19 && hourTime < 23 && check > luxCheck) {
                // 夜間時段
                luxAlert++;
            } else if (hourTime >= 23 && check > luxCheck) {
                // 夜間時段
                luxAlert++;
            } else if (hourTime >= 0 && hourTime <= 5 && check > luxCheck) {
                // 凌晨時段
                luxAlert++;
            }
        } else {
            if (check > luxCheck) {
                // 午休時段
                luxAlert++;
            }
        }
    }

    public void setLuxsensor() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (null != sensorManager) {
            Sensor light = sensorManager.getDefaultSensor(TYPE_LIGHT);
            if (!Objects.equals(null, sensorListener)) {
                sensorManager.unregisterListener(sensorListener);
            }

            // 感應器事件監聽器，Android 的 Light Sensor 照度偵測內容只有 values[0] 有意義
            sensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    // 監控感應器改變
                    currentLux = luxRanValue ? new Random().nextFloat() * 150 : event.values[0];
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // TODO nothing to do
                }
            };
            sensorManager.registerListener(sensorListener, light, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            // TODO FIXME
            String str = "TODO";
            //throw  new Exception("");
        }
    }

    public void stop(Context context) {
        // Make sure we don't have any filled variables hanging around
        this.currentLux = 0f;
        luxAlert = 0;
        // If we don't have a listener active we don't nee to unregister it
        if (Objects.equals(null, this.sensorListener)) {
            return;
        }
        //Unregister the listener
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        sensorManager.unregisterListener(sensorListener);

        this.sensorListener = null;
    }

    public void inputList(LuxStorage luxStorage) {
        luxList.add(luxStorage);
    }

    public Float getCurrentLux() {
        return currentLux;
    }

    public ArrayList<LuxStorage> getLuxList() {
        return luxList;
    }

    public int getListSize() {
        return luxList.size();
    }

    public int getLuxAlert() {
        return luxAlert;
    }

}
