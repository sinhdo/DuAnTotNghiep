package com.example.duantotnghiep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OderActivity extends AppCompatActivity {
    private ImageView imgProduct;
    private TextView tvNameProduct,tvPriceProduct,tvDescriptionPro;
    private Button btnAddToCart,btnBuyProduct;
    private int num;
    private Picasso picasso = Picasso.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder);
        AnhXa();

    }
    private void AnhXa(){
        imgProduct =findViewById(R.id.imgProduct);
        tvNameProduct =findViewById(R.id.tvNameProduct);
        tvPriceProduct =findViewById(R.id.tvPriceProduct);
        tvDescriptionPro =findViewById(R.id.tvDescriptionPro);
        btnAddToCart =findViewById(R.id.btnAddToCart);
        btnBuyProduct =findViewById(R.id.btnBuyProduct);
        String idProduct = getIntent().getStringExtra("idPro");
        btnBuyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogToBuy();
            }
        });
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("products").child(idProduct);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                Double price = snapshot.child("price").getValue(Double.class);
                String description = snapshot.child("description").getValue(String.class);
                tvNameProduct.setText(name);
                tvPriceProduct.setText("$  "+price);
                tvDescriptionPro.setText(description);
                Object imgObj = snapshot.child("imgProduct").getValue();
                if (imgObj instanceof ArrayList) {
                    ArrayList<String> imgList = (ArrayList<String>) imgObj;
                    if (!imgList.isEmpty()) {
                        String imgUrl = imgList.get(0);
                        Picasso.get().load(imgUrl).into(imgProduct);
                    }
                } else if (imgObj instanceof String) {
                    String imgUrl = (String) imgObj;
                    Picasso.get().load(imgUrl).into(imgProduct);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        

    }
    private void dialogToBuy() {
        Dialog dialog = new Dialog(OderActivity.this);
        dialog.setContentView(R.layout.dialog_order);
        dialog.getWindow().setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.bg_dialog));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;

        ImageView imgOrder = dialog.findViewById(R.id.imgOrder);
        TextView priceOrder = dialog.findViewById(R.id.priceOrder);
        TextView QuantityProduct = dialog.findViewById(R.id.QuantityProduct);
        ImageView  imgMinus = (ImageView) dialog.findViewById(R.id.img_minus);
        ImageView  imgPlus = (ImageView) dialog.findViewById(R.id.img_plus);
        TextView tvNum = (TextView) dialog.findViewById(R.id.tv_num);

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

        dialog.show();
    }

}