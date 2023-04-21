package com.c196.wgu_mobile.ui.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.adapter.AssociatedTermAdapter;
import com.c196.wgu_mobile.adapter.CourseStatusAdapter;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.term.NewTermActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.util.AlertManager;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.util.DateValidator;
import com.c196.wgu_mobile.viewmodel.TermViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NewCourseActivity extends AppCompatActivity {

    //initialize strings to be passed back to Courses in a bundle for the newly created CourseEntity
    public static final String ADD_COURSE_TITLE = "com.c196.wgu_mobile.EXTRA_COURSE_TITLE";
    public static final String ADD_COURSE_START = "com.c196.wgu_mobile.EXTRA_COURSE_START";
    public static final String ADD_COURSE_END = "com.c196.wgu_mobile.EXTRA_COURSE_END";
    public static final String ADD_COURSE_ASSOCIATED_TERM =
            "com.c196.wgu_mobile.EXTRA_COURSE_ASSOCIATED_TERM";
    public static final String ADD_COURSE_STATUS = "com.c196.wgu_mobile.EXTRA_COURSE_STATUS";
    public static final String ADD_COURSE_INS_NAME = "com.c196.wgu_mobile.EXTRA_COURSE_INS_NAME";
    public static final String ADD_COURSE_INS_PHONE = "com.c196.wgu_mobile.EXTRA_COURSE_INS_PHONE";
    public static final String ADD_COURSE_INS_EMAIL = "com.c196.wgu_mobile.EXTRA_COURSE_INS_EMAIL";
    public static final String ADD_COURSE_NOTE = "com.c196.wgu_mobile.EXTRA_COURSE_NOTE";


    //initialize edit text fields
    private EditText mAddCourseTitle;
    private EditText mAddCourseStart;
    private EditText mAddCourseEnd;
    private EditText mAddCourseInsName;
    private EditText mAddCourseInsPhone;
    private EditText mAddCourseInsEmail;
    private EditText mAddCourseNote;

    //initialize members needed to set up associated term recycler view
    private RecyclerView mAddAssociatedTerm;
    private String selectedTerm;


    //initialize members needed to set up course status recycler view
    private RecyclerView mAddCourseStatus;
    private String selectedStatus;
    private final List<String> statusOptions =
            Arrays.asList("In Progress", "Completed", "Dropped", "Plan to Take");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);

        //set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //take in text fields
        mAddCourseTitle = findViewById(R.id.add_course_title);
        mAddCourseStart = findViewById(R.id.add_course_start);
        mAddCourseEnd = findViewById(R.id.add_course_end);
        mAddCourseInsName = findViewById(R.id.add_course_ins_name);
        mAddCourseInsPhone = findViewById(R.id.add_course_ins_phone);
        mAddCourseInsEmail = findViewById(R.id.add_course_ins_email);
        mAddCourseNote = findViewById(R.id.add_course_note);

        //alert start
        AlertManager alert = new AlertManager(this);
        Button startAlertButton = findViewById(R.id.start_alert_button);
        startAlertButton.setOnClickListener(v -> alert.setCourseStartAlert(DateConverter
                .fromTimestamp(mAddCourseStart.getText().toString())));

        //alert end
        Button endAlertButton = findViewById(R.id.end_alert_button);
        endAlertButton.setOnClickListener(v -> alert.setCourseEndAlert(DateConverter
                .fromTimestamp(mAddCourseEnd.getText().toString())));


        //take in associated term
        mAddAssociatedTerm = findViewById(R.id.term_recycler_view);
        selectedTerm = "";
        AssociatedTermAdapter termAdapter = new AssociatedTermAdapter(this);
        mAddAssociatedTerm.setAdapter(termAdapter);
        LinearLayoutManager layoutManagerTerm = new LinearLayoutManager(this);
        mAddAssociatedTerm.setLayoutManager(layoutManagerTerm);



        //take in course status
        mAddCourseStatus = findViewById(R.id.status_recycler_view);
        selectedStatus = "";
        CourseStatusAdapter statusAdapter = new CourseStatusAdapter(this, statusOptions);
        mAddCourseStatus.setAdapter(statusAdapter);
        LinearLayoutManager layoutManagerStatus = new LinearLayoutManager(this);
        mAddCourseStatus.setLayoutManager(layoutManagerStatus);


        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            String error = "";
            DateValidator dateValidator = new DateValidator(this);
            if (TextUtils.isEmpty(mAddCourseTitle.getText())
                    || TextUtils.isEmpty(mAddCourseStart.getText())
                    || TextUtils.isEmpty(mAddCourseEnd.getText())
                    || termAdapter.getSelectedPosition() == -1
                    || statusAdapter.getSelectedPosition() == -1
                    || TextUtils.isEmpty(mAddCourseInsName.getText())
                    || TextUtils.isEmpty(mAddCourseInsPhone.getText())
                    || TextUtils.isEmpty(mAddCourseInsEmail.getText())) {

                if (TextUtils.isEmpty(mAddCourseTitle.getText())) {
                    error = "title ";
                }
                if (TextUtils.isEmpty(mAddCourseStart.getText())) {
                    error += "start date ";
                }
                if (TextUtils.isEmpty(mAddCourseEnd.getText())) {
                    error += "end date ";
                }
                if (termAdapter.getSelectedPosition() == -1) {
                    error += "associated term ";
                }
                if (statusAdapter.getSelectedPosition() == -1) {
                    error += "status ";
                }
                if (TextUtils.isEmpty(mAddCourseInsName.getText())) {
                    error += "instructor's name ";
                }
                if (TextUtils.isEmpty(mAddCourseInsPhone.getText())) {
                    error += "instructor's phone number ";
                }
                if (TextUtils.isEmpty(mAddCourseInsEmail.getText())) {
                    error += "instructor's email address ";
                }

                if (!(error.isEmpty())) {
                    Toast.makeText(NewCourseActivity.this, "Please ensure that " +
                            error + "field(s) are complete.", Toast.LENGTH_LONG).show();
                }

                setResult(RESULT_CANCELED, replyIntent);
                return;

            } else if (!(PhoneNumberUtils.isGlobalPhoneNumber
                    (String.valueOf(mAddCourseInsPhone.getText())))) {
                Toast.makeText(NewCourseActivity.this,
                            "Please ensure that the instructor's phone number is in correct " +
                                    "format", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, replyIntent);
                return;

            } else if (!(Patterns.EMAIL_ADDRESS.matcher(mAddCourseInsEmail.getText()).matches())) {
                Toast.makeText(NewCourseActivity.this,
                        "Please ensure that the instructor's email address is in correct " +
                                "format", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else if (!dateValidator.dateValidator(DateConverter.fromTimestamp(mAddCourseStart
                    .getText().toString()),
                    DateConverter.fromTimestamp(mAddCourseEnd.getText().toString()))) {
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else {

                int termId = -1;
                String termTitle = termAdapter.getSelectedTerm();
                TermViewModel mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
                LiveData<List<TermEntity>> termList = mTermViewModel.getAllTerms();
                ArrayList<TermEntity> primitiveTermList = new ArrayList<>();
                termList.observe(NewCourseActivity.this, primitiveTermList::addAll);
                for (TermEntity term : primitiveTermList) {
                    if (Objects.equals(term.getTitle(), termTitle)) {
                        termId = term.getId();
                    }
                }

                String selectedStatus = statusAdapter.getSelectedStatus();


                Bundle courseDetails = new Bundle();
                courseDetails.putString("title", mAddCourseTitle.getText().toString());
                courseDetails.putString("start", mAddCourseStart.getText().toString());
                courseDetails.putString("end", mAddCourseEnd.getText().toString());
                courseDetails.putString("term_id", String.valueOf(termId));
                courseDetails.putString("status", selectedStatus);
                courseDetails.putString("name", mAddCourseInsName.getText().toString());
                courseDetails.putString("phone", mAddCourseInsPhone.getText().toString());
                courseDetails.putString("email", mAddCourseInsEmail.getText().toString());

                replyIntent.putExtra(ADD_COURSE_TITLE, courseDetails.getString("title"));
                replyIntent.putExtra(ADD_COURSE_START, courseDetails.getString("start"));
                replyIntent.putExtra(ADD_COURSE_END, courseDetails.getString("end"));
                replyIntent.putExtra(ADD_COURSE_ASSOCIATED_TERM, String.valueOf(termId));
                replyIntent.putExtra(ADD_COURSE_STATUS, selectedStatus);
                replyIntent.putExtra(ADD_COURSE_INS_NAME, courseDetails.getString("name"));
                replyIntent.putExtra(ADD_COURSE_INS_PHONE, courseDetails.getString("phone"));
                replyIntent.putExtra(ADD_COURSE_INS_EMAIL, courseDetails.getString("email"));

                if ((mAddCourseNote.getText()) != null) {
                    courseDetails.putString("note", mAddCourseNote.getText().toString());
                    replyIntent.putExtra(ADD_COURSE_NOTE, courseDetails.getString("note"));
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