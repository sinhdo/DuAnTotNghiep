package com.example.duantotnghiep.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.databinding.ItemProductBinding;
import com.example.duantotnghiep.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    private List<Product> productList;
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductAdapter.ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    public void updateProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.binding.tvProduct.setText(product.getName());
        holder.binding.priceProduct.setText(String.valueOf(product.getPrice()));
        holder.binding.quantity.setText("Số lượng "+product.getQuantity());
        holder.binding.sold.setText(String.valueOf(product.getSold()));
        if (product.getImgProduct() != null && !product.getImgProduct().isEmpty()) {
            Uri firstImageUri = Uri.parse(product.getImgProduct().get(0));

            Glide.with(context).load(firstImageUri).into(holder.binding.imageProduct);
        } else {

            holder.binding.imageProduct.setImageResource(R.drawable.tnf);
        }

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEdit();
            }
        });
    }

    private void showDialogEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.edit_dialog_product,null);
        builder.setView(view);

        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public ProductViewHolder(ItemProductBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;

        }
    }

}
