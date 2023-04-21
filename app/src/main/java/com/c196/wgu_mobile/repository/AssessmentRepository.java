package com.c196.wgu_mobile.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.c196.wgu_mobile.database.AppDatabase;
import com.c196.wgu_mobile.database.AssessmentDao;
import com.c196.wgu_mobile.entity.AssessmentEntity;

import java.util.List;

public class AssessmentRepository {
    private final AssessmentDao mAssessmentDao;
    private final LiveData<List<AssessmentEntity>> mAllAssessments;
    private LiveData<List<AssessmentEntity>> mAssessmentsForCourse;

    public AssessmentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mAssessmentDao = db.assessmentDao();
        mAllAssessments = mAssessmentDao.getAllAssessments();
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() {
        return mAllAssessments;
    }

    public void insert(AssessmentEntity assessment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.insert(assessment);
        });
    }

    public void update(AssessmentEntity assessment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.update(assessment);
        });
    }

    public void delete(AssessmentEntity assessment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.delete(assessment);
        });
    }
}

