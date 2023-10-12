package com.example.duantotnghiep.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseHelper {
    private DatabaseReference databaseReference;

    public FireBaseHelper(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getUsersRef(){
        return databaseReference.child("user");
    }


}
