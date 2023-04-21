package com.c196.wgu_mobile.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.repository.TermRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private final TermRepository mRepository;

    private final LiveData<List<TermEntity>> mAllTerms;

    public TermViewModel(Application application) {
        super(application);
        mRepository = new TermRepository(application);
        mAllTerms = mRepository.getAllTerms();
    }

    public LiveData<List<TermEntity>> getAllTerms() { return mAllTerms; }

    public void insert(TermEntity term) { mRepository.insert(term); }

    public void update(TermEntity term) { mRepository.update(term); }

    public boolean delete(TermEntity term) {
        mRepository.delete(term);
        return true;
    }

    public LiveData<Integer> getTermIdByTitle(String title) {
        return mRepository.getTermIdByTitle(title);
    }

    public LiveData<String> getTermTitleById(int id) {
        return mRepository.getTermTitleById(id);
    }
}
