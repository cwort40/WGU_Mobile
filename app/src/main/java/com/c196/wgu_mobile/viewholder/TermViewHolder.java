package com.c196.wgu_mobile.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.c196.wgu_mobile.R;

public class TermViewHolder extends RecyclerView.ViewHolder {

    private final TextView termItemView;

    public TermViewHolder(View itemView) {
        super(itemView);
        termItemView = itemView.findViewById(R.id.title_text_view);
    }

    public void bind(String text) {
        termItemView.setText(text);
    }

    public static TermViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new TermViewHolder(view);
    }
}
