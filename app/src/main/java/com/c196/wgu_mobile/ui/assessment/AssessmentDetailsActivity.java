package com.c196.wgu_mobile.ui.assessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.entity.AssessmentEntity;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.viewmodel.CourseViewModel;

import java.util.concurrent.atomic.AtomicReference;

public class AssessmentDetailsActivity extends AppCompatActivity {

    private CourseViewModel mCourseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AssessmentEntity selectedAssessment = (AssessmentEntity)getIntent()
                .getSerializableExtra("selected_assessment");
        if (selectedAssessment == null) {
            // Handle error: no assessment was passed in the intent
            System.out.println("no assessment passed");

        }

        TextView assessmentTitleView = findViewById(R.id.assessment_title);
        assessmentTitleView.setText(selectedAssessment.getTitle());

        TextView assessmentStartDateView = findViewById(R.id.assessment_start_date);
        assessmentStartDateView.setText(DateConverter.dateToTimestamp(selectedAssessment.getStartDate()));

        TextView assessmentEndDateView = findViewById(R.id.assessment_end_date);
        assessmentEndDateView.setText(DateConverter.dateToTimestamp(selectedAssessment.getEndDate()));

        TextView assessmentTypeView = findViewById(R.id.assessment_type);
        assessmentTypeView.setText(selectedAssessment.getType());

        TextView assessmentAssociatedCourseView = findViewById(R.id.assessment_associated_course);
        int courseId = selectedAssessment.getCourse_id();
        final String[] courseTitle = new String[1];
        //create allCoursesList
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mCourseViewModel.getAllCourses().observe(this, courseEntities -> {
            for (CourseEntity courseEntity : courseEntities) {
                if (courseId == courseEntity.getId()) {
                    courseTitle[0] = courseEntity.getTitle();
                }
            }
            assessmentAssociatedCourseView.setText(courseTitle[0]);
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



    public void onReturnClicked(View view) {
        finish();
    }

}
