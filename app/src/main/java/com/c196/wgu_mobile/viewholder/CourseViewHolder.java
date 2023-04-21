package com.c196.wgu_mobile.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.c196.wgu_mobile.R;

public class CourseViewHolder extends RecyclerView.ViewHolder {

    private final TextView courseItemView;

    public CourseViewHolder(View itemView) {
        super(itemView);
        courseItemView = itemView.findViewById(R.id.title_text_view);
    }

    public void bind(String text) {
        courseItemView.setText(text);
    }

    public static CourseViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new CourseViewHolder(view);
    }

}
