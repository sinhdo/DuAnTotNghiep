package com.example.duantotnghiep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    private List<Integer> colors;
    public ColorAdapter(List<Integer> colors) {
        this.colors = colors;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int color = colors.get(position);
        holder.setColor(color);
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        private ImageView colorImageView;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorImageView = itemView.findViewById(R.id.colorImageView);
        }

        public void setColor(int color) {
            colorImageView.setBackgroundColor(color);
            colorImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        colors.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        }
    }
}
