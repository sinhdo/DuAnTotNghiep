package com.example.duantotnghiep.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.activity.InforOrderActivity;
import com.example.duantotnghiep.activity.ReviewsActivity;
import com.example.duantotnghiep.adapter.OrderAdapter;
import com.example.duantotnghiep.model.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoneFragment extends Fragment implements OrderAdapter.Callback{
    private RecyclerView recyclerView;
    private OrderAdapter oderAdapter;
    private ArrayList<Order> list = new ArrayList<>();
    private FirebaseUser firebaseUser;

    private TextView noResultsTextView;


    public DoneFragment() {
        // Required empty public constructor
    }

    public static DoneFragment newInstance() {
        DoneFragment fragment = new DoneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_done, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rec_done);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        noResultsTextView = view.findViewById(R.id.noResultsTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        oderAdapter = new OrderAdapter(getContext(), list, this);
        recyclerView.setAdapter(oderAdapter);
        GetDataDoneListForBuyer();
    }

    private void dialogForUser(Order order) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_menu_order);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.bg_dialog_order));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;
        Button btnCancel = dialog.findViewById(R.id.btn1);
        btnCancel.setVisibility(View.GONE);
        Button btnExit = dialog.findViewById(R.id.btn2);
        Button btn_review = dialog.findViewById(R.id.btn_review);
        btn_review.setVisibility(View.VISIBLE);
        btnExit.setOnClickListener(view -> {
            dialog.cancel();
        });
        Button tt = dialog.findViewById(R.id.btn_propety);
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InforOrderActivity.class);
                intent.putExtra("idOrder",order.getId());
                startActivity(intent);
            }
        });


        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReviewsActivity.class);
                intent.putExtra("idOrder",order.getId());
                startActivity(intent);
            }
        });

        dialog.show();
    }





    private void saveReviewToFirebase(String orderId, String reviewComment, int rating, String userId, String idReview, String idProduct) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference();

        DatabaseReference reviewsRef = myReference.child("reviews");

        String reviewId = reviewsRef.push().getKey(); // Tạo id mới cho đánh giá

        userId = firebaseUser.getUid();


        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("order_id", orderId);
        reviewData.put("comment", reviewComment);
        reviewData.put("rating", rating);
        reviewData.put("user_id", userId);
        reviewData.put("id_review", reviewId); // Gán giá trị id_review mới tạo


        // Truy vấn id_product từ id_order
        DatabaseReference ordersRef = myReference.child("list_order").child(orderId);
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String idProduct = snapshot.child("idProduct").getValue(String.class);

                    // Truy vấn danh sách sản phẩm để tìm sản phẩm có id_product trùng khớp
                    DatabaseReference productsRef = myReference.child("products");
                    productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productsSnapshot) {
                            for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                                String productId = productSnapshot.getKey();

                                // So sánh id_product từ list_order với id của từng sản phẩm trong danh sách sản phẩm
                                if (idProduct.equals(productId)) {
                                    String productName = productSnapshot.child("productName").getValue(String.class);
                                    // Thêm thông tin sản phẩm vào dữ liệu đánh giá
                                    reviewData.put("id_product", idProduct);
                                    reviewData.put("product_name", productName);

                                    // Lưu đánh giá với thông tin sản phẩm vào collection "reviews"
                                    reviewsRef.child(reviewId).setValue(reviewData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Thông báo thành công khi thêm đánh giá vào collection "reviews"
                                                    Toast.makeText(getContext(), "Đánh giá đã được thêm vào.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Thông báo khi thêm đánh giá không thành công
                                                    Toast.makeText(getContext(), "Thêm đánh giá thất bại.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    break; // Kết thúc vòng lặp sau khi tìm thấy sản phẩm tương ứng
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý khi có lỗi truy xuất dữ liệu danh sách sản phẩm
                        }
                    });
                } else {
                    // Xử lý khi không tìm thấy thông tin đơn hàng
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi truy xuất dữ liệu đơn hàng
            }
        });
    }



    private void getProductDetails(String idOrder) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("list_order").child(idOrder);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String idProduct = snapshot.child("idProduct").getValue(String.class);

                    if (idProduct != null) {
                        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products").child(idProduct);

                        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Retrieve product details here
                                    String productName = snapshot.child("productName").getValue(String.class);
                                    // Continue with the product details as needed
                                } else {
                                    // Handle case where product details are not found
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database error while fetching product details
                            }
                        });
                    }
                } else {
                    // Handle case where order details are not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error while fetching order details
            }
        });
    }
    private void GetDataDoneListForBuyer() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("list_order");

        myReference.orderByChild("status").equalTo("done").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order.getIdBuyer().equals(id_user)) {
                        list.add(order);
                    }
                }
                if (list.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    noResultsTextView.setVisibility(View.VISIBLE);

                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noResultsTextView.setVisibility(View.GONE);
                }
                oderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list order failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void logic(Order order) {
        getProductDetails(order.getId());
        dialogForUser(order);
    }
}