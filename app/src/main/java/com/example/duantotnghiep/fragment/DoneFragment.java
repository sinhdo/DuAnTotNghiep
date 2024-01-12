package com.example.duantotnghiep.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.duantotnghiep.adapter.OrderAdapterUser;
import com.example.duantotnghiep.model.InfoProductOrder;
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
import java.util.List;
import java.util.Map;

public class DoneFragment extends Fragment implements OrderAdapter.Callback{
    private RecyclerView recyclerView;
    private OrderAdapterUser orderAdapter;
    private ArrayList<Order> list = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private TextView noResultsTextView;
    private String currentFragment = "DoneFragment";


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
        recyclerView = view.findViewById(R.id.rec_wait);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapterUser(getContext(), list, this);
        orderAdapter.setCurrentFragment(currentFragment);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);
        recyclerView.setAdapter(orderAdapter);
        GetDataDoneListForBuyer();
    }
    private void GetDataDoneListForBuyer() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("list_order");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null && order.getIdSeller().equals(id_user)) {
                        if (order.getStatus().equals("Done")) {
                            list.add(order);
                        }
                    } else {
                        Toast.makeText(getContext(), "NULL", Toast.LENGTH_SHORT).show();
                    }
                }
                orderAdapter.notifyDataSetChanged();
                if (!list.isEmpty()) {
                    orderAdapter.setProductList(list.get(0).getListProduct());
                }
                if (list.isEmpty()) {
                    noResultsTextView.setVisibility(View.VISIBLE);
                } else {
                    noResultsTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list order failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void logic(Order order) {

    }
}