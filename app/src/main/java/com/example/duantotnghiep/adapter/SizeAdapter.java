package com.example.duantotnghiep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {
    private List<String> selectedSizes;

    public SizeAdapter(List<String> selectedSizes) {
        this.selectedSizes = selectedSizes;
    }

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_size, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        String size = selectedSizes.get(position);
        holder.bind(size);
    }

    @Override
    public int getItemCount() {
        return selectedSizes.size();
    }

    public class SizeViewHolder extends RecyclerView.ViewHolder {
        private TextView sizeTextView;

        public SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
        }

        public void bind(String size) {
            sizeTextView.setText(size);
        }
    }
}
