package com.c196.wgu_mobile.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.repository.CourseRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final CourseRepository mRepository;

    private final LiveData<List<CourseEntity>> mAllCourses;
    private final MutableLiveData<String> mSelectedStatus = new MutableLiveData<>();

    public CourseViewModel(Application application) {
        super(application);
        mRepository = new CourseRepository(application);
        mAllCourses = mRepository.getAllCourses();
    }

    public LiveData<List<CourseEntity>> getAllCourses() { return mAllCourses; }

    public void insert(CourseEntity course) { mRepository.insert(course); }

    public void update(CourseEntity course) { mRepository.update(course); }

    public void delete(CourseEntity course) { mRepository.delete(course); }

    public MutableLiveData<String> getSelectedStatus() {
        return mSelectedStatus;
    }

    public void setSelectedStatus(String status) {
        mSelectedStatus.setValue(status);
    }

    public LiveData<Boolean> canDeleteTerm(int id) {
        LiveData<Integer> coursesCount = mRepository.countCoursesByTermId(id);
        return Transformations.map(coursesCount, count -> count == 0);
    }


}

