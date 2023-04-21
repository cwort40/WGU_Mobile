package com.c196.wgu_mobile.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.entity.AssessmentEntity;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.viewmodel.AssessmentViewModel;
import com.c196.wgu_mobile.viewmodel.CourseViewModel;
import com.c196.wgu_mobile.viewmodel.TermViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView directionsTextView = findViewById(R.id.text_directions);

        directionsTextView.setText(R.string.app_instructions);

        CourseViewModel mCourseViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);

        Calendar currentCalendar = Calendar.getInstance();

        mCourseViewModel.getAllCourses().observe(this, courses -> {
            for (CourseEntity course : courses) {
                Date startDate = course.getStartDate();
                Calendar courseCalendar = Calendar.getInstance();
                courseCalendar.setTime(startDate);

                if (currentCalendar.get(Calendar.YEAR) == courseCalendar.get(Calendar.YEAR)
                        && currentCalendar.get(Calendar.MONTH) == courseCalendar.get(Calendar.MONTH)
                        && currentCalendar.get(Calendar.DAY_OF_MONTH) == courseCalendar
                        .get(Calendar.DAY_OF_MONTH)) {
                    // create a toast message
                    Toast.makeText(this, "ALERT: Course " + course.getTitle()
                                    + " is starting today!",
                            Toast.LENGTH_LONG).show();
                }

                Date endDate = course.getEndDate();
                courseCalendar.setTime(endDate);

                if (currentCalendar.get(Calendar.YEAR) == courseCalendar.get(Calendar.YEAR)
                        && currentCalendar.get(Calendar.MONTH) == courseCalendar.get(Calendar.MONTH)
                        && currentCalendar.get(Calendar.DAY_OF_MONTH) == courseCalendar
                        .get(Calendar.DAY_OF_MONTH)) {
                    // create a toast message
                    Toast.makeText(this, "ALERT: Course " + course.getTitle()
                                    + " is ending today!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        AssessmentViewModel mAssessmentViewModel = new ViewModelProvider(this)
                .get(AssessmentViewModel.class);

        mAssessmentViewModel.getAllAssessments().observe(this, assessments -> {
            for (AssessmentEntity assessment : assessments) {
                Date startDate = assessment.getStartDate();
                Calendar assessmentCalendar = Calendar.getInstance();
                assessmentCalendar.setTime(startDate);

                if (currentCalendar.get(Calendar.YEAR) == assessmentCalendar.get(Calendar.YEAR)
                        && currentCalendar.get(Calendar.MONTH) == assessmentCalendar
                        .get(Calendar.MONTH)
                        && currentCalendar.get(Calendar.DAY_OF_MONTH) == assessmentCalendar
                        .get(Calendar.DAY_OF_MONTH)) {
                    // create a toast message
                    Toast.makeText(this, "ALERT: Assessment " + assessment.getTitle() +
                                    " is starting today!",
                            Toast.LENGTH_LONG).show();
                }

                Date endDate = assessment.getEndDate();
                assessmentCalendar.setTime(endDate);

                if (currentCalendar.get(Calendar.YEAR) == assessmentCalendar.get(Calendar.YEAR)
                        && currentCalendar.get(Calendar.MONTH) == assessmentCalendar
                        .get(Calendar.MONTH)
                        && currentCalendar.get(Calendar.DAY_OF_MONTH) == assessmentCalendar
                        .get(Calendar.DAY_OF_MONTH)) {
                    // create a toast message
                    Toast.makeText(this, "ALERT: Assessment " + assessment.getTitle() +
                                    " is ending today!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.navigation_terms) {
            Intent intent1 = new Intent(this, TermsActivity.class);
            startActivity(intent1);
        } else if (item.getItemId() == R.id.navigation_courses) {
            Intent intent2 = new Intent(this, CoursesActivity.class);
            startActivity(intent2);
        } else if (item.getItemId() == R.id.navigation_assessments) {
            Intent intent3 = new Intent(this, AssessmentsActivity.class);
            startActivity(intent3);
        }
        return true;
    }

}

