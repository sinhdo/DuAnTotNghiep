package com.example.duantotnghiep.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duantotnghiep.MainActivity;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.database.FireBaseType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginTabFragment extends Fragment {
    EditText email, pass;
    TextView fg_Text;
    float v = 0;
    Button btn_Log;
    private FirebaseAuth firebaseAuth;
    private RegTabFragment regTabFragment;

    private TextInputLayout textInputLayoutUN;
    private TextInputLayout textInputLayoutPW;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
        email = root.findViewById(R.id.txt_Email_log);
        pass = root.findViewById(R.id.txt_pass_Log);
        fg_Text = root.findViewById(R.id.fg_pass_log);
        btn_Log = root.findViewById(R.id.btn_log);
        textInputLayoutUN = root.findViewById(R.id.com_google_android_material_textfield_TextInputLayoutUN);
        textInputLayoutPW = root.findViewById(R.id.com_google_android_material_textfield_TextInputLayoutPW);
        firebaseAuth = FirebaseAuth.getInstance();

        regTabFragment = new RegTabFragment();

        email.setTranslationX(800);
        pass.setTranslationX(800);
        fg_Text.setTranslationX(800);
        btn_Log.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        fg_Text.setAlpha(v);
        btn_Log.setAlpha(v);


        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        fg_Text.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btn_Log.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                email.requestFocus();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pass.requestFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                email.requestFocus();
                            }
                        }, 500);
                    }
                }, 900);
            }
        }, 1000);

        btn_Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                String txtemail  = email.getText().toString();
                String txtpassword = pass.getText().toString();

                if (validateInput(txtemail, txtpassword)) {
                    // Tạo Dialog loading
                    final Dialog loadingDialog = new Dialog(getContext());
                    loadingDialog.setContentView(R.layout.loading);
                    loadingDialog.setCancelable(false);
                    TextView txtLoading = loadingDialog.findViewById(R.id.txtLoading);
                    txtLoading.setText("Vui lòng đợi...");

                    // Hiển thị Dialog loading
                    loadingDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(txtemail, txtpassword)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String id = user.getUid();
                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    boolean isAdmin = FireBaseType.isAdmin(snapshot);
                                                    if (isAdmin) {
                                                        startActivity(new Intent(getContext(), MainActivity.class));
                                                    } else {
                                                        startActivity(new Intent(getContext(), MainActivity.class));
                                                    }

                                                    // Đóng Dialog loading
                                                    loadingDialog.dismiss();

                                                    Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Đóng Dialog loading
                                                loadingDialog.dismiss();

                                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Đóng Dialog loading
                                    loadingDialog.dismiss();

                                    Toast.makeText(getContext(), "Vui lòng kiểm tra email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        return root;
    }

    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            email.setError("Vui lòng nhập tên đăng nhập");
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!username.matches(emailRegex)) {
            email.setError("Email không hợp lệ");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            pass.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        return true;
    }


}