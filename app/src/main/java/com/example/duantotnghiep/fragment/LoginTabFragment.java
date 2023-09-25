package com.example.duantotnghiep.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duantotnghiep.R;

public class LoginTabFragment extends Fragment {
    EditText user,pass;
    TextView fg_Text;
    float v =0;
    Button btn_Log;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
user = root.findViewById(R.id.txt_Name_log);
pass = root.findViewById(R.id.txt_pass_Log);
fg_Text = root.findViewById(R.id.fg_pass_log);
btn_Log = root.findViewById(R.id.btn_log);

        user.setTranslationX(800);
        pass.setTranslationX(800);
        fg_Text.setTranslationX(800);
        btn_Log.setTranslationX(800);

        user.setAlpha(v);
        pass.setAlpha(v);
        fg_Text.setAlpha(v);
        btn_Log.setAlpha(v);


        user.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        fg_Text.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btn_Log.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        return root;
    }
}
