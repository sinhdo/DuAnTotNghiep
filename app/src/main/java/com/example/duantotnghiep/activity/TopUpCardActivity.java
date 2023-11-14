package com.example.duantotnghiep.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.CardAdapter;
import com.example.duantotnghiep.model.Card;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TopUpCardActivity extends AppCompatActivity {

    private TextView moneyTextView;
    private EditText etCardSerial, etCardPin;
    private Spinner spinnerCardProvider, spinnerCardValue;
    private Button btnAddCard;
    private ListView lvCardList;
    private List<Card> cardList;
    private CardAdapter cardAdapter;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
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
        moneyTextView = findViewById(R.id.Money);

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

        mAuth = FirebaseAuth.getInstance();


        userRef = FirebaseDatabase.getInstance().getReference("user");
        currentUserId = mAuth.getUid();

        loadCardDataFromFirebase();

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardSerial = etCardSerial.getText().toString().trim();
                String cardPin = etCardPin.getText().toString().trim();
                String cardProvider = spinnerCardProvider.getSelectedItem().toString();
                String cardValue = spinnerCardValue.getSelectedItem().toString();
                String time = getCurrentTime();
                String userId = mAuth.getUid();

                userRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String username = dataSnapshot.child("username").getValue(String.class);
                            Number walletValue = dataSnapshot.child("wallet").getValue(Number.class);
                            if (walletValue != null) {
                                String walletValueString = String.valueOf(walletValue);
                                moneyTextView.setText(walletValueString);

                                Card card = new Card(cardSerial, cardPin, cardProvider, cardValue, time, username, currentUserId);

                                DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("cards");
                                String cardId = cardRef.push().getKey();
                                cardRef.child(cardId).setValue(card);

                                etCardSerial.setText("");
                                etCardPin.setText("");

                                cardList.add(card);
                                cardAdapter.notifyDataSetChanged();
                            }
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

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }
    private void loadCardDataFromFirebase() {
        DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("cards");

        cardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardList.clear();

                for (DataSnapshot cardSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> cardData = (HashMap<String, Object>) cardSnapshot.getValue();
                    String cardSerial = (String) cardData.get("cardSerial");
                    String cardPin = (String) cardData.get("cardPin");
                    String cardProvider = (String) cardData.get("cardProvider");
                    String cardValue = (String) cardData.get("cardValue");
                    String time = (String) cardData.get("time");
                    String username = (String) cardData.get("username");
                    String userId = (String) cardData.get("userId");

                    Card card = new Card(cardSerial, cardPin, cardProvider, cardValue, time, username, userId);
                    cardList.add(card);
                }
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}