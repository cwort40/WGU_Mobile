package com.c196.wgu_mobile.ui.term;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
import com.c196.wgu_mobile.util.DateConverter;

public class TermDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TermEntity selectedTerm = (TermEntity)getIntent()
                .getSerializableExtra("selected_term");
        if (selectedTerm == null) {
            // Handle error: no term was passed in the intent
            System.out.println("no term passed");
        }

        TextView termTitleView = findViewById(R.id.term_title);
        termTitleView.setText(selectedTerm.getTitle());

        TextView termStartDateView = findViewById(R.id.term_start_date);
        termStartDateView.setText(DateConverter.dateToTimestamp(selectedTerm.getStartDate()));

        TextView termEndDateView = findViewById(R.id.term_end_date);
        termEndDateView.setText(DateConverter.dateToTimestamp(selectedTerm.getEndDate()));

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
