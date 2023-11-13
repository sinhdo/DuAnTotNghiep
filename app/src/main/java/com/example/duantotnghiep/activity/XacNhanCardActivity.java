package com.example.duantotnghiep.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.XacNhanCardAdapter;
import com.example.duantotnghiep.model.Card;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class XacNhanCardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private XacNhanCardAdapter xacNhanCardAdapter;
    private List<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_card);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardList = new ArrayList<>();
        xacNhanCardAdapter = new XacNhanCardAdapter(this,cardList);
        recyclerView.setAdapter(xacNhanCardAdapter);

        // Kết nối đến Firebase và lấy dữ liệu
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cards");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cardList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Card card = dataSnapshot.getValue(Card.class);
                    cardList.add(card);
                }
                xacNhanCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        });

    }
}