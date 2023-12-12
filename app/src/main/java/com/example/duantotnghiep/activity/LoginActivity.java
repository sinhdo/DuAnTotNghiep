package com.example.duantotnghiep.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.LoginAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
TabLayout tabLayout;
ViewPager viewPager;
FloatingActionButton gg,fb,ins;
float v=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);
//        fb = findViewById(R.id.fab_facebook);
//        gg = findViewById(R.id.fab_google);
//        ins = findViewById(R.id.fab_ins);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Register"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // Lấy vị trí của tab được chọn
        int position = tab.getPosition();

        // Chuyển đến trang tương ứng dựa trên vị trí của tab
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});
        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        fb.setTranslationY(300);
//        gg.setTranslationY(300);
//        ins.setTranslationY(300);
//        tabLayout.setTranslationY(300);
//
//        fb.setAlpha(v);
//        gg.setAlpha(v);
//        ins.setAlpha(v);
//        tabLayout.setAlpha(v);
//
//        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//        gg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
//        ins.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
//        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
    }
}