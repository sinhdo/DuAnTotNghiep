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
import java.util.List;

public class TopUpCardActivity extends AppCompatActivity {

    private EditText etCardSerial, etCardPin;
    private Spinner spinnerCardProvider, spinnerCardValue;
    private Button btnAddCard;
    private ListView lvCardList;
    private List<Card> cardList;
    private CardAdapter cardAdapter; // Sử dụng CardAdapter thay vì ArrayAdapter

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

        // Khởi tạo danh sách thẻ đã thêm và adapter
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(this, cardList); // Sử dụng CardAdapter
        lvCardList.setAdapter(cardAdapter); // Sử dụng CardAdapter

        // Khởi tạo danh sách lựa chọn cho hãng thẻ và mệnh giá
        String[] cardProviders = {"Viettel", "Mobifone", "Vinaphone"};
        String[] cardValues = {"10000", "20000", "50000", "100000"};

        ArrayAdapter<String> cardProviderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardProviders);
        ArrayAdapter<String> cardValueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardValues);

        cardProviderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardValueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCardProvider.setAdapter(cardProviderAdapter);
        spinnerCardValue.setAdapter(cardValueAdapter);

        // Lấy UID của người dùng đang đăng nhập hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        // Đường dẫn thẻ của người dùng hiện tại trong Realtime Database
        DatabaseReference userCardsRef = FirebaseDatabase.getInstance().getReference("user_cards").child(userId);

        // Mã lắng nghe sự thay đổi dữ liệu thẻ
        userCardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardList.clear(); // Xóa danh sách thẻ hiện tại

                for (DataSnapshot cardSnapshot : dataSnapshot.getChildren()) {
                    // Lấy thông tin thẻ và thêm vào danh sách
                    Card card = cardSnapshot.getValue(Card.class);
                    cardList.add(card);
                }

                cardAdapter.notifyDataSetChanged(); // Cập nhật giao diện ListView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardSerial = etCardSerial.getText().toString();
                String cardPin = etCardPin.getText().toString();
                String cardProvider = spinnerCardProvider.getSelectedItem().toString();
                String cardValue = spinnerCardValue.getSelectedItem().toString();

                // Lấy thời gian hiện tại
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String currentTime = sdf.format(calendar.getTime());

                // Lấy tên người dùng và ID người dùng đang đăng nhập hiện tại
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid(); // Lấy ID của người dùng hiện tại
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("user").child(userId);

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String username = dataSnapshot.child("username").getValue(String.class);
                            int currentWallet = dataSnapshot.child("wallet").getValue(Integer.class); // Lấy số tiền hiện tại trong ví

                            // Tính toán số tiền mới sau khi nạp thẻ
                            int cardValueInt = Integer.parseInt(cardValue); // Đổi giá trị thẻ thành kiểu int
                            int newWallet = currentWallet + cardValueInt;

                            // Cập nhật số tiền trong ví của người dùng
                            usersRef.child("wallet").setValue(newWallet);

                            // Tạo đối tượng Card từ thông tin thẻ, thời gian, tên người dùng và ID người dùng
                            Card card = new Card(cardSerial, cardPin, cardProvider, cardValue, currentTime, username, userId);

                            // Thêm thẻ vào Realtime Database
                            DatabaseReference cardsRef = FirebaseDatabase.getInstance().getReference("cards");
                            String cardKey = cardsRef.push().getKey(); // Tạo một khóa duy nhất cho thẻ
                            cardsRef.child(cardKey).setValue(card);

                            // Thêm thẻ vào danh sách và cập nhật ListView
                            cardList.add(card);
                            cardAdapter.notifyDataSetChanged();
                        } else {
                            // Handle error: User data doesn't exist
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error: Read operation cancelled
                    }
                });
            }
        });
    }

}


