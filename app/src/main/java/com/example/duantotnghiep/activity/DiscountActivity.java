package com.example.duantotnghiep.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.DiscountAdapter;
import com.example.duantotnghiep.model.Discount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscountActivity extends AppCompatActivity {

    private EditText edtNameDiscount, edtValueDiscount;
    RecyclerView recyclerView;
    private Button btnAddDiscount;
    private DatabaseReference discountRef;
    private DiscountAdapter discountAdapter;
    private List<Discount> discountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);


        discountRef = FirebaseDatabase.getInstance().getReference("discounts");


        edtNameDiscount = findViewById(R.id.NameDiscount);
        edtValueDiscount = findViewById(R.id.ValueDiscount);
        btnAddDiscount = findViewById(R.id.btnAddDiscount);
        RecyclerView recyclerView = findViewById(R.id.rv_discount);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        discountList = new ArrayList<>();
        discountAdapter = new DiscountAdapter(discountList);
        recyclerView.setAdapter(discountAdapter);
        btnAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDiscount();
            }
        });
        getDiscountsFromFirebase();
    }

    private void addDiscount() {

        String name = edtNameDiscount.getText().toString().trim();
        String valueString = edtValueDiscount.getText().toString().trim();


        if (name.isEmpty() || valueString.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }


        double value = Double.parseDouble(valueString);


        Discount discount = new Discount();
        discount.setCode(name);
        discount.setAmount(value);


        String discountId = discountRef.push().getKey();
        if (discountId != null) {
            discountRef.child(discountId).setValue(discount);


            Toast.makeText(this, "Thêm Discount thành công", Toast.LENGTH_SHORT).show();


            edtNameDiscount.setText("");
            edtValueDiscount.setText("");
        } else {
            Toast.makeText(this, "Lỗi khi thêm Discount", Toast.LENGTH_SHORT).show();
        }
    }
    private void getDiscountsFromFirebase() {

        discountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                discountList.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Discount discount = snapshot.getValue(Discount.class);


                    discountList.add(discount);
                }


                discountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DiscountActivity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
