package com.c196.wgu_mobile.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.c196.wgu_mobile.entity.AssessmentEntity;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Insert
    void insert(AssessmentEntity assessment);

    @Update
    void update(AssessmentEntity assessment);

    @Delete
    void delete(AssessmentEntity assessment);

    @Query("SELECT * FROM assessments")
    LiveData<List<AssessmentEntity>> getAllAssessments();
}

