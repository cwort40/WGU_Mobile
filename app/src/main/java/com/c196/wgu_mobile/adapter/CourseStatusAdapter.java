package com.c196.wgu_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.c196.wgu_mobile.R;

import java.util.List;

public class CourseStatusAdapter extends RecyclerView.Adapter<CourseStatusAdapter
        .CourseStatusViewHolder> {
    private final List<String> statusOptions;
    private int selectedPosition = -1;
    Context mContext;
    private String selectedStatus;

    public CourseStatusAdapter(Context context, List<String> statusOptions) {
        mContext = context;
        this.statusOptions = statusOptions;
    }

    @Override
    public CourseStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.checkbox_item, parent, false);
        return new CourseStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseStatusViewHolder holder, int position) {
        holder.checkBox.setText(statusOptions.get(position));
        holder.checkBox.setChecked(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return statusOptions.size();
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public class CourseStatusViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public CourseStatusViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedPosition = getAdapterPosition();
                    selectedStatus = statusOptions.get(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}

