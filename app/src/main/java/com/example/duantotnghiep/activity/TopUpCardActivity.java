package com.example.duantotnghiep.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.CardAdapter;
import com.example.duantotnghiep.model.Card;
import com.example.duantotnghiep.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TopUpCardActivity extends AppCompatActivity {
    private EditText etCardSerial, etCardPin;
    private Spinner spinnerCardProvider, spinnerCardValue;
    private Button btnAddCard;
    private ListView lvCardList;
    private List<Card> cardList;
    private CardAdapter cardAdapter;
    private String currentUserId; // Thêm biến để lưu ID người dùng hiện tại
    private FirebaseAuth mAuth; // Đối tượng Firebase Authentication
    private DatabaseReference userRef; // Tham chiếu đến bảng dữ liệu người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_card);

        etCardSerial = findViewById(R.id.etCardSerial);
        etCardPin = findViewById(R.id.etCardPin);
        spinnerCardProvider = findViewById(R.id.spinnerCardProvider);
        spinnerCardValue = findViewById(R.id.spinnerCardValue);
        btnAddCard = findViewById(R.id.btnAddCard);
        lvCardList = findViewById(R.id.lvCardList);

        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(this, cardList);
        lvCardList.setAdapter(cardAdapter);

        String[] cardProviders = {"Viettel", "Mobifone", "Vinaphone"};
        String[] cardValues = {"10000", "20000", "50000", "100000"};

        ArrayAdapter<String> cardProviderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardProviders);
        ArrayAdapter<String> cardValueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardValues);

        cardProviderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardValueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCardProvider.setAdapter(cardProviderAdapter);
        spinnerCardValue.setAdapter(cardValueAdapter);
        // Khởi tạo Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo tham chiếu đến bảng dữ liệu người dùng
        userRef = FirebaseDatabase.getInstance().getReference("user");

        // Các dòng mã khác ở onCreate không thay đổi

        // Tải dữ liệu thẻ từ Firebase và thêm vào danh sách cardList
        loadCardDataFromFirebase();

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ giao diện
                String cardSerial = etCardSerial.getText().toString().trim();
                String cardPin = etCardPin.getText().toString().trim();
                String cardProvider = spinnerCardProvider.getSelectedItem().toString();
                String cardValue = spinnerCardValue.getSelectedItem().toString();
                String time = getCurrentTime(); // Lấy thời gian hiện tại
                String userId = mAuth.getUid(); // Lấy ID người dùng hiện tại từ Firebase Authentication

                // Lấy thông tin người dùng từ Realtime Database
                userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String username = dataSnapshot.child("username").getValue(String.class);

                            // Tạo đối tượng Card
                            Card card = new Card(cardSerial, cardPin, cardProvider, cardValue, time, username, userId);

                            // Thêm đối tượng Card vào Realtime Database
                            DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("cards");
                            String cardId = cardRef.push().getKey(); // Tạo ID mới cho thẻ
                            cardRef.child(cardId).setValue(card);

                            // Xóa thông tin trên giao diện
                            etCardSerial.setText("");
                            etCardPin.setText("");

                            // Cập nhật lại danh sách thẻ (nếu cần)
                            cardList.add(card);
                            cardAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xử lý lỗi nếu cần
                    }
                });
            }
        });

    }
    // Hàm lấy thời gian hiện tại dưới dạng chuỗi
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }
    private void loadCardDataFromFirebase() {
        // Lấy tham chiếu đến bảng dữ liệu thẻ từ Firebase
        DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("cards");

        // Lấy ID người dùng hiện tại
        String currentUserId = mAuth.getUid();

        // Tạo truy vấn để chỉ lấy dữ liệu thẻ của người dùng hiện tại
        Query query = cardRef.orderByChild("userId").equalTo(currentUserId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardList.clear();

                for (DataSnapshot cardSnapshot : dataSnapshot.getChildren()) {
                    Card card = cardSnapshot.getValue(Card.class);
                    cardList.add(card);
                }

                // Cập nhật danh sách thẻ trên giao diện
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

}


