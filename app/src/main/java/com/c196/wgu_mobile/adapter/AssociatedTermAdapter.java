package com.c196.wgu_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.c196.wgu_mobile.R;
import com.c196.wgu_mobile.entity.TermEntity;
import com.c196.wgu_mobile.viewmodel.TermViewModel;

import java.util.ArrayList;
import java.util.List;

public class AssociatedTermAdapter extends RecyclerView.Adapter<AssociatedTermAdapter
        .AssociatedTermViewHolder> {

    private List<String> termOptions = new ArrayList<>();
    private int selectedPosition = -1;
    private String selectedTerm;
    private TermViewModel mTermViewModel;
    private ViewModelStoreOwner viewModelStoreOwner;

    public AssociatedTermAdapter(ViewModelStoreOwner viewModelStoreOwner) {
        this.viewModelStoreOwner = viewModelStoreOwner;
        mTermViewModel = new ViewModelProvider(viewModelStoreOwner).get(TermViewModel.class);
        mTermViewModel.getAllTerms().observe((LifecycleOwner) viewModelStoreOwner, termEntities -> {
            termOptions.clear();
            for (TermEntity termEntity : termEntities) {
                termOptions.add(termEntity.getTitle());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public AssociatedTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.checkbox_item, parent, false);
        return new AssociatedTermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssociatedTermViewHolder holder, int position) {
        holder.checkBox.setText(termOptions.get(position));
        holder.checkBox.setChecked(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return termOptions.size();
    }

    public String getSelectedTerm() {
        return selectedTerm;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }


    public class AssociatedTermViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public AssociatedTermViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedPosition = getAdapterPosition();
                    selectedTerm = termOptions.get(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}

