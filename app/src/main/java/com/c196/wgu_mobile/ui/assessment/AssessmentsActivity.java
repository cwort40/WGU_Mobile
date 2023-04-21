package com.c196.wgu_mobile.ui.assessment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.adapter.AssessmentAdapter;
import com.c196.wgu_mobile.adapter.CourseAdapter;
import com.c196.wgu_mobile.entity.AssessmentEntity;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.course.CourseDetailsActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
import com.c196.wgu_mobile.ui.course.EditCourseActivity;
import com.c196.wgu_mobile.ui.course.NewCourseActivity;
import com.c196.wgu_mobile.ui.term.EditTermActivity;
import com.c196.wgu_mobile.ui.term.NewTermActivity;
import com.c196.wgu_mobile.ui.term.TermsActivity;
import com.c196.wgu_mobile.util.AlertManager;
import com.c196.wgu_mobile.util.DateConverter;
import com.c196.wgu_mobile.util.RecyclerItemClickListener;
import com.c196.wgu_mobile.viewmodel.AssessmentViewModel;
import com.c196.wgu_mobile.viewmodel.CourseViewModel;
import com.c196.wgu_mobile.viewmodel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AssessmentsActivity extends AppCompatActivity {

    private CourseViewModel mCourseViewModel;
    private AssessmentViewModel mAssessmentViewModel;
    private final ArrayList<CourseEntity> allCourses = new ArrayList<>();
    private AssessmentAdapter assessmentAdapter;

    AlertManager alert;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        mContext = getApplicationContext();
        alert = new AlertManager(mContext);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create allCoursesList
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mCourseViewModel.getAllCourses().observe(this, courseEntities -> {
            for (CourseEntity courseEntity : courseEntities) {
                allCourses.add(courseEntity);
            }
        });

        RecyclerView assessmentRecyclerView = findViewById(R.id.assessments_recycler_view);
        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(new AssessmentAdapter
                .AssessmentDiff());
        assessmentRecyclerView.setAdapter(assessmentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        assessmentRecyclerView.setLayoutManager(layoutManager);

        mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

        mAssessmentViewModel.getAllAssessments().observe(this,
                assessmentEntities -> assessmentAdapter.submitList(assessmentEntities));

        if (mAssessmentViewModel.getAllAssessments() != null)
            System.out.println(mAssessmentViewModel.getAllAssessments());
        else
            System.out.println("list is null");



        FloatingActionButton fabAddAssessment = findViewById(R.id.fab_add);
        fabAddAssessment.setOnClickListener(view -> {
            Intent intent = new Intent(AssessmentsActivity.this,
                    NewAssessmentActivity.class);
            newAssessmentActivityResultLauncher.launch(intent);
        });

        //clickable recycler view item
        LiveData<List<AssessmentEntity>> assessmentsList = mAssessmentViewModel.getAllAssessments();
//        Context context = getApplicationContext();
        assessmentRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, assessmentRecyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(AssessmentsActivity.this,
                                AssessmentDetailsActivity.class);
                        intent.putExtra("selected_assessment", Objects
                                .requireNonNull(assessmentsList.getValue()).get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Intent intent = new Intent(AssessmentsActivity.this,
                                EditAssessmentActivity.class);
                        intent.putExtra("selected_assessment", Objects
                                .requireNonNull(assessmentsList.getValue()).get(position));
                        editAssessmentActivityResultLauncher.launch(intent);
                    }
                })
        );

        //spinner for course selection filter
        Spinner spinner = findViewById(R.id.course_spinner);
        final ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        stringAdapter.add("All"); // Adding "All" option to the top of the list

        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringAdapter);
        mCourseViewModel.getAllCourses().observe(this, courseEntities -> {
            stringAdapter.clear();
            stringAdapter.add("All"); // Adding "All" option to the top of the list
            for (CourseEntity courseEntity : courseEntities) {
                stringAdapter.add(courseEntity.getTitle());
            }
        });


        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourseTitle = parent.getItemAtPosition(position).toString();

                if(selectedCourseTitle.equals("All")) { // if "All" is selected, display all courses
                    mAssessmentViewModel.getAllAssessments()
                            .observe(AssessmentsActivity.this, assessmentAdapter::submitList);
                } else {

                    LiveData<List<CourseEntity>> courseList = mCourseViewModel.getAllCourses();

                    courseList.observe(AssessmentsActivity.this, courseEntities -> {
                        for (CourseEntity courseEntity : courseEntities) {
                            if (courseEntity.getTitle().equals(selectedCourseTitle)) {
                                mAssessmentViewModel.getAllAssessments()
                                        .observe(AssessmentsActivity.this, assessmentEntities -> {
                                            List<AssessmentEntity> filteredAssessments = new ArrayList<>();
                                            for (AssessmentEntity assessment : assessmentEntities) {
                                                if (assessment.getCourse_id() == courseEntity.getId()) {
                                                    filteredAssessments.add(assessment);
                                                }
                                            }
                                            assessmentAdapter.submitList(filteredAssessments);
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

        // Add the functionality to swipe items in the
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
                        AssessmentEntity selectedAssessment = assessmentAdapter
                                .getAssessmentAtPosition(position);
                        Toast.makeText(AssessmentsActivity.this, "Deleting " +
                                selectedAssessment.getTitle(), Toast.LENGTH_LONG).show();

                        // Delete the term
                        mAssessmentViewModel.delete(selectedAssessment);
                        alert.cancelAlert(selectedAssessment.getId());
                    }
                });

        helper.attachToRecyclerView(assessmentRecyclerView);

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

    private final ActivityResultLauncher<Intent> newAssessmentActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                String title = data.getStringExtra(NewAssessmentActivity
                                        .ADD_ASSESSMENT_TITLE);
                                String start = data.getStringExtra(NewAssessmentActivity
                                        .ADD_ASSESSMENT_START);
                                String end = data.getStringExtra(NewAssessmentActivity
                                        .ADD_ASSESSMENT_END);
                                String type = data.getStringExtra(NewAssessmentActivity
                                        .ADD_ASSESSMENT_TYPE);
                                String courseTitle = data.getStringExtra(NewAssessmentActivity
                                        .ADD_ASSESSMENT_COURSE);

                                // Get the courseId
                                int courseId = -1;
                                for (CourseEntity courseFromList : allCourses) {
                                    if (courseFromList.getTitle().equals(courseTitle)) {
                                        courseId = courseFromList.getId();
                                        break;
                                    }
                                }

                                AssessmentEntity assessment = new AssessmentEntity(title, DateConverter.fromTimestamp(start),
                                        DateConverter.fromTimestamp(end), type, courseId);
                                mAssessmentViewModel.insert(assessment);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        R.string.assessment_empty_not_saved,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

    private final ActivityResultLauncher<Intent> editAssessmentActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String title = data.getStringExtra(EditAssessmentActivity
                                .EDIT_ASSESSMENT_TITLE);
                        String start = data.getStringExtra(EditAssessmentActivity
                                .EDIT_ASSESSMENT_START);
                        String end = data.getStringExtra(EditAssessmentActivity
                                .EDIT_ASSESSMENT_END);
                        String type = data.getStringExtra(EditAssessmentActivity
                                .EDIT_ASSESSMENT_TYPE);
                        String courseTitle = data.getStringExtra(EditAssessmentActivity
                                .EDIT_ASSESSMENT_COURSE_TITLE);
                        int assessmentId = Integer.parseInt(data.getStringExtra(EditAssessmentActivity
                                .ASSESSMENT_ID));

                        // Get the courseId
                        int courseId = -1;
                        for (CourseEntity courseFromList : allCourses) {
                            if (courseFromList.getTitle().equals(courseTitle)) {
                                courseId = courseFromList.getId();
                                break;
                            }
                        }

                        AssessmentEntity assessment = new AssessmentEntity(title, DateConverter.fromTimestamp(start),
                                DateConverter.fromTimestamp(end), type, courseId, assessmentId);
                        mAssessmentViewModel.update(assessment);

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.term_empty_not_saved,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });



}