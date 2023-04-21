package com.c196.wgu_mobile.ui.term;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.assessment.NewAssessmentActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.util.DateValidator;

public class NewTermActivity extends AppCompatActivity {

    public static final String ADD_TERM_TITLE = "com.c196.wgu_mobile.EXTRA_TERM_TITLE";
    public static final String ADD_TERM_START = "com.c196.wgu_mobile.EXTRA_TERM_START";
    public static final String ADD_TERM_END = "com.c196.wgu_mobile.EXTRA_TERM_END";


    private EditText mAddTermTitle;
    private EditText mAddTermStart;
    private EditText mAddTermEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_term);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddTermTitle = findViewById(R.id.add_term_title);
        mAddTermStart = findViewById(R.id.add_term_start);
        mAddTermEnd = findViewById(R.id.add_term_end);


        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            String error = "";
            DateValidator dateValidator = new DateValidator(this);
            if (TextUtils.isEmpty(mAddTermTitle.getText())
                    || TextUtils.isEmpty(mAddTermStart.getText())
                    || TextUtils.isEmpty(mAddTermEnd.getText())) {

                if (TextUtils.isEmpty(mAddTermTitle.getText())) {
                    error = "title ";
                }
                if (TextUtils.isEmpty(mAddTermStart.getText())) {
                    error = "start date ";
                }
                if (TextUtils.isEmpty(mAddTermEnd.getText())) {
                    error += "end date ";
                }

                if (!(error.isEmpty())) {
                    Toast.makeText(NewTermActivity.this, "Please ensure that " +
                            error + "field(s) are complete.", Toast.LENGTH_LONG).show();
                }
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else if (!dateValidator.dateValidator(DateConverter.fromTimestamp(mAddTermStart
                            .getText().toString()),
                    DateConverter.fromTimestamp(mAddTermEnd.getText().toString()))) {
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else {
                Bundle termDetails = new Bundle();
                termDetails.putString("title", mAddTermTitle.getText().toString());
                termDetails.putString("start", mAddTermStart.getText().toString());
                termDetails.putString("end", mAddTermEnd.getText().toString());
                replyIntent.putExtra(ADD_TERM_TITLE, termDetails.getString("title"));
                replyIntent.putExtra(ADD_TERM_START, termDetails.getString("start"));
                replyIntent.putExtra(ADD_TERM_END, termDetails.getString("end"));
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
