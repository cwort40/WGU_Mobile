package com.c196.wgu_mobile.repository;

import static com.c196.wgu_mobile.database.AppDatabase.databaseWriteExecutor;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.c196.wgu_mobile.database.AppDatabase;
import com.c196.wgu_mobile.database.CourseDao;
import com.c196.wgu_mobile.entity.CourseEntity;

import java.util.List;

public class CourseRepository {

    private final CourseDao courseDao;
    private final LiveData<List<CourseEntity>> allCourses;

    public CourseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        courseDao = database.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
    }

    public void insert(CourseEntity course) {
        databaseWriteExecutor.execute(() -> {
            courseDao.insert(course);
        });
    }

    public void update(CourseEntity course) {
        databaseWriteExecutor.execute(() -> {
            courseDao.update(course);
        });
    }

    public void delete(CourseEntity course) {
        databaseWriteExecutor.execute(() -> {
            courseDao.delete(course);
        });
    }

    public LiveData<Integer> countCoursesByTermId(int id) {
        return courseDao.countCoursesByTermId(id);
    }


}
