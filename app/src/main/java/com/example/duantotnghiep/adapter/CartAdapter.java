package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.AddProductToCart;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<AddProductToCart> productsList;
    private Callback callback;
    TextView tv_totalbill;
    public CartAdapter(Context context,List<AddProductToCart> productsList, Callback callback) {
        this.context = context;
        this.productsList = productsList;
        this.callback = callback;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        AddProductToCart products = productsList.get(position);

        holder.tvNameProductCart.setText(products.getName_product());
        holder.tvNumProductCart.setText(products.getQuantity_product()+"");
        double totalPrice = products.getQuantity_product() * products.getPricetotal_product();
        String totalPriceFormatted = String.format(Locale.getDefault(), "Thành tiền: %.2f", totalPrice);
        holder.priceAllQuantity.setText(totalPriceFormatted);
        holder.tvColor.setBackgroundColor(products.getColor_product());
        holder.tvSize.setText(products.getSize_product());
        if (products.getImage_product() != null && !products.getImage_product().isEmpty()) {
            Uri imageUri = Uri.parse(products.getImage_product());
            Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.tnf)
                    .error(R.drawable.tnf)
                    .into(holder.imgProductCart);
        } else {
            holder.imgProductCart.setImageResource(R.drawable.tnf);
        }
        holder.imgDeleteProductCart.setOnClickListener(view -> {
            callback.deleteItemCart(products);
        });
        holder.cvLickitem.setOnClickListener(view -> {
            callback.updateItemCart(products);
        });
    }

    @Override
    public int getItemCount() {
        return productsList == null ? 0 : productsList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imgProductCart;
        private TextView tvNameProductCart;
        private TextView tvPriceProductCart;
        private TextView tvNumProductCart;
        private ImageView tvColor;
        private TextView tvSize;
        private CardView cvLickitem;
        private ImageView imgDeleteProductCart;
        private TextView priceAllQuantity;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            cvLickitem = (CardView) itemView.findViewById(R.id.cv_lickitem);
            imgProductCart = (ShapeableImageView) itemView.findViewById(R.id.img_productCart);
            tvNameProductCart = (TextView) itemView.findViewById(R.id.tv_nameProductCart);
            tvPriceProductCart = (TextView) itemView.findViewById(R.id.tv_priceProductCart);
            tvNumProductCart = (TextView) itemView.findViewById(R.id.tv_numProductCart);
            imgDeleteProductCart = (ImageView) itemView.findViewById(R.id.img_deleteProductCart);
            tvColor = (ImageView) itemView.findViewById(R.id.tv_colorProductCart);
            tvSize = (TextView) itemView.findViewById(R.id.tv_sizeProductCart);
            priceAllQuantity = (TextView) itemView.findViewById(R.id.priceAllQuantity);
        }
    }
    public interface Callback{
        void deleteItemCart(AddProductToCart products);
        void updateItemCart(AddProductToCart products);
    }
}
