package com.c196.wgu_mobile.ui.course;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.c196.wgu_mobile.adapter.CourseAdapter;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.util.AlertManager;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.util.RecyclerItemClickListener;
import com.c196.wgu_mobile.viewmodel.CourseViewModel;
import com.c196.wgu_mobile.viewmodel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CoursesActivity extends AppCompatActivity {

    private CourseViewModel mCourseViewModel;
    private TermViewModel mTermViewModel;

    final CourseAdapter adapter = new CourseAdapter(new CourseAdapter.CourseDiff());

    AlertManager alert;
    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        mContext = getApplicationContext();
        alert = new AlertManager(mContext);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_courses);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //initialize view models
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        LiveData<List<CourseEntity>> coursesList = mCourseViewModel.getAllCourses();

        // Update the cached copy of the words in the adapter.
        mCourseViewModel.getAllCourses().observe(this, adapter::submitList);

        FloatingActionButton fabAddCourse = findViewById(R.id.fab_add);
        fabAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoursesActivity.this,
                        NewCourseActivity.class);
                newCourseActivityResultLauncher.launch(intent);
            }
        });

        //clickable recycler view item
//        Context context = getApplicationContext();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener
                        .OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(CoursesActivity.this,
                                CourseDetailsActivity.class);
                        intent.putExtra("selected_course", Objects
                                .requireNonNull(coursesList.getValue()).get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Intent intent = new Intent(CoursesActivity.this,
                                EditCourseActivity.class);
                        intent.putExtra("selected_course", Objects
                                .requireNonNull(coursesList.getValue()).get(position));
                        editCourseActivityResultLauncher.launch(intent);
                    }
                })
        );

        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        CourseEntity selectedCourse = adapter.getCourseAtPosition(position);
                        Toast.makeText(CoursesActivity.this, "Deleting " +
                                selectedCourse.getTitle(), Toast.LENGTH_LONG).show();

                        // Delete the term
                        mCourseViewModel.delete(selectedCourse);
                    }
                });

        helper.attachToRecyclerView(recyclerView);


        Spinner spinner = findViewById(R.id.term_spinner);
        final ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        stringAdapter.add("All"); // Adding "All" option to the top of the list

        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringAdapter);
        mTermViewModel.getAllTerms().observe(this, termEntities -> {
            stringAdapter.clear();
            stringAdapter.add("All"); // Adding "All" option to the top of the list
            for (TermEntity termEntity : termEntities) {
                stringAdapter.add(termEntity.getTitle());
            }
        });

//        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTermTitle = parent.getItemAtPosition(position).toString();

                if(selectedTermTitle.equals("All")) { // if "All" is selected, display all courses
                    mCourseViewModel.getAllCourses().observe(CoursesActivity.this,
                            adapter::submitList);
                } else {

                    LiveData<List<TermEntity>> termList = mTermViewModel.getAllTerms();
                    //TODO: fix current course filtering that is not working
                    termList.observe(CoursesActivity.this, termEntities -> {
                        for (TermEntity termEntity : termEntities) {
                            if (termEntity.getTitle().equals(selectedTermTitle)) {
                                int termId = termEntity.getId();
                                mCourseViewModel.getAllCourses().observe(CoursesActivity.this,
                                        courseEntities -> {
                                            List<CourseEntity> filteredCourses = new ArrayList<>();
                                            for (CourseEntity courseEntity : courseEntities) {
                                                int courseTermId = courseEntity.getTermId();
                                                if (courseTermId == termId) {
                                                    filteredCourses.add(courseEntity);
                                                }
                                            }
                                            adapter.submitList(filteredCourses);
                                        });
                                break;
                            }
                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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



    private final ActivityResultLauncher<Intent> newCourseActivityResultLauncher =
            registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        String title = data.getStringExtra(NewCourseActivity.ADD_COURSE_TITLE);
                        String start = data.getStringExtra(NewCourseActivity.ADD_COURSE_START);
                        String end = data.getStringExtra(NewCourseActivity.ADD_COURSE_END);
                        String termId = data.getStringExtra(NewCourseActivity.ADD_COURSE_ASSOCIATED_TERM);
                        String status = data.getStringExtra(NewCourseActivity.ADD_COURSE_STATUS);
                        String insName = data.getStringExtra(NewCourseActivity
                                .ADD_COURSE_INS_NAME);
                        String insPhone = data.getStringExtra(NewCourseActivity
                                .ADD_COURSE_INS_PHONE);
                        String insEmail = data.getStringExtra(NewCourseActivity
                                .ADD_COURSE_INS_EMAIL);


                        //note is optional
                        String note = "";
                        if ((data.getStringExtra(NewCourseActivity.ADD_COURSE_NOTE)) != null)
                            note = data.getStringExtra(NewCourseActivity.ADD_COURSE_NOTE);

                        CourseEntity course = new CourseEntity(title, Integer.parseInt(termId),
                                DateConverter.fromTimestamp(start),
                                DateConverter.fromTimestamp(end), status, insName,
                                insPhone, insEmail, note);
                        mCourseViewModel.insert(course);
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.course_empty_not_saved,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> editCourseActivityResultLauncher =
            registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();
                    String title = data.getStringExtra(EditCourseActivity.EDIT_COURSE_TITLE);
                    String start = data.getStringExtra(EditCourseActivity.EDIT_COURSE_START);
                    String end = data.getStringExtra(EditCourseActivity.EDIT_COURSE_END);
                    String termId = data.getStringExtra(EditCourseActivity.EDIT_COURSE_ASSOCIATED_TERM);
                    String status = data.getStringExtra(EditCourseActivity.EDIT_COURSE_STATUS);
                    String insName = data.getStringExtra(EditCourseActivity.EDIT_COURSE_INS_NAME);
                    String insPhone = data.getStringExtra(EditCourseActivity.EDIT_COURSE_INS_PHONE);
                    String insEmail = data.getStringExtra(EditCourseActivity.EDIT_COURSE_INS_EMAIL);
                    int id = Integer.parseInt((data.getStringExtra(EditCourseActivity.COURSE_ID)));

                    //note is optional
                    String note = "";
                    if ((data.getStringExtra(EditCourseActivity
                            .EDIT_COURSE_NOTE)) != null)
                        note = data.getStringExtra(EditCourseActivity
                                .EDIT_COURSE_NOTE);

                    CourseEntity course = new CourseEntity(title, Integer.parseInt(termId),
                            DateConverter.fromTimestamp(start),
                            DateConverter.fromTimestamp(end), status, insName,
                            insPhone, insEmail, note);
                    mCourseViewModel.update(course);
                    mCourseViewModel.getAllCourses()
                            .observe(CoursesActivity.this, adapter::submitList);

                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.course_empty_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            });

}

