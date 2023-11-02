package com.example.duantotnghiep.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.OrderDetailsAdapter;
import com.example.duantotnghiep.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private Button btnOrder;
    private RecyclerView rcvOrderDetail;

    private TextView txtSubtotal, txtDelivery, txtTax, txtTotal;

    private ImageView imgChooseVoucher, imgChooseAddress, imgChoosePayment;

    private List<Product> productList;
    private OrderDetailsAdapter adapter;

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

        Bundle bundle = getIntent().getBundleExtra("productData");
        if (bundle != null) {
            // Lấy dữ liệu từ Bundle
            String productName = bundle.getString("productName");
            double productPrice = bundle.getDouble("productPrice");
            String productImageUrl = bundle.getString("productImageUrl");
            String selectedSize = bundle.getString("selectedSize");
            String selectedColor = bundle.getString("selectedColor");
            int selectedQuantity = bundle.getInt("selectedQuantity");

            // Tạo một đối tượng Product từ dữ liệu nhận được
            Product product = new Product(productName, productImageUrl, selectedColor, selectedQuantity, productPrice, selectedSize);

            productList = new ArrayList<>();
            productList.add(product);

            adapter = new OrderDetailsAdapter(productList);

            rcvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
            rcvOrderDetail.setAdapter(adapter);
        }else {
            productList = new ArrayList<>(); // Khởi tạo productList rỗng
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOrder();
            }
        });
    }

    private void showDiaLogVoucher(){

    }

    private void showDiaLogAddress(){

    }

    private void showDiaLogMethod(){

    }

    private void showDialogOrder() {
        ConstraintLayout successDialog = findViewById(R.id.successDialog);
        View view = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.success_dialog_order, successDialog);
        Button successDone = view.findViewById(R.id.btnDone);
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        successDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(OrderDetailsActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}