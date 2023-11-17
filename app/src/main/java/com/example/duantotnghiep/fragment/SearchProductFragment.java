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
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private List<Product> list = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;
    private TextView textViewNull;
    private ProductHomeAdapter adapter;


    public SearchProductFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView=view.findViewById(R.id.svListProduct);
        recyclerView =view.findViewById(R.id.listProduct);
        textViewNull = view.findViewById(R.id.noResultsTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter = new ProductHomeAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null ||newText.isEmpty()){
                    textViewNull.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    SearchProduct(newText);
                }
                return true;
            }
        });
    }
    private void SearchProduct(String query) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("products");

        Query searchQuery = myRef.orderByChild("name")
                .startAt(query)
                .endAt(query + "\uf8ff");

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null && !product.getSellerId().equals(firebaseUser.getUid())) {
                        list.add(product);
                    }
                }
                adapter.setData(list);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()) {
                    textViewNull.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    textViewNull.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

}