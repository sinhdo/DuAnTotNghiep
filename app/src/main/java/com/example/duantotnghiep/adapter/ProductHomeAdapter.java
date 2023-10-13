package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.databinding.ItemProductHomeBinding;

public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.ProductHomeViewHolder> {
    Context context;
    public ProductHomeAdapter(Context context) {

        this.context = context;
    }
    @NonNull
    @Override
    public ProductHomeAdapter.ProductHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHomeAdapter.ProductHomeViewHolder(ItemProductHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHomeAdapter.ProductHomeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ProductHomeViewHolder extends RecyclerView.ViewHolder {
        ItemProductHomeBinding binding;
        public ProductHomeViewHolder(ItemProductHomeBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;
        }
    }
}
