package com.example.duantotnghiep.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Activity.ListUserActivity;
import com.example.duantotnghiep.Activity.LocationActivity;
import com.example.duantotnghiep.Activity.ManagerProductActivity;
import com.example.duantotnghiep.Activity.ManHinhChoActivity;

import com.example.duantotnghiep.Activity.ChangePassword_Activity;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.database.FireBaseType;
import com.example.duantotnghiep.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mReference;
    private FirebaseUser firebaseUser;
    private ImageView imgUser;
  private   CardView cvOut,cvOder,cvPayment,cvReview,cvTK,cvPromotion,cvQLUser,cvQLProduct,cvChangePass,cvAdddiachi;
    private TextView textViewName,textSDT,textViewEmail,textFixInfor;
    private ImageView dialog_AVT;
    private TextInputEditText edImg;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Picasso picasso = Picasso.get();
    public ProfileFragment() {

    }
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvOut = view.findViewById(R.id.cvOut);
        cvOder = view.findViewById(R.id.cvOder);
        cvPayment = view.findViewById(R.id.cvPayment);
        cvReview = view.findViewById(R.id.cvReView);
        cvTK = view.findViewById(R.id.cvTK);
        cvPromotion = view.findViewById(R.id.cvPromotion);
        cvQLUser = view.findViewById(R.id.cvQLUser);
        cvQLProduct = view.findViewById(R.id.cvQLProduct);
        cvChangePass = view.findViewById(R.id.cvChangePass);
        cvAdddiachi = view.findViewById(R.id.cvAdddiachi);

        imgUser = view.findViewById(R.id.imageViewAvatar);
        textViewName = view.findViewById(R.id.textViewName);
        textSDT = view.findViewById(R.id.textSDT);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textFixInfor = view.findViewById(R.id.textFixInfor);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username", "");
        String phone = sharedPreferences.getString("phone", "");
        String email = sharedPreferences.getString("email", "");
        String img = sharedPreferences.getString("img", "");


        if (name.isEmpty()) {
            setInfoProfile();
        } else {
            textViewName.setText(name);
            textSDT.setText(phone);
            textViewEmail.setText(email);
            if (img.equals("")) {
                imgUser.setImageResource(R.drawable.baseline_person_24);
            } else {
                picasso.load(img).into(imgUser);
            }
        }
        firebaseAuth = FirebaseAuth.getInstance();
        setRoleListUser();
        setInfoProfile();

        cvOder.setOnClickListener(this);
        cvOut.setOnClickListener(this);
        cvPromotion.setOnClickListener(this);
        cvPayment.setOnClickListener(this);
        cvChangePass.setOnClickListener(this);
        cvQLProduct.setOnClickListener(this);
        cvReview.setOnClickListener(this);
        cvTK.setOnClickListener(this);
        cvQLUser.setOnClickListener(this);
        cvAdddiachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
            }
        });
        textFixInfor.setOnClickListener(this);
    }
    private void setInfoProfile() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = mReference.child("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("username").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String img = snapshot.child("img").getValue(String.class);
                textViewName.setText(name);
                textViewEmail.setText(email);

                if (img.equals("")||img.isEmpty()) {
                    imgUser.setImageResource(R.drawable.baseline_person_24);
                } else {
                    picasso.load(img).into(imgUser);
                }
                if (getActivity() != null) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", name);
                    editor.putString("phone", phone);
                    editor.putString("email", email);
                    editor.putString("img", img);
                    editor.apply();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi", "onCancelled: " + error.getMessage());
            }
        });
    }
    private void showDialogOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_out_app, null);

        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        startActivity(new Intent(getContext(), ManHinhChoActivity.class));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private void showDialogFigProfile() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_fix_profile);

        TextInputEditText edUserName = dialog.findViewById(R.id.edUserName);
        TextInputEditText edPhone = dialog.findViewById(R.id.edPhone);
        TextInputEditText edAddress = dialog.findViewById(R.id.edAddress);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        dialog_AVT = dialog.findViewById(R.id.dialog_AVT);
        Button btnImg= dialog.findViewById(R.id.btnImg);
        edImg = dialog.findViewById(R.id.edImg);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        edUserName.setText(user.getUsername());
                        edPhone.setText(user.getPhone());
                        edAddress.setText(user.getAddress());
                        edImg.setText(user.getImg());

                        String imgUrl = user.getImg();
                        if (!TextUtils.isEmpty(imgUrl)) {
                            Picasso.get().load(imgUrl).into(dialog_AVT);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserName = edUserName.getText().toString().trim();
                String newPhone = edPhone.getText().toString().trim();
                String newAddress = edAddress.getText().toString().trim();
                String newImgUrl = edImg.getText().toString().trim();

                userRef.child("username").setValue(newUserName);
                userRef.child("phone").setValue(newPhone);
                userRef.child("address").setValue(newAddress);
                userRef.child("img").setValue(newImgUrl);

                if (!newImgUrl.isEmpty()) {
                    Picasso.get().load(newImgUrl).into(imgUser);
                }else if (dialog_AVT.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) dialog_AVT.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] imageData = baos.toByteArray();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(userId + ".jpg");
                    UploadTask uploadTask = storageRef.putBytes(imageData);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        userRef.child("img").setValue(imageUrl);
                                        Toast.makeText(getActivity(), "Thay đổi thông tin thành công", Toast.LENGTH_SHORT).show();
                                        replaceFragment(new ProfileFragment());
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "Lỗi khi tải lên ảnh", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                Toast.makeText(getActivity(), "Thay đổi thông tin thành công", Toast.LENGTH_SHORT).show();
                replaceFragment(new ProfileFragment());
                dialog.dismiss();
            }
            private void replaceFragment(ProfileFragment profileFragment) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, profileFragment);
                fragmentTransaction.commit();
            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(dialog_AVT);
            String imageUrl = imageUri.toString();
            edImg.setText(imageUrl);
        }
    }
    public void setRoleListUser(){
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAdmin = FireBaseType.isAdmin(dataSnapshot);
                if (isAdmin) {
                    cvQLUser.setVisibility(View.VISIBLE);
                    cvQLProduct.setVisibility(View.GONE);
                } else {
                    cvQLUser.setVisibility(View.GONE);
                    cvQLProduct.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        setInfoProfile();
    }
    @Override
    public void onClick(View view) {

            if (view.getId()==R.id.cvOut){
                showDialogOut();
                firebaseAuth.signOut();
            }else if (view.getId()==R.id.cvOder){

            }else if (view.getId()==R.id.cvPayment){

            }else if (view.getId()==R.id.cvPromotion){

            }else if (view.getId()==R.id.cvTK){

            }else if (view.getId()==R.id.cvQLUser){
                startActivity(new Intent(getContext(), ListUserActivity.class));
            }else if (view.getId()==R.id.cvQLProduct){
                startActivity(new Intent(getContext(),ManagerProductActivity.class));
            }else if (view.getId()==R.id.cvReView){

            }else if (view.getId()==R.id.cvChangePass){
                startActivity(new Intent(getContext(), ChangePassword_Activity.class));
            } else if (view.getId()==R.id.textFixInfor) {
                showDialogFigProfile();
            }
    }
}