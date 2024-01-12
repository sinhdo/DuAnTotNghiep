package com.example.duantotnghiep.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.InfoProductOrder;
import com.example.duantotnghiep.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapterUser extends RecyclerView.Adapter<OrderAdapterUser.OrderViewHolder>{
    private Context context;
    private List<Order> list;
    private OrderAdapter.Callback callback;
    private List<InfoProductOrder> infoProductOrderList;
    private String currentFragment;

    public OrderAdapterUser (Context context, List<Order> list, OrderAdapter.Callback callback) {
        this.context = context;
        this.list = list;
        this.infoProductOrderList = new ArrayList<>();
        this.currentFragment = currentFragment;
        this.callback = callback;
    }
    public List<InfoProductOrder> getInfoProductOrderList() {
        return infoProductOrderList;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderAdapterUser.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapterUser.OrderViewHolder holder, int position) {
        Order order = list.get(position);
        if (order == null) {
            return;
        }
        if (order.getCustomerImage() != null && !order.getCustomerImage().isEmpty()) {
            Uri imageUri = Uri.parse(order.getCustomerImage());
            Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.tnf)
                    .error(R.drawable.tnf)
                    .into(holder.img_byer);
        } else {
            holder.img_byer.setImageResource(R.drawable.tnf);
        }
        holder.tv_nameByer.setText("Đơn của: " + order.getCustomerName());
        holder.phoneByer.setText("SĐT: " +order.getNumberPhone());
        holder.adresByer.setText("Địa chỉ: " +order.getAddress());
        holder.tvNoteOrder.setText("NOTE: " +order.getNote());
        holder.soluong.setText(String.valueOf("Số lượng SP: : " +order.getTotalQuantity()));
        holder.tvDate.setText(order.getDate());

        final int clickedPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.logic(order);
                }
            }
        });
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog(clickedPosition);
            }
        });
    }
    private void showMenuDialog(int position) {
        Order order = list.get(position);
        Dialog menuDialog = new Dialog(context);
        menuDialog.setContentView(R.layout.dialog_menu_order);

        Button btnReview = menuDialog.findViewById(R.id.btn_review);
        Button btnPropety = menuDialog.findViewById(R.id.btn_propety);
        Button btnHuyDon = menuDialog.findViewById(R.id.btnHuyDon);
        Button btnOut = menuDialog.findViewById(R.id.btnOut);

        if (currentFragment.equals("CancleFragment")){
            btnReview.setText("Mua lại đơn hàng");
        }
        if (currentFragment.equals("WaitFragment")) {
            btnHuyDon.setVisibility(View.VISIBLE);
        } else {
            btnHuyDon.setVisibility(View.GONE);
        }
        if (currentFragment.equals("DoneFragment")) {
            btnReview.setVisibility(View.VISIBLE);
        } else {
            btnReview.setVisibility(View.GONE);
        }
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnPropety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDetailDialog(order.getListProduct());
                menuDialog.dismiss();
            }
        });
        btnHuyDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus(order.getId(), "Cancle");
                Toast.makeText(context, "Đã hủy đơn!!!", Toast.LENGTH_SHORT).show();
                menuDialog.dismiss();
            }
        });
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog.dismiss();
            }
        });

        menuDialog.show();
    }
    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }
    private void updateOrderStatus(String orderId, String status) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("list_order");
        DatabaseReference orderRef = ordersRef.child(orderId).child("status");
        orderRef.setValue(status);
    }
    private void showOrderDetailDialog(List<InfoProductOrder> productList) {
        Dialog orderDetailDialog = new Dialog(context);
        orderDetailDialog.setContentView(R.layout.dialog_order_detail);
        RecyclerView recyclerView = orderDetailDialog.findViewById(R.id.recyclerViewHL);
        ProductOrderAdapter productOrderAdapter = new ProductOrderAdapter(context, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(productOrderAdapter);
        productOrderAdapter.setProductList(productList);
        orderDetailDialog.show();

        for (InfoProductOrder product : productList) {
            Log.d("Product", "Name: " + product.getNamePr() + ", Price: " + product.getPrice());
        }
    }
    public void setProductList(List<InfoProductOrder> productList) {
        this.infoProductOrderList = productList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_byer;
        private TextView tv_nameByer, adresByer, phoneByer, soluong, tvNoteOrder, tvDate;
        private ImageView imgMenu;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            img_byer = (ImageView) itemView.findViewById(R.id.img_byer);
            tv_nameByer = (TextView) itemView.findViewById(R.id.tv_nameByer);
            adresByer = (TextView) itemView.findViewById(R.id.adresByer);
            phoneByer = (TextView) itemView.findViewById(R.id.phoneByer);
            imgMenu = (ImageView) itemView.findViewById(R.id.img_menu);
            soluong = (TextView) itemView.findViewById(R.id.soluong);
            tvNoteOrder = (TextView) itemView.findViewById(R.id.tvNoteOrder);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

        }
    }
    public interface Callback {
        void logic(Order order);
    }
}