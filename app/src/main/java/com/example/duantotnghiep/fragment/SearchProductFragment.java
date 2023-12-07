package com.example.duantotnghiep.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.ProductAdapter;
import com.example.duantotnghiep.adapter.ProductHomeAdapter;
import com.example.duantotnghiep.adapter.ProductHomeAdapter2;
import com.example.duantotnghiep.adapter.UserAdapter;
import com.example.duantotnghiep.model.Product;
import com.example.duantotnghiep.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchProductFragment extends Fragment {
    private View view;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private List<Product> list = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;
    private TextView textViewNull;
    ProductHomeAdapter2 productHomeAdapter2;

    public SearchProductFragment() {
        // Required empty public constructor
    }

    public static SearchProductFragment newInstance() {
        SearchProductFragment fragment = new SearchProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_search_product, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.svListProduct);
        recyclerView = view.findViewById(R.id.listProduct);
        textViewNull = view.findViewById(R.id.noResultsTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productHomeAdapter2 = new ProductHomeAdapter2(getContext(), list);
        recyclerView.setAdapter(productHomeAdapter2);

        loadAllProducts(); // Hiển thị tất cả các sản phẩm khi màn hình được chạy

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.isEmpty()) {
                    loadAllProducts(); // Hiển thị tất cả các sản phẩm nếu không có từ khóa tìm kiếm
                } else {
                    searchProducts(newText); // Tìm kiếm và cập nhật danh sách sản phẩm theo từ khóa
                }
                return true;
            }
        });
    }

    private void loadAllProducts() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null && !product.getSellerId().equals(firebaseUser.getUid())) {
                        productList.add(product);
                    }
                }
                updateRecyclerView(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void searchProducts(String query) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("products");

        Query searchQuery = myRef.orderByChild("name")
                .startAt(query)
                .endAt(query + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null && !product.getSellerId().equals(firebaseUser.getUid())) {
                        productList.add(product);
                    }
                }
                updateRecyclerView(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void updateRecyclerView(List<Product> productList) {
        list.clear();
        list.addAll(productList);
        productHomeAdapter2.updateProductList(productList);

        if (productList.isEmpty()) {
            textViewNull.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNull.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}