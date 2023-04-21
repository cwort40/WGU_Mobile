package com.c196.wgu_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.c196.wgu_mobile.R;

import java.util.List;

public class AssessmentTypeAdapter extends RecyclerView.Adapter<AssessmentTypeAdapter
        .AssessmentTypeViewHolder> {
    private final List<String> typeOptions;
    private int selectedPosition = -1;
    Context mContext;
    private String selectedType;

    public AssessmentTypeAdapter(Context context, List<String> typeOptions) {
        mContext = context;
        this.typeOptions = typeOptions;
    }

    @Override
    public AssessmentTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.checkbox_item, parent, false);
        return new AssessmentTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssessmentTypeViewHolder holder, int position) {
        holder.checkBox.setText(typeOptions.get(position));
        holder.checkBox.setChecked(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return typeOptions.size();
    }

    public String getSelectedType() {
        return selectedType;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public class AssessmentTypeViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public AssessmentTypeViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedPosition = getAdapterPosition();
                    selectedType = typeOptions.get(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
