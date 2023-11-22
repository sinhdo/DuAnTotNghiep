package com.example.duantotnghiep.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.activity.InforOrderActivity;
import com.example.duantotnghiep.adapter.OrderAdapter;
import com.example.duantotnghiep.model.Order;
import com.example.duantotnghiep.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class WaitForShopFragment extends Fragment implements OrderAdapter.Callback{
    private RecyclerView recyclerView;
    private OrderAdapter oderAdapter;
    private ArrayList<Order> list = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private TextView noResultsTextView;

    private DatabaseReference userRef;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public WaitForShopFragment() {
        // Required empty public constructor
    }

    public static WaitForShopFragment newInstance() {
        WaitForShopFragment fragment = new WaitForShopFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wait_for_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rec_waitforshop);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        oderAdapter = new OrderAdapter(getContext(), list, this);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);
        recyclerView.setAdapter(oderAdapter);
        GetDataWaitListForShop();
    }
    private void GetDataWaitListForShop() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("list_order");

        myReference.orderByChild("status").equalTo("waiting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order.getIdSeller().equals(id_user)) {
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
    private void dialogForShop(Order order) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_menu_order);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.bg_dialog_order));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;
        Button btnCancel = dialog.findViewById(R.id.btn1);
        Button btnExit = dialog.findViewById(R.id.btn2);
        btnExit.setText("Xác nhận đơn hàng");
        btnExit.setOnClickListener(view -> {
            order.setStatus("confirmed");
            deductAmountFromBuyer();
            UpdateStatus(order);
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Confirm Cancellation");
            builder.setMessage("Are you sure you want to cancel this order?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    order.setStatus("canceledbyshop");
                    UpdateStatus(order);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog1 = builder.create();
            dialog1.show();

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

        dialog.show();
    }
    private void deductAmountFromBuyer() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("list_order");
        Query query = orderRef.orderByChild("status").equalTo("waiting");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null && order.getIdBuyer().equals(currentUser.getUid())) {
                        deductAmountFromBuyer(order.getTotal(), order.getIdBuyer(), orderSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xảy ra lỗi khi đọc dữ liệu đơn hàng từ Firebase
                Toast.makeText(getContext(), "Failed to read order information", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deductAmountFromBuyer(final double amountToDeduct, final String buyerId, final String orderId) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("list_order").child("id");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String idBuyer = dataSnapshot.child("idBuyer").getValue(String.class);
                    double total = dataSnapshot.child("total").getValue(Double.class);

                    DatabaseReference buyerRef = FirebaseDatabase.getInstance().getReference().child("user").child(idBuyer);
                    buyerRef.runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            User buyer = mutableData.getValue(User.class);
                            if (buyer != null) {
                                double currentWallet = buyer.getWallet();
                                double newWallet = currentWallet - total;
                                buyer.setWallet(newWallet);
                                mutableData.setValue(buyer);
                                return Transaction.success(mutableData);
                            }
                            return Transaction.abort();
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                            if (committed) {
                                // Tiền đã được trừ thành công, tiếp tục cập nhật trạng thái đơn hàng
                                updateOrderStatus(orderId, "confirmed");
                                Dialog dialog = new Dialog(requireContext());
                                dialog.dismiss();
                            } else {
                                // Xảy ra lỗi khi cập nhật số dư của người mua
                                Toast.makeText(getContext(), "Failed to deduct amount from buyer's wallet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Không tìm thấy đơn hàng
                    Toast.makeText(getContext(), "Order not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xảy ra lỗi khi truy cập cơ sở dữ liệu
                Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateOrderStatus(String orderId, String newStatus) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("list_order").child(orderId);
        orderRef.child("status").setValue(newStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cập nhật trạng thái đơn hàng thành công
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xảy ra lỗi khi cập nhật trạng thái đơn hàng
                        Toast.makeText(getContext(), "Failed to update order status", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void UpdateStatus(Order order) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_order");
        String id = order.getId();
        myRef.child(id).setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(getContext(), "Update status", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Update fall", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void logic(Order order) {
        dialogForShop(order);
    }
}