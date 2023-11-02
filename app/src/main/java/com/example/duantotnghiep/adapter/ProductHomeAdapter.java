package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activity.OrderActivity;
import com.example.duantotnghiep.R;

import com.example.duantotnghiep.model.Product;
import com.example.duantotnghiep.databinding.ItemProductHomeBinding;

import java.util.List;

public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.ProductHomeViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductHomeAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }
    public void setData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHomeViewHolder(ItemProductHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHomeViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.binding.tvPriceHome.setText(String.valueOf(product.getPrice()) + " $");
        holder.binding.tvTitleHome.setText(product.getName());
        holder.binding.tvSoldHome.setText(String.valueOf(product.getSold()));
        if (product.getImgProduct() != null && !product.getImgProduct().isEmpty()) {
            Uri firstImageUri = Uri.parse(product.getImgProduct().get(0));
            Glide.with(context).load(firstImageUri).into(holder.binding.imvProductHome);
        } else {
            holder.binding.imvProductHome.setImageResource(R.drawable.tnf);
        }
        holder.binding.ctlProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("idPro",product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProductList(List<Product> updatedList) {
        productList = updatedList;
        notifyDataSetChanged();
    }

    public class ProductHomeViewHolder extends RecyclerView.ViewHolder {
        ItemProductHomeBinding binding;

        public ProductHomeViewHolder(ItemProductHomeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
