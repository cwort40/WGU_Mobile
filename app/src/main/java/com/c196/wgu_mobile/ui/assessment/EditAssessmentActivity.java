package com.c196.wgu_mobile.ui.assessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.adapter.AssessmentTypeAdapter;
import com.c196.wgu_mobile.entity.AssessmentEntity;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.util.AlertManager;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.util.DateValidator;
import com.c196.wgu_mobile.viewmodel.CourseViewModel;

import java.util.Arrays;
import java.util.List;

public class EditAssessmentActivity extends AppCompatActivity {

    public static final String EDIT_ASSESSMENT_TITLE = "com.c196.wgu_mobile.EDIT_ASSESSMENT_TITLE";
    public static final String EDIT_ASSESSMENT_START = "com.c196.wgu_mobile.EDIT_ASSESSMENT_START";
    public static final String EDIT_ASSESSMENT_END = "com.c196.wgu_mobile.EDIT_ASSESSMENT_END";
    public static final String EDIT_ASSESSMENT_TYPE = "com.c196.wgu_mobile.EDIT_ASSESSMENT_TYPE";
    public static final String EDIT_ASSESSMENT_COURSE_TITLE =
            "com.c196.wgu_mobile.EDIT_ASSESSMENT_COURSE_TITLE";
    public static final String ASSESSMENT_ID = "com.c196.wgu_mobile.ASSESSMENT_ID";

    private EditText mEditAssessmentTitle;
    private EditText mEditAssessmentStart;
    private EditText mEditAssessmentEnd;
    private String assessmentId;


    CourseViewModel mCourseViewModel;

    private RecyclerView mEditAssessmentType;
    private String selectedType;
    private final List<String> typeOptions =
            Arrays.asList("Objective Assessment", "Performance Assessment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AssessmentEntity selectedAssessment = (AssessmentEntity)getIntent()
                .getSerializableExtra("selected_assessment");
        if (selectedAssessment == null) {
            // Handle error: no assessment was passed in the intent
            System.out.println("no assessment passed");
        }

        assessmentId = String.valueOf(selectedAssessment.getId());

        mEditAssessmentTitle = findViewById(R.id.add_assessment_title);
        mEditAssessmentTitle.setText(selectedAssessment.getTitle());

        mEditAssessmentStart = findViewById(R.id.add_assessment_start);
        mEditAssessmentStart.setText(DateConverter.dateToTimestamp(selectedAssessment.getStartDate()));

        mEditAssessmentEnd = findViewById(R.id.add_assessment_end);
        mEditAssessmentEnd.setText(DateConverter.dateToTimestamp(selectedAssessment.getEndDate()));

        mEditAssessmentType = findViewById(R.id.type_recycler_view);
        selectedType = "";
        AssessmentTypeAdapter typeAdapter = new AssessmentTypeAdapter(this, typeOptions);
        mEditAssessmentType.setAdapter(typeAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEditAssessmentType.setLayoutManager(layoutManager);

        //alert start
        AlertManager alert = new AlertManager(this);
        Button startAlertButton = findViewById(R.id.start_alert_button);
        startAlertButton.setOnClickListener(v -> alert.setCourseStartAlert(DateConverter
                .fromTimestamp(mEditAssessmentStart.getText().toString())));

        //alert end
        Button endAlertButton = findViewById(R.id.end_alert_button);
        endAlertButton.setOnClickListener(v -> alert.setCourseEndAlert(DateConverter
                .fromTimestamp(mEditAssessmentEnd.getText().toString())));

        //TODO: test to make sure all assessments new and edit are functional and then set the main activity one


        //spinner for associated course
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        Spinner spinner = findViewById(R.id.course_spinner);
        final ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringAdapter);
        mCourseViewModel.getAllCourses().observe(this, courseEntities -> {
            stringAdapter.clear();
            for (CourseEntity courseEntity : courseEntities) {
                stringAdapter.add(courseEntity.getTitle());
            }
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            String error = "";
            DateValidator dateValidator = new DateValidator(this);
            if (TextUtils.isEmpty(mEditAssessmentTitle.getText())
                    || TextUtils.isEmpty(mEditAssessmentStart.getText())
                    || TextUtils.isEmpty(mEditAssessmentEnd.getText())
                    || typeAdapter.getSelectedPosition() == -1
                    || TextUtils.isEmpty(spinner.getSelectedItem().toString())) {

                if (TextUtils.isEmpty(mEditAssessmentTitle.getText())) {
                    error = "title ";
                }
                if (TextUtils.isEmpty(mEditAssessmentStart.getText())) {
                    error += "start date ";
                }
                if (TextUtils.isEmpty(mEditAssessmentEnd.getText())) {
                    error += "end date ";
                }
                if (typeAdapter.getSelectedPosition() == -1) {
                    error += "type ";
                }
                if (TextUtils.isEmpty(spinner.getSelectedItem().toString())) {
                    error += "associated course ";
                }

                if (!(error.isEmpty())) {
                    Toast.makeText(EditAssessmentActivity.this, "Please ensure that " +
                            error + "field(s) are complete.", Toast.LENGTH_LONG).show();
                }

                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else if (!dateValidator.dateValidator(DateConverter.fromTimestamp(mEditAssessmentStart
                            .getText().toString()),
                    DateConverter.fromTimestamp(mEditAssessmentEnd.getText().toString()))) {
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else {
                String selectedType = typeAdapter.getSelectedType();

                Bundle termDetails = new Bundle();
                termDetails.putString("title", mEditAssessmentTitle.getText().toString());
                termDetails.putString("start", mEditAssessmentStart.getText().toString());
                termDetails.putString("end", mEditAssessmentEnd.getText().toString());
                termDetails.putString("type", selectedType);
                termDetails.putString("course", spinner.getSelectedItem().toString());
                termDetails.putString("assessment_id", assessmentId);
                replyIntent.putExtra(EDIT_ASSESSMENT_TITLE, termDetails.getString("title"));
                replyIntent.putExtra(EDIT_ASSESSMENT_START, termDetails.getString("start"));
                replyIntent.putExtra(EDIT_ASSESSMENT_END, termDetails.getString("end"));
                replyIntent.putExtra(EDIT_ASSESSMENT_TYPE, selectedType);
                replyIntent.putExtra(EDIT_ASSESSMENT_COURSE_TITLE, termDetails
                        .getString("course"));
                replyIntent.putExtra(ASSESSMENT_ID, termDetails.getString("assessment_id"));

                setResult(RESULT_OK, replyIntent);
            }
            finish();
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