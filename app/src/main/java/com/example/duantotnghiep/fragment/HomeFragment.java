package com.example.duantotnghiep.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.ProductAdapter;
import com.example.duantotnghiep.adapter.ProductHomeAdapter;
import com.example.duantotnghiep.adapter.SliderImageAdapter;
import com.example.duantotnghiep.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class HomeFragment extends Fragment {
    private TextView textViewName;
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;
    RecyclerView rvManager;

    ProductAdapter productAdapter;
    List<Product> productList;
    DatabaseReference databaseReference;

    ProductHomeAdapter productHomeAdapter;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();
        productList = new ArrayList<>();


        loadDataFromFirebase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Handler handler = new Handler();     
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewName = view.findViewById(R.id.txtName);
        RecyclerView recyclerView = view.findViewById(R.id.view1);
        RecyclerView recyclerView2 = view.findViewById(R.id.view2);

         productHomeAdapter = new ProductHomeAdapter(getContext(), productList);
        recyclerView.setAdapter(productHomeAdapter);
        recyclerView2.setAdapter(productHomeAdapter);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPaperSlider);

        viewPager2.setAdapter(new SliderImageAdapter(getContext()));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);

        
        Runnable sliderRunnable = new Runnable() {
            @Override
            public void run() {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        };

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(sliderRunnable);
                handler.postDelayed(sliderRunnable, 3000);
            }
        });

        return view;
    }
    private void loadDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

       
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               productList.clear(); 

               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   Product product = snapshot.getValue(Product.class);
                   productList.add(product);
               }

             
               productHomeAdapter.updateProductList(productList);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
              
           }
       });
   }
    private void setInfoProfile() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = mReference.child("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("username").getValue(String.class);
                textViewName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi", "onCancelled: " + error.getMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInfoProfile();
    }
}