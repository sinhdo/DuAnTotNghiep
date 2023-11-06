package com.example.duantotnghiep.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.OrderDetailsAdapter;
import com.example.duantotnghiep.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class orderDetailsActivity extends AppCompatActivity {
    private Button btnOrder;
    private RecyclerView rcvOrderDetail;

    private TextView txtSubtotal, txtDelivery, txtTax, txtTotal;

    private ImageView imgChooseVoucher, imgChooseAddress, imgChoosePayment;

    private List<Product> productList;
    private List<Integer> colorList;
    private OrderDetailsAdapter adapter;

    private String idProduct;

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btnOrder = findViewById(R.id.btnOrder);
        rcvOrderDetail = findViewById(R.id.rcvOrder_dt);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtDelivery = findViewById(R.id.txtDelivery);
        txtTax = findViewById(R.id.txtTax);
        txtTotal = findViewById(R.id.txtTotal);
        imgChooseVoucher = findViewById(R.id.imgChooseVoucher);
        imgChooseAddress = findViewById(R.id.imgChooseAddress);
        imgChoosePayment = findViewById(R.id.imgChoosePayment);

        Intent intent = getIntent();
        idProduct = intent.getStringExtra("idPro");
        productRef = FirebaseDatabase.getInstance().getReference().child("products").child(idProduct);

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy thông tin sản phẩm từ dataSnapshot
                    String name = dataSnapshot.child("name").getValue(String.class);
                    double price = dataSnapshot.child("price").getValue(Double.class);
                    List<String> imgProduct = dataSnapshot.child("imgProduct").getValue(new GenericTypeIndicator<List<String>>() {});

                    Bundle bundle = getIntent().getBundleExtra("productData");
                    adapter = new OrderDetailsAdapter();

                    if (bundle != null) {

                        colorList = bundle.getIntegerArrayList("Color");
                        String size = bundle.getString("Size");
                        int quantity = bundle.getInt("Quantity");

                        // Tạo danh sách size với một phần tử duy nhất
                        List<String> sizeList = new ArrayList<>();
                        sizeList.add(size);

                        // Tạo một đối tượng Product từ dữ liệu nhận được
                        Product product = new Product(name, price, imgProduct, Collections.singletonList(size), colorList, quantity);

                        productList = new ArrayList<>();
                        productList.add(product);

                        adapter.setProductList(productList);
                        adapter.setColorList(colorList);

                        rcvOrderDetail.setLayoutManager(new LinearLayoutManager(orderDetailsActivity.this));
                        rcvOrderDetail.setAdapter(adapter);

                        double subtotal = price * quantity;
                        txtSubtotal.setText(String.format(subtotal + " VND"));

                        double delivery = 0.0;
                        double tax = 0.0;

                        String deliveryText = txtDelivery.getText().toString().replace("VND", "");
                        String taxText = txtTax.getText().toString().replace("VND", "");

                        if (!TextUtils.isEmpty(deliveryText)) {
                            delivery = Double.parseDouble(deliveryText);
                        }

                        if (!TextUtils.isEmpty(taxText)) {
                            tax = Double.parseDouble(taxText);
                        }

                        double total = subtotal + delivery + tax;
                        txtTotal.setText(String.format(total + " VND"));

                    } else {
                        productList = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOrder();
            }
        });

        imgChooseVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaLogVoucher();
            }
        });
        imgChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaLogAddress();
            }
        });
        imgChoosePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaLogMethod();
            }
        });
    }

    private void showDiaLogVoucher(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(orderDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_voucher, null);


        builder.setView(view);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDiaLogAddress(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(orderDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_address, null);


        builder.setView(view);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDiaLogMethod(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(orderDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_method, null);


        builder.setView(view);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogOrder() {
        ConstraintLayout successDialog = findViewById(R.id.successDialog);
        View view = LayoutInflater.from(orderDetailsActivity.this).inflate(R.layout.success_dialog_order, successDialog);
        Button successDone = view.findViewById(R.id.btnDone);
        AlertDialog.Builder builder = new AlertDialog.Builder(orderDetailsActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        successDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(orderDetailsActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}