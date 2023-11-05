package com.example.duantotnghiep.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private List<Product> productList;
    private List<Integer> colorList;

    public OrderDetailsAdapter() {
        this.productList = productList;
    }

    public void setColorList(List<Integer> colorList) {
        this.colorList = colorList;
        notifyDataSetChanged();
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
            holder.bind(product, colorList, position);
        }
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName, tvSize, tvPrice, tvCum, tvColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_Od_Dt);
            tvProductName = itemView.findViewById(R.id.NameProduct_dt);
            tvSize = itemView.findViewById(R.id.tvSize_dt);
            tvColor = itemView.findViewById(R.id.tvColor_dt);
            tvPrice = itemView.findViewById(R.id.priceOrder_dt);
            tvCum = itemView.findViewById(R.id.tvCum_dt);
        }

        public void bind(Product product, List<Integer> colorList, int position) {
            tvProductName.setText(product.getName());

            tvSize.setText(String.format("Cỡ %s", TextUtils.join(", ", product.getSize())));

            if (colorList != null && colorList.size() > position) {
                int color = colorList.get(position);
                tvColor.setBackgroundColor(color);
            }

            tvPrice.setText("$ " + product.getPrice());

            tvCum.setText(String.format("Số lượng %s", product.getQuantity()));

            if (product.getImgProduct() != null && !product.getImgProduct().isEmpty()) {
                Picasso.get().load(product.getImgProduct().get(0)).into(imgProduct);
            }
        }
    }
}