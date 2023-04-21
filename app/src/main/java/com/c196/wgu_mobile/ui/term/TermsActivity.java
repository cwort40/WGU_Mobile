package com.c196.wgu_mobile.ui.term;

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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.adapter.TermAdapter;
import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.ui.MainActivity;
import com.c196.wgu_mobile.ui.assessment.AssessmentsActivity;
import com.c196.wgu_mobile.ui.course.CoursesActivity;
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

public class TermsActivity extends AppCompatActivity {

    private TermViewModel mTermViewModel;
    private CourseViewModel mCourseViewModel;
    public static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;

    final TermAdapter adapter = new TermAdapter(new TermAdapter.TermDiff());

    AlertManager alert;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        mContext = getApplicationContext();
        alert = new AlertManager(mContext);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fabAddTerm = findViewById(R.id.fab_add);
        fabAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermsActivity.this, NewTermActivity.class);
                newTermActivityResultLauncher.launch(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview_terms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        LiveData<List<TermEntity>> termsList = mTermViewModel.getAllTerms();

        // Update the cached copy of the words in the adapter.
        mTermViewModel.getAllTerms().observe(this, adapter::submitList);

        //clickable recycler view item
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, recyclerView ,new RecyclerItemClickListener
                        .OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(TermsActivity.this,
                                TermDetailsActivity.class);
                        intent.putExtra("selected_term", Objects
                                .requireNonNull(termsList.getValue()).get(position));
                        startActivity(intent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        Intent intent = new Intent(TermsActivity.this,
                                EditTermActivity.class);
                        intent.putExtra("selected_term", Objects
                                .requireNonNull(termsList.getValue()).get(position));
                        editTermActivityResultLauncher.launch(intent);
                    }
                })
        );

        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
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
                        TermEntity selectedTerm = adapter.getTermAtPosition(position);

                        boolean canDelete = Boolean.TRUE.equals(mCourseViewModel
                                .canDeleteTerm(selectedTerm.getId()).getValue());
                        if (canDelete) {
                            mTermViewModel.delete(selectedTerm);
                            alert.cancelAlert(selectedTerm.getId());
                            Toast.makeText(TermsActivity.this,
                                    "Term deleted",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TermsActivity.this,
                                    "Cannot delete term as it has associated courses",
                                    Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });

        helper.attachToRecyclerView(recyclerView);
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

    private final ActivityResultLauncher<Intent> newTermActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String title = data.getStringExtra(NewTermActivity.ADD_TERM_TITLE);
                        String start = data.getStringExtra(NewTermActivity.ADD_TERM_START);
                        String end = data.getStringExtra(NewTermActivity.ADD_TERM_END);
                        TermEntity term = new TermEntity(title, DateConverter
                                .fromTimestamp(start), DateConverter.fromTimestamp(end));
                        mTermViewModel.insert(term);
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.term_empty_not_saved,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });


    private final ActivityResultLauncher<Intent> editTermActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        int id = Integer.parseInt(data.getStringExtra(EditTermActivity.TERM_ID));
                        String title = data.getStringExtra(EditTermActivity.EDIT_TERM_TITLE);
                        String start = data.getStringExtra(EditTermActivity.EDIT_TERM_START);
                        String end = data.getStringExtra(EditTermActivity.EDIT_TERM_END);
                        TermEntity term = new TermEntity(id, title, DateConverter
                                .fromTimestamp(start), DateConverter.fromTimestamp(end));
                        mTermViewModel.update(term);
                        mTermViewModel.getAllTerms()
                                .observe(TermsActivity.this, adapter::submitList);

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.term_empty_not_saved,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

}
