package com.c196.wgu_mobile.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.c196.wgu_mobile.entity.CourseEntity;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.viewholder.CourseViewHolder;
import com.c196.wgu_mobile.viewholder.TermViewHolder;

public class CourseAdapter extends ListAdapter<CourseEntity, CourseViewHolder>  {

    public CourseAdapter(@NonNull DiffUtil.ItemCallback<CourseEntity> diffCallback) {
        super(diffCallback);
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CourseViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        CourseEntity current = getItem(position);
        holder.bind(current.getTitle());
    }

    public CourseEntity getCourseAtPosition(int position) {
        return getItem(position);
    }

    public static class CourseDiff extends DiffUtil.ItemCallback<CourseEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CourseEntity oldItem,
                                          @NonNull CourseEntity newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }

}
