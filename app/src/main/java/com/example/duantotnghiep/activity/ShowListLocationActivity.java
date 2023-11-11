package com.example.duantotnghiep.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.duantotnghiep.MainActivity;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.ShowLocationAdapter;
import com.example.duantotnghiep.databinding.ActivityShowListLocationBinding;
import com.example.duantotnghiep.model.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowListLocationActivity extends AppCompatActivity implements ShowLocationAdapter.Callback {
    private FirebaseUser firebaseUser;
    private ActivityShowListLocationBinding binding;
    private ArrayList<Location> list = new ArrayList<>();
    private ShowLocationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowListLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rcvShowListLocation.setLayoutManager(new LinearLayoutManager(this));
        showList();
        binding.imgBack.setOnClickListener(view -> {
            finish();
        });
    }
    private void showList(){
        getListLocation();
        adapter = new ShowLocationAdapter(getApplicationContext(), list, this);
        binding.rcvShowListLocation.setAdapter(adapter);
    }
    private void getListLocation(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("user").child(id_user).child("location");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null){
                    list.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Location location = dataSnapshot.getValue(Location.class);
                    list.add(location);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("LISTCART", "onCancelled: " + error.getMessage());
            }
        });
    }

    @Override
    public void clickItem(Location location) {
        Intent intent = new Intent(ShowListLocationActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("nameLocation",location.getName());
        bundle.putString("phoneLocation",location.getPhone());
        bundle.putString("location",location.getLocation());

        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }
}