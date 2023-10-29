package com.example.duantotnghiep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.Location;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPhone;
    private EditText etLocation;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        etUsername = findViewById(R.id.et_username);
        etPhone = findViewById(R.id.et_phone);
        etLocation = findViewById(R.id.et_location);
        btnSubmit = findViewById(R.id.btn_submit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userReference = databaseReference.child("user").child(userId);

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String phoneNumber = dataSnapshot.child("phone").getValue(String.class);

                    etUsername.setText(username);
                    etPhone.setText(phoneNumber);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Firebasezzz", "Failed to read value.", databaseError.toException());
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etUsername.getText().toString();
                    String phone = etPhone.getText().toString();
                    String location = etLocation.getText().toString();

                    Location locationObj = new Location(userId, name, phone, location);

                    // Add the location to the user's location list
                    DatabaseReference locationListReference = userReference.child("location");
                    locationListReference.push().setValue(locationObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LocationActivity.this, "Location added successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LocationActivity.this, "Failed to add location!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}


