package com.example.duantotnghiep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duantotnghiep.MainActivity;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.database.FireBaseUserHelper;
import com.example.duantotnghiep.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class RegTabFragment extends Fragment {
    private EditText txt_User_re, txt_pass_re, txt_phone_re, txt_Email_re, txt_Address_re, txt_repass_re;
    private Button btn_reg;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FireBaseUserHelper firebaseHelper = new FireBaseUserHelper();
    private DatabaseReference usersRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_tab_fragment, container, false);
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        txt_User_re = root.findViewById(R.id.txt_User_re);
        txt_pass_re = root.findViewById(R.id.txt_pass_re);
        txt_repass_re = root.findViewById(R.id.txt_repass_re);
        txt_phone_re = root.findViewById(R.id.txt_phone_re);
        txt_Email_re = root.findViewById(R.id.txt_Email_re);
        txt_Address_re = root.findViewById(R.id.txt_Address_re);
        btn_reg = root.findViewById(R.id.btn_reg);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_User_re.getText().toString();
                String pass = txt_pass_re.getText().toString();
                String repass = txt_repass_re.getText().toString();
                String phone = txt_phone_re.getText().toString();
                String email = txt_Email_re.getText().toString();
                String address = txt_Address_re.getText().toString();

                if (validateRegistration(username, phone, email, address, pass, repass)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseUser = firebaseAuth.getCurrentUser();
                                        String id = firebaseUser.getUid();
                                        User user = new User();
                                        user.setId(id);
                                        user.setUsername(username);
                                        user.setPassword(pass);
                                        user.setAddress(address);
                                        user.setEmail(email);
                                        user.setPhone(phone);
                                        user.setUser_type(false);
                                        user.setWallet("");
                                        user.setImg("");
                                        usersRef = firebaseHelper.getUsersRef();
                                        usersRef.child(id).setValue(user);
                                        Toast.makeText(getContext(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                    }else {
                                        Toast.makeText(getContext(), "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        return root;
    }

    private boolean validateRegistration(String username, String phone, String email, String address, String password, String repasss) {
        if (TextUtils.isEmpty(username)) {
            txt_User_re.setError("Vui lòng nhập tên đăng nhập");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            txt_pass_re.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        if (password.length() < 6) {
            txt_pass_re.setError("Vui lòng nhập mật khẩu lớn hơn 6 kí tự");
            return false;
        }
        if (TextUtils.isEmpty(repasss)) {
            txt_repass_re.setError("Vui lòng nhập lại mật khẩu ");
            return false;
        }
        if (!password.equals(repasss)) {
            txt_repass_re.setError("Mật khẩu không trùng");
            return false;
        }


        if (TextUtils.isEmpty(phone)) {
            txt_phone_re.setError("Vui lòng nhập số điện thoại");
            return false;
        }

        String phoneRegex = "^[0-9]{10}$";
        if (!phone.matches(phoneRegex)) {
            txt_phone_re.setError("Số điện thoại không hợp lệ");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            txt_Email_re.setError("Vui lòng nhập email");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            txt_Email_re.setError("Email không hợp lệ");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            txt_Address_re.setError("Vui lòng nhập địa chỉ");
            return false;
        }

        return true;
    }
}
