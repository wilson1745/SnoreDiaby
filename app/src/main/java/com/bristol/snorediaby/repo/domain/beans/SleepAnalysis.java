package com.bristol.snorediaby.repo.domain.beans;

import com.bristol.snorediaby.R;
import com.bristol.snorediaby.web.view.activities.data.DataActivity;

public class SleepAnalysis {

    private final double timeToBed;
    private final double sleepHour;
    //   private Calendar calendar;
    private String suggest;

    //   private double grade_numberOfPlay;
    //   private double grade_subOfAlarm;
    private double grade_sumOfSleep;
    private double grade_timeOfSleep;
    //   private int sleepGrade;
    private final DataActivity dataActivity;

    //入睡時間與第二型糖尿病關係關係沒很大
    public SleepAnalysis(DataActivity dataActivity, double timeToBed, double sleepHour) {
        this.timeToBed = timeToBed;
        this.sleepHour = sleepHour;
        this.dataActivity = dataActivity;

        //      calendar = Calendar.getInstance();
        suggest = "";
    }

    public void analyseSleep() {
        //      grade_numberOfPlay = checkTouch();
        //      grade_subOfAlarm   = checkGetUp();
        grade_sumOfSleep = checkLength();
        grade_timeOfSleep = checkTimeBed();
        //      sleepGrade = countGrade();
    }

    //睡覺碰手機
    //   private double checkTouch() {
    //      int numberOfPlay =  MyReceiver.getNum2();
    //      return pow(1.01, -numberOfPlay);
    //   }

    //   //是否賴床
    //   private double checkGetUp() {
    //      int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
    //      int nowMinute = calendar.get(Calendar.MINUTE);
    //      int subOfAlarm;
    //      double alarmTime = alarmHour + (alarmMinute / 60), nowTime = nowHour + (nowMinute / 60);
    //
    //      if (nowTime > alarmTime) subOfAlarm = (nowHour - alarmHour) * 60 + (nowMinute - alarmMinute);
    //      else if (nowTime == alarmTime) subOfAlarm = Math.abs(nowMinute - alarmMinute);
    //      else subOfAlarm = (alarmHour - nowHour) * 60 + nowMinute - alarmMinute;
    //
    //      double early = subOfAlarm / 20;
    //      if (early > 2) {
    //         suggest += "您的睡眠不深沉，比預定還早起床。";
    //         grade_subOfAlarm = 0.8;
    //      } else if (early > -1) {
    //         suggest += "您在規定時間起床，希望您繼續保持。";
    //         grade_subOfAlarm = 0.99;
    //      } else {
    //         suggest += "您這樣有點賴床。";
    //         grade_subOfAlarm = 0.9;
    //      }
    //
    //      return grade_subOfAlarm;
    //   }

    //睡眠時間長短
    private double checkLength() {
        if (sleepHour >= 9) {
            suggest += dataActivity.getString(R.string.sleep_nine);
            grade_sumOfSleep = 0.78;
        } else if (sleepHour >= 7 && sleepHour < 9) {
            suggest += dataActivity.getString(R.string.sleep_good);
            grade_sumOfSleep = 0.96;
        } else if (sleepHour < 7) {
            suggest += dataActivity.getString(R.string.sleep_short);
            grade_sumOfSleep = 0.5;
        } else {
            suggest += dataActivity.getString(R.string.sleep_bug_first);
            grade_sumOfSleep = 0.0;
        }

        return grade_sumOfSleep;
    }

    //入睡時間
    private double checkTimeBed() {
        if (timeToBed >= 21 && timeToBed < 23) {
            grade_timeOfSleep = 1;
            suggest += dataActivity.getString(R.string.sleep_21);
        } else if (timeToBed >= 23 && timeToBed <= 23.9) {
            grade_timeOfSleep = 0.95;
            suggest += dataActivity.getString(R.string.slee_23);
        } else if (timeToBed >= 0 && timeToBed < 3) {
            grade_timeOfSleep = 0.75;
            suggest += dataActivity.getString(R.string.sleep_0);
        } else if (timeToBed >= 3 && timeToBed < 6) {
            grade_timeOfSleep = 0.65;
            suggest += dataActivity.getString(R.string.sleep_3);
        } else if (timeToBed >= 6 && timeToBed < 11) {
            grade_timeOfSleep = 0.45;
            suggest += dataActivity.getString(R.string.sleep_6);
        } else if (timeToBed >= 11 && timeToBed < 15) {
            grade_timeOfSleep = 1;
            suggest += dataActivity.getString(R.string.sleep_11);
        } else if (timeToBed >= 15 && timeToBed < 21) {
            grade_timeOfSleep = 0.75;
            suggest += dataActivity.getString(R.string.sleep_15);
        } else {
            grade_timeOfSleep = 0.0;
            suggest += dataActivity.getString(R.string.sleep_bug_2);
        }

        return grade_timeOfSleep;
    }

    //   private int countGrade() {
    //      double grade = 100
    //              * grade_subOfAlarm
    //              * grade_sumOfSleep
    //              * pow(grade_numberOfPlay, 0.2);
    //
    //      grade = 10 * pow(grade, 0.5);
    //      grade = Math.round(grade);
    //
    //      double sleepGrade = 100;
    //      sleepGrade = (float) (0.2 * sleepGrade + 0.8 * grade);  //睡眠評分
    //      sleepGrade = Math.round(sleepGrade - (luxAlert * 0.5)); //扣掉打呼和燈光的評分
    //
    //      return (int) sleepGrade;
    //   }

    //   public double getSleepHour() {
    //      return sleepHour;
    //   }
    //
    //   public int getSleepGrade() {
    //      return sleepGrade;
    //   }

    public String getSuggest() {
        return suggest;
    }

}
