package com.example.duantotnghiep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.ManagerSellerAdapter;
import com.example.duantotnghiep.adapter.ProductAdapter;
import com.example.duantotnghiep.databinding.ActivityManagerProductBinding;
import com.example.duantotnghiep.fragment.AddProductFragment;
import com.google.android.material.tabs.TabLayout;

public class ManagerProductActivity extends AppCompatActivity {
    ActivityManagerProductBinding binding;
    private boolean isInAddProductFragment = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManagerProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tabLayoutSeller.addTab(binding.tabLayoutSeller.newTab().setText("Còn hàng"));
        binding.tabLayoutSeller.addTab(binding.tabLayoutSeller.newTab().setText("Hết hàng"));
        binding.tabLayoutSeller.setTabTextColors(Color.parseColor("#D3D3D3"), Color.parseColor("#E91E63"));
        binding.tabLayoutSeller.setSelectedTabIndicatorColor(Color.parseColor("#E91E63"));
        binding.tabLayoutSeller.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.tabLayoutSeller.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();


                binding.viewPaperSeller.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPaperSeller.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayoutSeller));
        binding.viewPaperSeller.setAdapter(new ManagerSellerAdapter(getSupportFragmentManager(), this, binding.tabLayoutSeller.getTabCount()));


        binding.floatAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFloatingActionButton(); // Ẩn FAB
                isInAddProductFragment = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                AddProductFragment addProductFragment = new AddProductFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.layout, addProductFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



    }

    public void hideFloatingActionButton() {
        binding.floatAddProduct.hide();
    }

    public void showFloatingActionButton() {
        binding.floatAddProduct.show();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        if (backStackEntryCount > 0) {

            fragmentManager.popBackStack();
            isInAddProductFragment = false;
            if (backStackEntryCount == 1) {
                showFloatingActionButton();
            }
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isInAddProductFragment) {
            showFloatingActionButton();
        }
    }



}