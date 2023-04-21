package com.c196.wgu_mobile.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.c196.wgu_mobile.R;

public class AssessmentViewHolder extends RecyclerView.ViewHolder {

    private final TextView assessmentItemView;

    public AssessmentViewHolder(View itemView) {
        super(itemView);
        assessmentItemView = itemView.findViewById(R.id.title_text_view);
    }

    public void bind(String text) {
        assessmentItemView.setText(text);
    }

    public static AssessmentViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new AssessmentViewHolder(view);
    }

}
