package com.example.duantotnghiep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.ProductAdapter;
import com.example.duantotnghiep.databinding.ActivityManagerProductBinding;
import com.example.duantotnghiep.fragment.AddProductFragment;

public class ManagerProductActivity extends AppCompatActivity {
ActivityManagerProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManagerProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.rvManager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.floatAddProduct.hide();
                } else {
                    binding.floatAddProduct.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        ProductAdapter productAdapter =new ProductAdapter(this);
        binding.rvManager.setLayoutManager(new LinearLayoutManager(this));

        binding.rvManager.setAdapter(productAdapter);
        binding.floatAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Tạo một fragment mới
                AddProductFragment addProductFragment = new AddProductFragment();

                // Bắt đầu giao dịch fragment
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Thay thế fragment hiện tại bằng fragment mới
                transaction.replace(R.id.layout, addProductFragment); // R.id.fragment_container là ID của container fragment trong layout của bạn

                // Hoàn thành giao dịch fragment
                transaction.commit();
            }
        });

    }
}