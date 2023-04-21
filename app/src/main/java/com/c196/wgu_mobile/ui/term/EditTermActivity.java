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
import android.widget.TextView;
import android.widget.Toast;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.util.DateValidator;

public class EditTermActivity extends AppCompatActivity {

    public static final String EDIT_TERM_TITLE = "com.c196.wgu_mobile.EDIT_TERM_TITLE";
    public static final String EDIT_TERM_START = "com.c196.wgu_mobile.EDIT_TERM_START";
    public static final String EDIT_TERM_END = "com.c196.wgu_mobile.EDIT_TERM_END";
    public static final String TERM_ID = "com.c196.wgu_mobile.TERM_ID";

    private EditText mEditTermTitle;
    private EditText mEditTermStart;
    private EditText mEditTermEnd;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TermEntity selectedTerm = (TermEntity)getIntent()
                .getSerializableExtra("selected_term");
        if (selectedTerm == null) {
            // Handle error: no term was passed in the intent
            System.out.println("no term passed");
        }

        assert selectedTerm != null;
        id = String.valueOf(selectedTerm.getId());

        mEditTermTitle = findViewById(R.id.add_term_title);
        mEditTermTitle.setText(selectedTerm.getTitle());

        mEditTermStart = findViewById(R.id.add_term_start);
        mEditTermStart.setText(DateConverter.dateToTimestamp(selectedTerm.getStartDate()));

        mEditTermEnd = findViewById(R.id.add_term_end);
        mEditTermEnd.setText(DateConverter.dateToTimestamp(selectedTerm.getEndDate()));

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            String error = "";
            DateValidator dateValidator = new DateValidator(this);
            if (TextUtils.isEmpty(mEditTermTitle.getText())
                    || TextUtils.isEmpty(mEditTermStart.getText())
                    || TextUtils.isEmpty(mEditTermEnd.getText())) {

                if (TextUtils.isEmpty(mEditTermTitle.getText())) {
                    error = "title ";
                }
                if (TextUtils.isEmpty(mEditTermStart.getText())) {
                    error = "start date ";
                }
                if (TextUtils.isEmpty(mEditTermEnd.getText())) {
                    error += "end date ";
                }

                if (!(error.isEmpty())) {
                    Toast.makeText(EditTermActivity.this, "Please ensure that " +
                            error + "field(s) are complete.", Toast.LENGTH_LONG).show();
                }
                setResult(RESULT_CANCELED, replyIntent);
                return;
            } else {
                Bundle termDetails = new Bundle();
                termDetails.putString("title", mEditTermTitle.getText().toString());
                termDetails.putString("start", mEditTermStart.getText().toString());
                termDetails.putString("end", mEditTermEnd.getText().toString());
                termDetails.putString("id", id);
                replyIntent.putExtra(EDIT_TERM_TITLE, termDetails.getString("title"));
                replyIntent.putExtra(EDIT_TERM_START, termDetails.getString("start"));
                replyIntent.putExtra(EDIT_TERM_END, termDetails.getString("end"));
                replyIntent.putExtra(TERM_ID, termDetails.getString("id"));
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