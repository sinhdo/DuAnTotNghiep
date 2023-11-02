package com.example.duantotnghiep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private List<Product> productList;

    public OrderDetailsAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (productList != null && productList.size() > position) {
            Product product = productList.get(position);
            holder.bind(product);
        }
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName, tvDescription, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_Od_Dt);
            tvProductName = itemView.findViewById(R.id.NameProduct_dt);
            tvDescription = itemView.findViewById(R.id.tvDescrip_dt);
            tvPrice = itemView.findViewById(R.id.priceOrder_dt);
        }

        public void bind(Product product) {
            tvProductName.setText(product.getName());
            tvDescription.setText(product.getDescription());
            tvPrice.setText("$ " + product.getPrice());

            if (product.getImgProduct() != null && !product.getImgProduct().isEmpty()) {
                Picasso.get().load(product.getImgProduct().get(0)).into(imgProduct);
            }
        }
    }
}

