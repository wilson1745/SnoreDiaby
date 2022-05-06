package com.bristol.snorediaby.repo.beans;

import java.util.Calendar;

public class Calendars {

    public String findCalendar() {
        Calendar cl = Calendar.getInstance();

        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH) + 1;
        int day = cl.get(Calendar.DAY_OF_MONTH);
        int hours = cl.get(Calendar.HOUR_OF_DAY);
        int minutes = cl.get(Calendar.MINUTE);
        int seconds = cl.get(Calendar.SECOND);

        String month_s = formatZero(month);
        String day_s = formatZero(day);
        String hours_s = formatZero(hours);
        String minutes_s = formatZero(minutes);
        String second_s = formatZero(seconds);

        return year + "." + month_s + "." + day_s + " " + hours_s + ":" + minutes_s + ":" + second_s;
    }

    public String formatZero(int time) {
        return time < 10 ? "0" + time : String.valueOf(time);
    }

    public double getBedHour() {
        Calendar cl = Calendar.getInstance();

        double timeOfSleep = cl.get(Calendar.HOUR_OF_DAY);
        double timeMinOfSleep = cl.get(Calendar.MINUTE);
        timeMinOfSleep = timeMinOfSleep / 60;

        return timeOfSleep + timeMinOfSleep;
    }

}
