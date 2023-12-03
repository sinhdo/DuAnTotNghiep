package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.databinding.CircleColorBinding;

import java.util.ArrayList;
import java.util.List;

public class MutilpleColorAdapter extends RecyclerView.Adapter<MutilpleColorAdapter.ViewHolder> {
    private List<Integer> colorList;

    public MutilpleColorAdapter() {
        this.colorList = new ArrayList<>();
    }
    public MutilpleColorAdapter(List<Integer> colorList) {
        this.colorList = colorList;
    }

    public void setColorList(List<Integer> colorList) {
        this.colorList = colorList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext(); // Get the Context
        CircleColorBinding binding = CircleColorBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding, context);
    }
    public void updateColor(int color) {
        if (colorList.contains(color)) {

            colorList.remove(Integer.valueOf(color));
        } else {

            colorList.add(color);
        }
        notifyDataSetChanged();
    }





    @Override

    public void onBindViewHolder(@NonNull MutilpleColorAdapter.ViewHolder holder, int position) {
        Integer color = colorList.get(position);

        holder.bind(color);
    }


    @Override

    public int getItemCount() {
        return colorList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleColorBinding binding;
        private ImageView colorImageView;
        private Context context;
        public ViewHolder(CircleColorBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            colorImageView = binding.colorImageView1;
        }


        public void bind(Integer color) {

            GradientDrawable circle = new GradientDrawable();
            circle.setShape(GradientDrawable.OVAL);
            circle.setColor(color);

            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{context.getResources().getDrawable(R.drawable.circle_background), circle});
            colorImageView.setBackground(layerDrawable);
        }




    }
    public void updateSelectedColors(List<Integer> selectedColors) {
        if (selectedColors != null) {

            colorList = selectedColors;
        } else {
            colorList.clear();
        }
        notifyDataSetChanged();
    }
    public List<Integer> getSelectedColors() {
        return colorList;

    }



}
