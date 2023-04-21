package com.c196.wgu_mobile.entity;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;
import static androidx.room.ForeignKey.RESTRICT;

import static java.sql.Types.NULL;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.c196.wgu_mobile.util.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "courses")
//        ,
//        foreignKeys = @ForeignKey(entity = TermEntity.class,
//                parentColumns = "id",
//                childColumns = "term_id"),
//        indices = {@Index("term_id")})
@TypeConverters({DateConverter.class})
public class CourseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "term_id")
    private int termId;

    @ColumnInfo(name = "title")
    private String title;

    private Date startDate;

    private Date endDate;

    private String status;

    private String instructorName;

    private String instructorPhone;

    private String instructorEmail;

    private String note;

    @Ignore
    public CourseEntity(int id, String title, int termId, Date startDate, Date endDate, String status,
                        String instructorName, String instructorPhone, String instructorEmail,
                        String note) {
        this.id = id;
        this.title = title;
        this.termId = termId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.note = note;
    }

    public CourseEntity(String title, int termId, Date startDate, Date endDate, String status,
                        String instructorName, String instructorPhone, String instructorEmail,
                        String note) {
        this.title = title;
        this.termId = termId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

