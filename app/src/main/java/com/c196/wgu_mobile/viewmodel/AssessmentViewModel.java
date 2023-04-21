package com.c196.wgu_mobile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.c196.wgu_mobile.entity.AssessmentEntity;
import com.c196.wgu_mobile.repository.AssessmentRepository;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private final AssessmentRepository repository;
    private final LiveData<List<AssessmentEntity>> allAssessments;
    private LiveData<List<AssessmentEntity>> assessmentsForCourse;
    int courseId;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public void insert(AssessmentEntity assessment) {
        repository.insert(assessment);
    }

    public void update(AssessmentEntity assessment) {
        repository.update(assessment);
    }

    public void delete(AssessmentEntity assessment) {
        repository.delete(assessment);
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() {
        return allAssessments;
    }

}

