package com.example.duantotnghiep.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.ColorAdapter;
import com.example.duantotnghiep.adapter.OrderAdapter;
import com.example.duantotnghiep.adapter.ReviewAdapter;
import com.example.duantotnghiep.adapter.SizeAdapter;
import com.example.duantotnghiep.model.AddProductToCart;
import com.example.duantotnghiep.model.Order;
import com.example.duantotnghiep.model.Reviews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private TextView tvNameProduct,tvPriceProduct,tvDescriptionPro, tvQuantity, tvSold;
    private Button btnAddToCart,btnBuyProduct;
    private int productQuantity;
    private ColorAdapter selectedColorAdapter;
    private int num;
    private RecyclerView recyclerView;
    private ArrayList<Order> orderList = new ArrayList<>();
    private String idProduct;
    List<String> sizeList;
    ArrayList<Integer> colors;
    private Picasso picasso = Picasso.get();
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;
    private ReviewAdapter reviewAdapter;
    private List<Reviews> reviewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        AnhXa();
        reviewAdapter = new ReviewAdapter(this, reviewsList);
        hienthi();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewAdapter);
        loadDataFromFirebase();
    }
    private void hienthi() {
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("reviews");

        reviewsRef.orderByChild("productId").equalTo(idProduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewsList.clear();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Reviews review = reviewSnapshot.getValue(Reviews.class);
                    if (review != null) {
                        reviewsList.add(review);
                    }
                }
                Log.d("=====", "onDataChange: "+reviewsList+"    vs    "+idProduct);
                reviewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
    private void AnhXa(){
        imgProduct =findViewById(R.id.imgProduct);
        tvNameProduct =findViewById(R.id.tvNameProduct);
        tvPriceProduct =findViewById(R.id.tvPriceProduct);
        tvDescriptionPro =findViewById(R.id.tvDescriptionPro);
        tvQuantity =findViewById(R.id.tvQuantity);
        tvSold = findViewById(R.id.tvSold);
        btnAddToCart =findViewById(R.id.btnAddToCart);
        btnBuyProduct =findViewById(R.id.btnBuyProduct);
        idProduct = getIntent().getStringExtra("idPro");
        recyclerView = findViewById(R.id.rcv_review);
        btnBuyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = tvNameProduct.getText().toString();
                Double productPrice = Double.parseDouble(tvPriceProduct.getText().toString().replace(" VND", ""));
                String productImageUrl = imgProduct.getTag().toString();
                if (productImageUrl != null && !productImageUrl.isEmpty()) {
                    dialogToBuy(productName, productPrice, productImageUrl,0);
                } else {
                    Toast.makeText(OrderActivity.this, "Không tìm thấy hình ảnh sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = tvNameProduct.getText().toString();
                Double productPrice = Double.parseDouble(tvPriceProduct.getText().toString().replace(" VND", ""));
                String productImageUrl = imgProduct.getTag().toString();
                if (productImageUrl != null && !productImageUrl.isEmpty()) {
                    dialogToBuy(productName, productPrice, productImageUrl,1);
                } else {
                    Toast.makeText(OrderActivity.this, "Không tìm thấy hình ảnh sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadDataFromFirebase() {
        idProduct = getIntent().getStringExtra("idPro");
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("products").child(idProduct);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                Double price = snapshot.child("price").getValue(Double.class);
                String description = snapshot.child("description").getValue(String.class);
                Integer quantity = snapshot.child("quantity").getValue(Integer.class);
                Integer sold = snapshot.child("sold").getValue(Integer.class);
                if (quantity != null) {
                    productQuantity = quantity;
                }
                ArrayList<String> imgProductUrls = snapshot.child("imgProduct").getValue(new GenericTypeIndicator<ArrayList<String>>() {});

                tvNameProduct.setText(name);
                tvPriceProduct.setText(price + " VND");
                tvDescriptionPro.setText("Mô tả sản phẩm: " + description);
                if (quantity != null) {
                    tvQuantity.setText("Số lượng còn: " + quantity);
                }
                tvSold.setText("Đã bán: " + sold);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu từ Firebase
            }
        });
    }
    private void dialogToBuy(String productName, Double productPrice, String productImageUrl,int type) {
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
            if (num < productQuantity){
                num++;
                tvNum.setText(num+"");
            }else {
                Toast.makeText(OrderActivity.this, "Sản phẩm chỉ có " + productQuantity + " cái", Toast.LENGTH_SHORT).show();
            }
        });
        nameProduct.setText(productName);
        priceOrder.setText(productPrice + " VND");
        if (productImageUrl != null && !productImageUrl.isEmpty()) {
            picasso.load(productImageUrl).into(imgOrder);
        } else {
            imgOrder.setImageResource(R.drawable.baseline_shopping_cart_24);
            Toast.makeText(OrderActivity.this, "Không tìm thấy hình ảnh sản phẩm", Toast.LENGTH_SHORT).show();
        }

        if (type == 0) {
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedSize = sizeAdapter.getSelectedSize();
                    ArrayList<Integer> selectedColors = new ArrayList<>(selectedColorAdapter.getSelectedColorList());

                    if (selectedColors.isEmpty()) {
                        Toast.makeText(OrderActivity.this, "Vui lòng chọn ít nhất một màu sắc", Toast.LENGTH_SHORT).show();
                    } else if (selectedSize == null ) {
                        Toast.makeText(OrderActivity.this, "Vui lòng chọn kích cỡ", Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("Size", selectedSize);
                        bundle.putIntegerArrayList("Color", selectedColors);
                        bundle.putInt("Quantity", num);

                        Intent intent = new Intent(OrderActivity.this, orderDetailsActivity.class);
                        intent.putExtra("productData", bundle);
                        intent.putExtra("idPro", idProduct);
                        startActivity(intent);
                    }
                }
            });
        }else {
            btnBuy.setText("Thêm vào giỏ hàng");
            String id_user = firebaseUser.getUid();
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedColor = selectedColorAdapter.getSelectedColor();
                    if (selectedColor == null || sizeAdapter.getSelectedSize() == null) {
                        Toast.makeText(OrderActivity.this, "Vui lòng chọn đủ màu và kích cỡ", Toast.LENGTH_SHORT).show();
                    } else {
                        mReference = FirebaseDatabase.getInstance().getReference().child("cart");
                        String newKey = idProduct; // Sử dụng ID sản phẩm làm khóa
                        AddProductToCart product1 = new AddProductToCart(newKey, id_user, idProduct, productName, Integer.parseInt(selectedColor), sizeAdapter.getSelectedSize(), productImageUrl, num, productPrice);
                        mReference.child(id_user).child(newKey).setValue(product1);
                        Toast.makeText(OrderActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
        dialog.show();
    }
}