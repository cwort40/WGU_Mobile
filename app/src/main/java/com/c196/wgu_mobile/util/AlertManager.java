package com.c196.wgu_mobile.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.c196.wgu_mobile.entity.AssessmentEntity;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlertManager {
    private final Context mContext;
    private final AlarmManager mAlarmManager;

    public AlertManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    public void setCourseStartAlert(Date startDate) {
        Intent intentStart = new Intent(mContext, AlertReceiver.class);
        intentStart.putExtra("description", "Course starting today!");
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(mContext,
                (int) startDate.getTime(),
                intentStart, PendingIntent.FLAG_UPDATE_CURRENT);

        // set the alarm to start at the start date of the course
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);

        // get current date
        Calendar currentCalendar = Calendar.getInstance();

        // check if the start date is the current day
        if (calendarStart.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                && calendarStart.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
                && calendarStart.get(Calendar.DAY_OF_MONTH) == currentCalendar
                .get(Calendar.DAY_OF_MONTH)) {
            // create a toast message
            Toast.makeText(mContext, "ALERT: Course is starting today!",
                    Toast.LENGTH_LONG).show();
        }

        //start
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarStart.getTimeInMillis(),
                pendingIntentStart);
    }

    public void setCourseEndAlert(Date endDate) {
        Intent intentEnd = new Intent(mContext, AlertReceiver.class);
        intentEnd.putExtra("description", "Course ending today!");
        PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(mContext,
                (int) endDate.getTime(),
                intentEnd, PendingIntent.FLAG_UPDATE_CURRENT);

        // set the alarm to start at the end date of the course
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);

        // get current date
        Calendar currentCalendar = Calendar.getInstance();

        // check if the end date is the current day
        if (calendarEnd.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                && calendarEnd.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
                && calendarEnd.get(Calendar.DAY_OF_MONTH) == currentCalendar
                .get(Calendar.DAY_OF_MONTH)) {
            // create a toast message
            Toast.makeText(mContext, "ALERT: Course is ending today!",
                    Toast.LENGTH_LONG).show();
        }

        //end
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarEnd.getTimeInMillis(),
                pendingIntentEnd);
    }




    public void setAssessmentStartAlert(Date startDate) {
        Intent intentStart = new Intent(mContext, AlertReceiver.class);
        intentStart.putExtra("description", "Assessment starting today!");
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(mContext,
                (int) startDate.getTime(),
                intentStart, PendingIntent.FLAG_UPDATE_CURRENT);

        // set the alarm to start at the start date of the course
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);

        // get current date
        Calendar currentCalendar = Calendar.getInstance();

        // check if the start date is the current day
        if (calendarStart.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                && calendarStart.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
                && calendarStart.get(Calendar.DAY_OF_MONTH) == currentCalendar
                .get(Calendar.DAY_OF_MONTH)) {
            // create a toast message
            Toast.makeText(mContext, "ALERT: Assessment is starting today!",
                    Toast.LENGTH_LONG).show();
        }

        //start
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarStart.getTimeInMillis(),
                pendingIntentStart);
    }

    public void setAssessmentEndAlert(Date endDate) {
        Intent intentEnd = new Intent(mContext, AlertReceiver.class);
        intentEnd.putExtra("description", "Assessment ending today!");
        PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(mContext,
                (int) endDate.getTime(),
                intentEnd, PendingIntent.FLAG_UPDATE_CURRENT);

        // set the alarm to start at the end date of the course
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);

        // get current date
        Calendar currentCalendar = Calendar.getInstance();

        // check if the end date is the current day
        if (calendarEnd.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                && calendarEnd.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
                && calendarEnd.get(Calendar.DAY_OF_MONTH) == currentCalendar
                .get(Calendar.DAY_OF_MONTH)) {
            // create a toast message
            Toast.makeText(mContext, "ALERT: Assessment is ending today!",
                    Toast.LENGTH_LONG).show();
        }

        //end
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarEnd.getTimeInMillis(),
                pendingIntentEnd);
    }

    public void cancelAlert(int id) {
        Intent intentStart = new Intent(mContext, AlertReceiver.class);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(mContext, id, intentStart,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(pendingIntentStart);
        Intent intentEnd = new Intent(mContext, AlertReceiver.class);
        PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(mContext, id+1,
                intentEnd,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(pendingIntentEnd);
    }

}

