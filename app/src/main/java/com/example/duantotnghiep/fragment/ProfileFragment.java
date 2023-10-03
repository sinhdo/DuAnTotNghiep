package com.example.duantotnghiep.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.duantotnghiep.Activity.SplashScreen_Activity;
import com.example.duantotnghiep.R;


public class ProfileFragment extends Fragment {
    CardView cvOut;

    public ProfileFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        cvOut = view.findViewById(R.id.cvOut);

        cvOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOut();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                Intent intent = new Intent(getActivity(), SplashScreen_Activity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}