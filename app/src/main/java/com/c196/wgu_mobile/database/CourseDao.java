package com.c196.wgu_mobile.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.c196.wgu_mobile.entity.CourseEntity;

import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM courses")
    LiveData<List<CourseEntity>> getAllCourses();

    @Insert
    void insert(CourseEntity course);

    @Update
    void update(CourseEntity course);

    @Delete
    void delete(CourseEntity course);

    @Query("SELECT COUNT(*) FROM courses WHERE term_id = :id")
    LiveData<Integer> countCoursesByTermId(int id);


}

