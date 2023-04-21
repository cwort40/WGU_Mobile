package com.c196.wgu_mobile.ui.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.adapter.AssociatedTermAdapter;
import com.c196.wgu_mobile.adapter.CourseStatusAdapter;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.util.AlertManager;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.util.DateValidator;
import com.c196.wgu_mobile.viewmodel.TermViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditCourseActivity extends AppCompatActivity {

    //initialize strings to be passed back to Courses in a bundle for the edited CourseEntity
    public static final String EDIT_COURSE_TITLE = "com.c196.wgu_mobile.EDIT_COURSE_TITLE";
    public static final String EDIT_COURSE_START = "com.c196.wgu_mobile.EDIT_COURSE_START";
    public static final String EDIT_COURSE_END = "com.c196.wgu_mobile.EDIT_COURSE_END";
    public static final String EDIT_COURSE_ASSOCIATED_TERM =
            "com.c196.wgu_mobile.EDIT_COURSE_ASSOCIATED_TERM";
    public static final String EDIT_COURSE_STATUS = "com.c196.wgu_mobile.EDIT_COURSE_STATUS";
    public static final String EDIT_COURSE_INS_NAME = "com.c196.wgu_mobile.EDIT_COURSE_INS_NAME";
    public static final String EDIT_COURSE_INS_PHONE = "com.c196.wgu_mobile.EDIT_COURSE_INS_PHONE";
    public static final String EDIT_COURSE_INS_EMAIL = "com.c196.wgu_mobile.EDIT_COURSE_INS_EMAIL";
    public static final String EDIT_COURSE_NOTE = "com.c196.wgu_mobile.EDIT_COURSE_NOTE";
    public static final String COURSE_ID = "com.c196.wgu_mobile.COURSE_ID";

    //initialize edit text fields
    private EditText mEditCourseTitle;
    private EditText mEditCourseStart;
    private EditText mEditCourseEnd;
    private EditText mEditCourseInsName;
    private EditText mEditCourseInsPhone;
    private EditText mEditCourseInsEmail;
    private EditText mEditCourseNote;
    private String id;

    //initialize members needed to set up associated term recycler view
    private RecyclerView mAddAssociatedTerm;
    private String selectedTerm;


    //initialize members needed to set up course status recycler view
    private RecyclerView mEditCourseStatus;
    private String selectedStatus;
    private final List<String> statusOptions =
            Arrays.asList("In Progress", "Completed", "Dropped", "Plan to Take");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CourseEntity selectedCourse = (CourseEntity) getIntent()
                .getSerializableExtra("selected_course");
        if (selectedCourse == null) {
            // Handle error: no term was passed in the intent
            System.out.println("no course passed");
        }

        assert selectedCourse != null;

        //populate fields with the existing values of the selected course that was passed in
        id = String.valueOf(selectedCourse.getId());

        mEditCourseTitle = findViewById(R.id.add_course_title);
        mEditCourseTitle.setText(selectedCourse.getTitle());

        mEditCourseStart = findViewById(R.id.add_course_start);
        mEditCourseStart.setText(DateConverter.dateToTimestamp(selectedCourse.getStartDate()));

        mEditCourseEnd = findViewById(R.id.add_course_end);
        mEditCourseEnd.setText(DateConverter.dateToTimestamp(selectedCourse.getEndDate()));

        mEditCourseInsName = findViewById(R.id.add_course_ins_name);
        mEditCourseInsName.setText(selectedCourse.getInstructorName());

        mEditCourseInsPhone = findViewById(R.id.add_course_ins_phone);
        mEditCourseInsPhone.setText(selectedCourse.getInstructorPhone());

        mEditCourseInsEmail = findViewById(R.id.add_course_ins_email);
        mEditCourseInsEmail.setText(selectedCourse.getInstructorEmail());

        mEditCourseNote = findViewById(R.id.add_course_note);
        mEditCourseNote.setText(selectedCourse.getNote());

        //alert start
        AlertManager alert = new AlertManager(this);
        Button startAlertButton = findViewById(R.id.start_alert_button);
        startAlertButton.setOnClickListener(v -> alert.setCourseStartAlert(DateConverter
                .fromTimestamp(mEditCourseStart.getText().toString())));

        //alert end
        Button endAlertButton = findViewById(R.id.end_alert_button);
        endAlertButton.setOnClickListener(v -> alert.setCourseEndAlert(DateConverter
                .fromTimestamp(mEditCourseEnd.getText().toString())));

        //take in associated term
        mAddAssociatedTerm = findViewById(R.id.term_recycler_view);
        selectedTerm = "";
        AssociatedTermAdapter termAdapter = new AssociatedTermAdapter(this);
        mAddAssociatedTerm.setAdapter(termAdapter);
        LinearLayoutManager layoutManagerTerm = new LinearLayoutManager(this);
        mAddAssociatedTerm.setLayoutManager(layoutManagerTerm);


        //take in course status
        mEditCourseStatus = findViewById(R.id.status_recycler_view);
        selectedStatus = "";
        CourseStatusAdapter statusAdapter = new CourseStatusAdapter(this, statusOptions);
        mEditCourseStatus.setAdapter(statusAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEditCourseStatus.setLayoutManager(layoutManager);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            String error = "";
            DateValidator dateValidator = new DateValidator(this);
            if (TextUtils.isEmpty(mEditCourseTitle.getText())
                    || TextUtils.isEmpty(mEditCourseStart.getText())
                    || TextUtils.isEmpty(mEditCourseEnd.getText())
                    || termAdapter.getSelectedPosition() == -1
                    || statusAdapter.getSelectedPosition() == -1
                    || TextUtils.isEmpty(mEditCourseInsName.getText())
                    || TextUtils.isEmpty(mEditCourseInsPhone.getText())
                    || TextUtils.isEmpty(mEditCourseInsEmail.getText())) {

                if (TextUtils.isEmpty(mEditCourseTitle.getText())) {
                error = "title ";
                }
                if (TextUtils.isEmpty(mEditCourseStart.getText())) {
                    error += "start date ";
                }
                if (TextUtils.isEmpty(mEditCourseEnd.getText())) {
                    error += "end date ";
                }
                if (termAdapter.getSelectedPosition() == -1) {
                    error += "associated term ";
                }
                if (statusAdapter.getSelectedPosition() == -1) {
                    error += "status ";
                }
                if (TextUtils.isEmpty(mEditCourseInsName.getText())) {
                    error += "instructor's name ";
                }
                if (TextUtils.isEmpty(mEditCourseInsPhone.getText())) {
                    error += "instructor's phone number ";
                }
                if (TextUtils.isEmpty(mEditCourseInsEmail.getText())) {
                    error += "instructor's email address ";
                }

                if (!(error.isEmpty())) {
                    Toast.makeText(EditCourseActivity.this, "Please ensure that " +
                            error + "field(s) are complete.", Toast.LENGTH_LONG).show();
                }

                setResult(RESULT_CANCELED, replyIntent);
                return;

            } else if (!(PhoneNumberUtils.isGlobalPhoneNumber
                    (String.valueOf(mEditCourseInsPhone.getText())))) {
                Toast.makeText(EditCourseActivity.this,
                        "Please ensure that the instructor's phone number is in correct " +
                                "format", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, replyIntent);
                return;

            } else if (!(Patterns.EMAIL_ADDRESS.matcher(mEditCourseInsEmail.getText()).matches())) {
                Toast.makeText(EditCourseActivity.this,
                        "Please ensure that the instructor's email address is in correct " +
                                "format", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else if (!dateValidator.dateValidator(DateConverter.fromTimestamp(mEditCourseStart
                            .getText().toString()),
                    DateConverter.fromTimestamp(mEditCourseEnd.getText().toString()))) {
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else {

                int termId = -1;
                String termTitle = termAdapter.getSelectedTerm();
                TermViewModel mTermViewModel = new ViewModelProvider(this)
                        .get(TermViewModel.class);
                LiveData<List<TermEntity>> termList = mTermViewModel.getAllTerms();
                ArrayList<TermEntity> primitiveTermList = new ArrayList<>();
                termList.observe(EditCourseActivity.this, primitiveTermList::addAll);
                for (TermEntity term : primitiveTermList) {
                    if (Objects.equals(term.getTitle(), termTitle)) {
                        termId = term.getId();
                    }
                }

                String selectedStatus = statusAdapter.getSelectedStatus();

                Bundle courseDetails = new Bundle();
                courseDetails.putString("title", mEditCourseTitle.getText().toString());
                courseDetails.putString("start", mEditCourseStart.getText().toString());
                courseDetails.putString("end", mEditCourseEnd.getText().toString());
                courseDetails.putString("term_id", String.valueOf(termId));
                courseDetails.putString("status", selectedStatus);
                courseDetails.putString("ins_name", mEditCourseInsName.getText().toString());
                courseDetails.putString("ins_phone", mEditCourseInsPhone.getText().toString());
                courseDetails.putString("ins_email", mEditCourseInsEmail.getText().toString());
                courseDetails.putString("id", id);

                replyIntent.putExtra(EDIT_COURSE_TITLE, courseDetails.getString("title"));
                replyIntent.putExtra(EDIT_COURSE_START, courseDetails.getString("start"));
                replyIntent.putExtra(EDIT_COURSE_END, courseDetails.getString("end"));
                replyIntent.putExtra(EDIT_COURSE_ASSOCIATED_TERM, String.valueOf(termId));
                replyIntent.putExtra(EDIT_COURSE_STATUS, selectedStatus);
                replyIntent.putExtra(EDIT_COURSE_INS_NAME,
                        courseDetails.getString("ins_name"));
                replyIntent.putExtra(EDIT_COURSE_INS_PHONE,
                        courseDetails.getString("ins_phone"));
                replyIntent.putExtra(EDIT_COURSE_INS_EMAIL,
                        courseDetails.getString("ins_email"));
                replyIntent.putExtra(COURSE_ID, courseDetails.getString("id"));

                if ((mEditCourseNote.getText()) != null) {
                    courseDetails.putString("note", mEditCourseNote.getText().toString());
                    replyIntent.putExtra(EDIT_COURSE_NOTE, courseDetails.getString("note"));
                }

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