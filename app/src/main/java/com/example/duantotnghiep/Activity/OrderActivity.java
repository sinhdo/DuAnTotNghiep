package com.example.duantotnghiep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.R;

import com.example.duantotnghiep.adapter.ColorAdapter;
import com.example.duantotnghiep.adapter.SizeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private ImageView imgProduct;
    private TextView tvNameProduct,tvPriceProduct,tvDescriptionPro;
    private Button btnAddToCart,btnBuyProduct;

    private ColorAdapter selectedColorAdapter;
    private int num;

    private String idProduct;
    List<String> sizeList;
    ArrayList<Integer> colors;
    private Picasso picasso = Picasso.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AnhXa();
        loadDataFromFirebase();
    }
    private void AnhXa(){
        imgProduct =findViewById(R.id.imgProduct);
        tvNameProduct =findViewById(R.id.tvNameProduct);
        tvPriceProduct =findViewById(R.id.tvPriceProduct);
        tvDescriptionPro =findViewById(R.id.tvDescriptionPro);
        btnAddToCart =findViewById(R.id.btnAddToCart);
        btnBuyProduct =findViewById(R.id.btnBuyProduct);
        idProduct = getIntent().getStringExtra("idPro");

        btnBuyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = tvNameProduct.getText().toString();
                Double productPrice = Double.parseDouble(tvPriceProduct.getText().toString().replace("$ ", ""));
                String productImageUrl = imgProduct.getTag().toString();
                if (productImageUrl != null && !productImageUrl.isEmpty()) {
                    dialogToBuy(productName, productPrice, productImageUrl);
                } else {
                    // Xử lý khi không có URL hình ảnh
                    Toast.makeText(OrderActivity.this, "Không tìm thấy hình ảnh sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadDataFromFirebase() {
        idProduct = getIntent().getStringExtra("idPro");
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products").child(idProduct);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                Double price = snapshot.child("price").getValue(Double.class);
                String description = snapshot.child("description").getValue(String.class);
                ArrayList<String> imgProductUrls = snapshot.child("imgProduct").getValue(new GenericTypeIndicator<ArrayList<String>>() {});

                tvNameProduct.setText(name);
                tvPriceProduct.setText("$ " + price);
                tvDescriptionPro.setText(description);

                if (imgProductUrls != null && !imgProductUrls.isEmpty()) {
                    String imgUrl = imgProductUrls.get(0);
                    picasso.load(imgUrl).into(imgProduct);
                    imgProduct.setTag(imgUrl);
                }

                colors = new ArrayList<>();
                DataSnapshot colorsSnapshot = snapshot.child("color");
                for (DataSnapshot colorSnapshot : colorsSnapshot.getChildren()) {
                    Integer color = colorSnapshot.getValue(Integer.class);
                    if (color != null) {
                        colors.add(color);
                    }
                }
                DataSnapshot sizeSnapshot = snapshot.child("size");
                sizeList = new ArrayList<>();
                for (DataSnapshot sizeItemSnapshot : sizeSnapshot.getChildren()) {
                    String size = sizeItemSnapshot.getValue(String.class);
                    if (size != null) {
                        sizeList.add(size);
                    }
                }
                tvNameProduct.setText(name);
                tvPriceProduct.setText("$ " + price);
                tvDescriptionPro.setText(description);

                if (imgProductUrls != null && !imgProductUrls.isEmpty()) {
                    String imgUrl = imgProductUrls.get(0);
                    picasso.load(imgUrl).into(imgProduct);
                    imgProduct.setTag(imgUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu từ Firebase
            }
        });
    }
    private void dialogToBuy(String productName, Double productPrice, String productImageUrl) {
        Dialog dialog = new Dialog(OrderActivity.this);
        dialog.setContentView(R.layout.dialog_order);
        dialog.getWindow().setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.bg_dialog));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;

        ImageView imgOrder = dialog.findViewById(R.id.imgOrder);
        TextView priceOrder = dialog.findViewById(R.id.priceOrder);
        TextView nameProduct = dialog.findViewById(R.id.NameProduct);
        ImageView  imgMinus = (ImageView) dialog.findViewById(R.id.img_minus);
        ImageView  imgPlus = (ImageView) dialog.findViewById(R.id.img_plus);
        Button btnBuy = dialog.findViewById(R.id.btnBuy);
        TextView tvNum = (TextView) dialog.findViewById(R.id.tv_num);
        RecyclerView rvMutilpeColor = dialog.findViewById(R.id.rvOpsionColor);
        RecyclerView rvMutilpeSize = dialog.findViewById(R.id.rvOpsionSize);

        rvMutilpeColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ColorAdapter colorAdapter = new ColorAdapter(colors);
        rvMutilpeColor.setAdapter(colorAdapter);

        rvMutilpeSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SizeAdapter sizeAdapter = new SizeAdapter(sizeList);
        rvMutilpeSize.setAdapter(sizeAdapter);

        selectedColorAdapter = colorAdapter;

        num=1;
        imgMinus.setOnClickListener(view -> {
            if (num > 1){
                num--;
                tvNum.setText(num+"");

            }
        });
        imgPlus.setOnClickListener(view -> {
            if (num < 10){
                num++;
                tvNum.setText(num+"");

            }
        });
        nameProduct.setText(productName);
        priceOrder.setText("$ " + productPrice);
        if (productImageUrl != null && !productImageUrl.isEmpty()) {
            picasso.load(productImageUrl).into(imgOrder);
        } else {
            imgOrder.setImageResource(R.drawable.baseline_shopping_cart_24);
            Toast.makeText(OrderActivity.this, "Không tìm thấy hình ảnh sản phẩm", Toast.LENGTH_SHORT).show();
        }
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Size", sizeAdapter.getSelectedSize());
                ArrayList<Integer> selectedColors = new ArrayList<>(selectedColorAdapter.getSelectedColorList());
                bundle.putIntegerArrayList("Color", selectedColors);
                bundle.putInt("Quantity", num);

                Intent intent = new Intent(OrderActivity.this, OrderDetailsActivity.class);
                intent.putExtra("productData", bundle);
                intent.putExtra("idPro", idProduct);
                startActivity(intent);
            }
        });
        dialog.show();
    }
}