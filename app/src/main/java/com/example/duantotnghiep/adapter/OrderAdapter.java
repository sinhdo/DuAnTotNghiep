package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.Order;
import com.example.duantotnghiep.model.Reviews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> list;
    private Callback callback;

    private List<String> userIds;


    public OrderAdapter(Context context, List<Order> list, Callback callback) {
        this.context = context;
        this.list = list;
        this.callback = callback;

    }

    public void updateReviewsForProduct(String productId, List<Reviews> reviewList) {
        // Tìm đơn hàng có productId tương ứng trong danh sách đơn hàng
        for (Order order : list) {
            if (order.getId().equals(productId)) {
                order.setReviewList(reviewList); // Đặt danh sách đánh giá cho đơn hàng này
                notifyDataSetChanged(); // Thông báo sự thay đổi để cập nhật giao diện
                return;
            }
        }
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

        //
        String productId = list.get(position).getId(); // Lấy productId từ Order tại vị trí position
       // Gọi phương thức để lấy dữ liệu đánh giá từ Firebase và hiển thị lên RecyclerView
        fetchReviewsForProduct(productId, holder);
        //
        if (TextUtils.isEmpty(oder.getNotes()) || oder.getNotes().equals("")) {
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

    private void fetchReviewsForProduct(String productId, OrderViewHolder holder) {
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("reviews");

        reviewsRef.orderByChild("product_id").equalTo(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Reviews> reviewList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reviews review = dataSnapshot.getValue(Reviews.class);
                    if (review != null) {
                        reviewList.add(review);
                    }
                }

                if (!reviewList.isEmpty()) {
                    // Hiển thị RecyclerView cho đánh giá
                    ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewList );
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.recyclerView.setAdapter(reviewAdapter);
                    holder.recyclerView.setVisibility(View.VISIBLE);
                } else {
//                    holder.recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(context, "Lỗi khi tải dữ liệu đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct, colorProduct;
        private RecyclerView recyclerView;
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
//            recyclerView = (RecyclerView) itemView.findViewById(R.id.rcv_review);

        }
    }

    public interface Callback {
        void logic(Order order);
    }

}
