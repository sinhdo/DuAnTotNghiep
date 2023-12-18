package com.example.duantotnghiep.fragment;

import android.app.Dialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WaitFragment extends Fragment implements OrderAdapter.Callback{
    private RecyclerView recyclerView;
    private OrderAdapter oderAdapter;
    private ArrayList<Order> list = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private TextView noResultsTextView;


    // TODO: Rename and change types of parameters
    public WaitFragment() {
        // Required empty public constructor
    }

    public static WaitFragment newInstance() {
        WaitFragment fragment = new WaitFragment();
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
        return inflater.inflate(R.layout.fragment_wait, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rec_wait);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        oderAdapter = new OrderAdapter(getContext(), list, this);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);
        recyclerView.setAdapter(oderAdapter);
        GetDataWaitListForBuyer();
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
        Button btnExit = dialog.findViewById(R.id.btn2);
        Button btn_review = dialog.findViewById(R.id.btn_review);
        btn_review.setVisibility(View.INVISIBLE);
        btnExit.setOnClickListener(view -> {
            dialog.cancel();
        });
        btnCancel.setOnClickListener(view -> {
            order.setStatus("canceled");
            UpdateStatus(order);
            ReturnmoneyForBuyer(order);
            dialog.dismiss();
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

    private void GetDataWaitListForBuyer() {
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
        dialogForUser(order);
    }
    private void ReturnmoneyForBuyer(Order order) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference orderRef = firebaseDatabase.getReference("list_order");
        DatabaseReference buyerRef = firebaseDatabase.getReference("user").child(order.getIdBuyer()).child("wallet");
        DatabaseReference sellerRef = firebaseDatabase.getReference("user").child(order.getIdSeller()).child("wallet");
        String id = order.getId();
        boolean checkPaid = order.getPaid();
        order.setPaid(checkPaid);
        orderRef.child(id).setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    if (checkPaid) {
                        buyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    double buyerBalance = snapshot.getValue(Double.class);
                                    double newBuyerBalance = buyerBalance + order.getTotal();
                                    buyerRef.setValue(newBuyerBalance);
                                    Toast.makeText(getContext(), "Bạn đã được hoàn "+order.getTotal()+ " VND", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Update status", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}