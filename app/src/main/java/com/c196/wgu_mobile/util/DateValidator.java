package com.c196.wgu_mobile.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.c196.wgu_mobile.ui.course.NewCourseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator {
    private final Context mContext;

    public DateValidator(Context context) {
        mContext = context;
    }

    public boolean dateValidator(Date startDate, Date endDate) {
        if(startDate.before(endDate)) {
            return true;
        } else {
            Toast.makeText(mContext, "Start date must be before end date.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

