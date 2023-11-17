package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.Order;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.security.auth.callback.Callback;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> list;
    private Callback callback;

    public OrderAdapter(Context context, List<Order> list, Callback callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order oder = list.get(position);
        if (oder == null) {
            return;
        }
        if (oder.getImgProduct() != null && !oder.getImgProduct().isEmpty()) {
            Uri imageUri = Uri.parse(oder.getImgProduct());
            Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.tnf)
                    .error(R.drawable.tnf)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.tnf);
        }
        holder.tvNameProduct.setText("Sản phẩm: " + oder.getNameProduct());
        holder.tvQuantity.setText(String.valueOf(oder.getQuantity()));
        holder.tvTotal.setText(String.valueOf(oder.getTotal()));
        holder.colorProduct.setBackgroundColor(oder.getColor());
        if (oder.getNotes().isEmpty() || oder.getNotes() == "") {
            holder.tvnote.setVisibility(View.GONE);
        } else {
            holder.tvnote.setText("Note : " + oder.getNotes());
        }
        if (oder.getStatus().equals("deliver")||oder.getStatus().equals("done")){
            holder.tv_paid.setVisibility(View.VISIBLE);
            if (oder.getPaid()==true){
                holder.tv_paid.setText("Đã thanh toán bằng ví");
            }else {
                holder.tv_paid.setText("Thanh toán khi nhận hàng");
            }
        }
        holder.imgMenu.setOnClickListener(view -> {
            callback.logic(oder);
        });

    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct, colorProduct;
        private TextView tvNameProduct;
        private TextView tvQuantity;
        private TextView tvTotal;
        private TextView tvnote;
        private TextView tv_paid;
        private ImageView imgMenu;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
            colorProduct = (ImageView) itemView.findViewById(R.id.tv_color);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tv_nameProduct);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvTotal = (TextView) itemView.findViewById(R.id.tv_total);
            imgMenu = (ImageView) itemView.findViewById(R.id.img_menu);
            tvnote = (TextView) itemView.findViewById(R.id.tvNoteOrder);
            tv_paid = (TextView) itemView.findViewById(R.id.tv_paid);
        }
    }

    public interface Callback {
        void logic(Order order);
    }

}
