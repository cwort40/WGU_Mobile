package com.c196.wgu_mobile.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.c196.wgu_mobile.entity.AssessmentEntity;
import com.c196.wgu_mobile.viewholder.AssessmentViewHolder;

public class AssessmentAdapter extends ListAdapter<AssessmentEntity, AssessmentViewHolder> {

    public AssessmentAdapter(@NonNull DiffUtil.ItemCallback<AssessmentEntity> diffCallback) {
        super(diffCallback);
    }

    @Override
    public AssessmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AssessmentViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(AssessmentViewHolder holder, int position) {
        AssessmentEntity current = getItem(position);
        holder.bind(current.getTitle());
    }

    public AssessmentEntity getAssessmentAtPosition(int position) {
        return getItem(position);
    }

    public static class AssessmentDiff extends DiffUtil.ItemCallback<AssessmentEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull AssessmentEntity oldItem,
                                       @NonNull AssessmentEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AssessmentEntity oldItem,
                                          @NonNull AssessmentEntity newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }

}

