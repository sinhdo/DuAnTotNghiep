package com.example.duantotnghiep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.duantotnghiep.databinding.ActivityMainBinding;
import com.example.duantotnghiep.fragment.CartFragment;
import com.example.duantotnghiep.fragment.HomeFragment;
import com.example.duantotnghiep.fragment.ProfileFragment;

import com.example.duantotnghiep.fragment.SearchProductFragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.cart) {
                replaceFragment(new CartFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (id==R.id.search) {
                replaceFragment(new SearchProductFragment());
            } else {
                replaceFragment(new HomeFragment());
            }
            return true;
        });
        requestPermissions();
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void requestPermissions() {

        TedPermission.Builder builderTed = TedPermission.create();
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Chú ý");
                builder.setMessage("Bạn cần cấp quyền thì mới sử dụng được ứng dụng");
                builder.setNegativeButton("Cấp quyến", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    builderTed.check();
                });
                builder.setPositiveButton("Thoát", (dialogInterface, i) -> System.exit(0));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        builderTed.setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                .check();
    }
}