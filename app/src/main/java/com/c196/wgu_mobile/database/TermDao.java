package com.c196.wgu_mobile.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.c196.wgu_mobile.entity.TermEntity;

import java.util.List;

@Dao
public interface TermDao {

    @Query("SELECT * FROM terms")
    LiveData<List<TermEntity>> getAllTerms();

    @Insert
    void insert(TermEntity term);

    @Update
    void update(TermEntity term);

    @Delete
    void delete(TermEntity term);

    @Query("SELECT id FROM terms WHERE title = :title")
    LiveData<Integer> getTermIdByTitle(String title);

    @Query("SELECT title FROM terms WHERE id = :id")
    LiveData<String> getTermTitleById(int id);

}
