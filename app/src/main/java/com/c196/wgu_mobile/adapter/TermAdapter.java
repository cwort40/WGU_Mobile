package com.c196.wgu_mobile.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.viewholder.TermViewHolder;

public class TermAdapter extends ListAdapter<TermEntity, TermViewHolder> {

    public TermAdapter(@NonNull DiffUtil.ItemCallback<TermEntity> diffCallback) {
        super(diffCallback);
    }

    @Override
    public TermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TermViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TermViewHolder holder, int position) {
        TermEntity current = getItem(position);
        holder.bind(current.getTerm());
    }

    public TermEntity getTermAtPosition(int position) {
        return getItem(position);
    }

    public static class TermDiff extends DiffUtil.ItemCallback<TermEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull TermEntity oldItem, @NonNull TermEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TermEntity oldItem,
                                          @NonNull TermEntity newItem) {
            return oldItem.getTerm().equals(newItem.getTerm());
        }
    }

}