package com.c196.wgu_mobile.ui.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.viewmodel.TermViewModel;

public class CourseDetailsActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CourseEntity selectedCourse = (CourseEntity)getIntent()
                .getSerializableExtra("selected_course");
        if (selectedCourse == null) {
            // Handle error: no term was passed in the intent
            System.out.println("no course passed");
        }

        TextView courseTitleView = findViewById(R.id.course_title);
        courseTitleView.setText(selectedCourse.getTitle());

        TextView courseStartDateView = findViewById(R.id.course_start);
        courseStartDateView.setText(DateConverter.dateToTimestamp(selectedCourse.getStartDate()));

        TextView courseEndDateView = findViewById(R.id.course_end);
        courseEndDateView.setText(DateConverter.dateToTimestamp(selectedCourse.getEndDate()));

        TextView courseStatusDateView = findViewById(R.id.course_status);
        courseStatusDateView.setText(selectedCourse.getStatus());

        TextView courseInsNameView = findViewById(R.id.course_ins_name);
        courseInsNameView.setText(selectedCourse.getInstructorName());

        TextView courseInsPhoneView = findViewById(R.id.course_ins_phone);
        courseInsPhoneView.setText(selectedCourse.getInstructorPhone());

        TextView courseInsEmailView = findViewById(R.id.course_ins_email);
        courseInsEmailView.setText(selectedCourse.getInstructorEmail());

        TextView courseNoteView = findViewById(R.id.course_note);
        courseNoteView.setText(selectedCourse.getNote());

        //get term title based off of id and set text field
        TextView associatedCourseView = findViewById(R.id.associated_course);
        TermViewModel mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        mTermViewModel.getAllTerms().observe(CourseDetailsActivity.this, termEntities -> {
            for (TermEntity termEntity : termEntities) {
                if (termEntity.getId() == selectedCourse.getTermId()) {
                    associatedCourseView.setText(termEntity.getTitle());
                }
            }
        });

        //note sharing via SMS
        phoneNumberEditText = findViewById(R.id.edittext_phone_number);
        Button shareNotesButton = findViewById(R.id.share_notes_button);

        //Add onClickListener to the shareNotesButton
        shareNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                String notes = selectedCourse.getNote(); //replace this with the actual notes you want to share

                Intent sendSmsIntent = new Intent(Intent.ACTION_SENDTO);
                sendSmsIntent.setData(Uri.parse("smsto:" + phoneNumber));
                sendSmsIntent.putExtra("sms_body", notes);
                startActivity(sendSmsIntent);
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

    public void onReturnClicked(View view) {
        finish();
    }

}