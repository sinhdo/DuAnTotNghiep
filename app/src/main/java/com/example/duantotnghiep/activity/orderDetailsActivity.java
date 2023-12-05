package com.example.duantotnghiep.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.MainActivity;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.DiscountAdapter;
import com.example.duantotnghiep.adapter.DiscountDetailsAdapter;
import com.example.duantotnghiep.adapter.OrderDetailsAdapter;
import com.example.duantotnghiep.fragment.CartFragment;
import com.example.duantotnghiep.fragment.ConfirmFragment;
import com.example.duantotnghiep.model.Discount;
import com.example.duantotnghiep.model.Order;
import com.example.duantotnghiep.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class orderDetailsActivity extends AppCompatActivity {
    private Button btnOrder;
    private RecyclerView rcvOrderDetail;
    private TextView txtSubtotal, txtDelivery, txtTax, txtTotal, txtAddress, txtPayment, txtPhone, txtMoney, txtVoucher;
    private LinearLayout linearLayouAddress, idlr, lrlVC;
    private EditText notes;
    private List<Product> productList;
    private List<Integer> colorList;
    private OrderDetailsAdapter adapter;
    private String idProduct;
    private DatabaseReference productRef, userRef, discountRef, buyerRef;
    private FirebaseUser firebaseUser;
    private int quantity = 0;
    private double delivery;
    private double tax;
    private double discountAmount;
    private double subtotal;
    private double discountedPrice;
    private double price;
    private List<Discount> discountList;
    private boolean isVoucherSelected = false;
    private Discount selectedDiscount;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private boolean finalPaid;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btnOrder = findViewById(R.id.btnOrder);
        rcvOrderDetail = findViewById(R.id.rcvOrder_dt);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtDelivery = findViewById(R.id.txtDelivery);
        txtTax = findViewById(R.id.txtTax);
        txtTotal = findViewById(R.id.txtTotal);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        txtPayment = findViewById(R.id.txtPayment);
        txtMoney = findViewById(R.id.txtMoney);
        txtVoucher = findViewById(R.id.txtVoucher);
        notes = findViewById(R.id.editTextMessageToSeller);
        linearLayouAddress = findViewById(R.id.lrlAddress);
        lrlVC = findViewById(R.id.lrlVC);
        idlr = findViewById(R.id.idlr);
        userRef = FirebaseDatabase.getInstance().getReference();
        poppuGetListPayment();

        Intent intent = getIntent();
        idProduct = intent.getStringExtra("idPro");
        discountRef = FirebaseDatabase.getInstance().getReference("discounts");
        productRef = FirebaseDatabase.getInstance().getReference().child("products").child(idProduct);
        String buyerID = firebaseUser.getUid();
        buyerRef = userRef.child("user").child(buyerID).child("wallet");
        buyerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double walletAmount = dataSnapshot.getValue(Double.class);
                    txtMoney.setText(String.format(walletAmount + " VND"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
        lrlVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogVoucher();
            }
        });
        linearLayouAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaLogAddress();
            }
        });
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String idseller = dataSnapshot.child("sellerId").getValue(String.class);
                    String productType = dataSnapshot.child("productType").getValue(String.class);
                    String categoryID = dataSnapshot.child("categoryID").getValue(String.class);
                    String brand = dataSnapshot.child("brand").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String reviewId = dataSnapshot.child("reviewId").getValue(String.class);
                    int sold = dataSnapshot.child("sold").getValue(Integer.class);
                    price = dataSnapshot.child("price").getValue(Double.class);
                    int currentQuantity = dataSnapshot.child("quantity").getValue(Integer.class);
                    List<String> imgProduct = dataSnapshot.child("imgProduct").getValue(new GenericTypeIndicator<List<String>>() {
                    });

                    Bundle bundle = getIntent().getBundleExtra("productData");
                    adapter = new OrderDetailsAdapter();

                    if (bundle != null) {
                        double discountAmount = bundle.getDouble("discountAmount", 0.0);

                        colorList = bundle.getIntegerArrayList("Color");
                        String size = bundle.getString("Size");
                        quantity = bundle.getInt("Quantity");
                        List<String> sizeList = new ArrayList<>();
                        sizeList.add(size);

                        Discount discount = new Discount(discountAmount);

                        Product product = new Product(idProduct, idseller, name,null, categoryID, brand,
                                description, imgProduct, colorList, sold, reviewId, quantity, price, Collections.singletonList(size), discount);

                        product.setSelectedQuantity(quantity);
                        productList = new ArrayList<>();
                        productList.add(product);

                        adapter.setProductList(productList);
                        adapter.setColorList(colorList);

                        rcvOrderDetail.setLayoutManager(new LinearLayoutManager(orderDetailsActivity.this));
                        rcvOrderDetail.setAdapter(adapter);

                        subtotal = price * quantity;
                        delivery = 0.0;
                        tax = 0.0;

                        String deliveryText = txtDelivery.getText().toString().replace("VND", "");
                        String taxText = txtTax.getText().toString().replace("VND", "");

                        if (!TextUtils.isEmpty(deliveryText)) {
                            delivery = Double.parseDouble(deliveryText);
                        }
                        if (!TextUtils.isEmpty(taxText)) {
                            tax = Double.parseDouble(taxText);
                        }
                        double total = subtotal + delivery + tax;
                        discountedPrice = total - discountAmount;

                        // Hiển thị thông tin đơn hàng
                        txtSubtotal.setText(String.format("%.0f VND", subtotal));
                        txtDelivery.setText(String.format("%.0f VND", delivery));
                        txtTax.setText(String.format("%.0f VND", tax));
                        txtTotal.setText(String.valueOf(discountedPrice));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("list_order");
                String newKey = orderRef.push().getKey();
                String idBuyer = firebaseUser.getUid();
                String date = getCurrentTime();

                if (txtAddress.getText().toString().equalsIgnoreCase("Enter your address")) {
                    Toast.makeText(orderDetailsActivity.this, "Vui lòng chọn địa chỉ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtPayment.getText().toString().equalsIgnoreCase("Payment methods")) {
                    Toast.makeText(orderDetailsActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }

                finalPaid = false;
                if (txtPayment.getText().toString().equalsIgnoreCase("Payment on delivery")) {
                    finalPaid = false;
                } else if (txtPayment.getText().toString().equalsIgnoreCase("Pay with wallet")) {
                    finalPaid = true;
                }
                if (finalPaid) {
                    double totalAmount = Double.parseDouble(txtTotal.getText().toString());
                    String buyerID = firebaseUser.getUid();
                    checkSufficientWalletAmount(buyerID, totalAmount, newKey, idBuyer, date);
                } else {
                    performNextSteps(newKey, idBuyer, date);
                }
            }
        });
    }
    private void showdialogVoucher() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_voucher);

        RecyclerView rcvVoucher = dialog.findViewById(R.id.rcvVoucher);
        rcvVoucher.setLayoutManager(new LinearLayoutManager(this));

        discountList = new ArrayList<>();
        DiscountDetailsAdapter Detailsadapter = new DiscountDetailsAdapter(discountList);
        rcvVoucher.setAdapter(Detailsadapter);
        discountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                discountList.clear();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Discount discount = snapshot.getValue(Discount.class);
                    if (currentUser != null && discount != null && currentUser.getUid().equals(discount.getSellerId())) {
                        discountList.add(discount);
                    }
                }
                Detailsadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(orderDetailsActivity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        Detailsadapter.setOnItemClickListener(new DiscountDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Discount discount) {
                if (discount != null) {
                    if (selectedDiscount != null && discount.getCode().equals(selectedDiscount.getCode())) {
                        // Hủy bỏ việc sử dụng voucher nếu voucher đã được chọn trước đó
                        selectedDiscount = null; // Đặt selectedDiscount về null
                        discountedPrice = price * quantity;
                        txtSubtotal.setText(String.format("%.0f VND", discountedPrice));
                        txtVoucher.setText("Chosse Voucher"); // Xóa nội dung voucher

                        Toast.makeText(orderDetailsActivity.this, "Đã hủy bỏ sử dụng voucher", Toast.LENGTH_SHORT).show();
                    } else {
                        // Sử dụng voucher
                        selectedDiscount = discount;
                        double discountPercentage = discount.getAmount() / 100.0;
                        double originalPrice = price * quantity;
                        discountedPrice = originalPrice - (originalPrice * discountPercentage);

                        txtSubtotal.setText(String.format("%.0f VND", discountedPrice));

                        double discountValue = originalPrice * discountPercentage;
                        String discountMessage = String.format("Sản phẩm đã được giảm: %.0f VND", discountValue);
                        Toast.makeText(orderDetailsActivity.this, discountMessage, Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putDouble("discountAmount", discount.getAmount());
                        Intent intent = new Intent();
                        intent.putExtra("productData", bundle);
                        setResult(Activity.RESULT_OK, intent);

                        // Hiển thị thông tin voucher
                        if (discount.getCode() != null) {
                            String voucherCode = discount.getCode();
                            double voucherAmount = discount.getAmount();
                            String voucherText = String.format("Mã voucher: %s\nDiscount: %.0f%%\nĐã giảm: %.0f VND", voucherCode, voucherAmount, discountValue);
                            txtVoucher.setText(voucherText);
                        } else {
                            Toast.makeText(orderDetailsActivity.this, "Mã voucher không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateTotal();
                    Intent intentT = new Intent();
                    intentT.putExtra("discountedPrice", discountedPrice);
                    intentT.putExtra("subtotal", subtotal);
                    setResult(Activity.RESULT_OK, intentT);
                }
                isVoucherSelected = true;
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (isVoucherSelected) {
                    txtSubtotal.setText(String.format("%.0f VND", discountedPrice));
                } else {
                    txtSubtotal.setText(String.format("%.0f VND", subtotal));
                }

                double total = Double.parseDouble(txtSubtotal.getText().toString()) + delivery + tax;
                txtTotal.setText(String.valueOf(total));
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                Toast.makeText(this, "Chưa chọn địa chỉ", Toast.LENGTH_SHORT).show();
                Log.d("GGGGGGG", "setLocation: ");
            } else {
                idlr.setVisibility(View.VISIBLE);
                String name = bundle.getString("nameLocation");
                Log.d("HHHHHHHHHHH", "setLocation: " + name);
                String phone = bundle.getString("phoneLocation");
                String location = bundle.getString("location");
                txtAddress.setText(location);
                txtPhone.setVisibility(View.VISIBLE);
                txtPhone.setText(phone);
            }
        }
    }
    private void updateTotal() {
        double subtotal = Double.parseDouble(txtSubtotal.getText().toString().replaceAll("[^\\d.]", ""));
        double total = subtotal + delivery + tax;
        txtTotal.setText(String.valueOf(total));
    }
    private void performNextSteps(String newKey, String idBuyer, String date) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        DatabaseReference discountRef = FirebaseDatabase.getInstance().getReference("discounts"); // Thêm tham chiếu đến danh sách mã voucher trong Firebase

        for (int i = 0; i < adapter.getItemCount(); i++) {
            Product product = adapter.getProduct(i);
            if (product != null) {
                List<Integer> listColor = product.getColor();
                int color = listColor.get(i);

                Bundle bundle = getIntent().getBundleExtra("productData");
                if (bundle != null) {
                    int receivedQuantity = bundle.getInt("Quantity");

                    productsRef.child(product.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int currentQuantity = dataSnapshot.child("quantity").getValue(Integer.class);
                            int remainingQuantity = currentQuantity - receivedQuantity;

                            if (remainingQuantity >= 0) {
                                productsRef.child(product.getId()).child("quantity").setValue(remainingQuantity);

                                Order order = new Order(newKey, idBuyer, product.getSellerId(), product.getId(), product.getName(),
                                        product.getImgProduct().get(0), color, Double.parseDouble(txtTotal.getText().toString()),
                                        date, txtAddress.getText().toString(), txtPhone.getText().toString(),
                                        receivedQuantity, notes.getText().toString(), finalPaid, "waiting");

                                On_Create_Bill(order);
                                Log.d("=====", "onClick: sl" + product.getSellerId() + " pr " + product.getId());
                                if (isVoucherSelected) {
                                    Log.d("OrderDetailsActivity", "isVoucherSelected: true");
                                    if (selectedDiscount != null && selectedDiscount.getId() != null) {
                                        Log.d("OrderDetailsActivity", "selectedDiscount ID: " + selectedDiscount.getId());
                                        discountRef.child(selectedDiscount.getId()).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Xóa thành công
                                                        selectedDiscount = null; // Đặt lại selectedDiscount về null
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(orderDetailsActivity.this, "Lỗi xóa voucher!!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Log.d("OrderDetailsActivity", "selectedDiscount is null or has no ID");
                                    }
                                }
                            } else {
                                Toast.makeText(orderDetailsActivity.this, "Số lượng sản phẩm không đủ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý khi có lỗi xảy ra trong quá trình truy vấn Firebase
                        }
                    });
                }
            }
        }
    }
    private void showDiaLogAddress() {
        Intent intent = new Intent(orderDetailsActivity.this, ShowListLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("key", "value");
        intent.putExtras(bundle);
        startActivityForResult(intent, Activity.RESULT_CANCELED);
    }
    private void On_Create_Bill(Order order) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_order");
        String id = order.getId();
        myRef.child(id).setValue(order, (error, ref) -> {
            if (error == null) {
                showDialogOrder();
            } else {
                Toast.makeText(orderDetailsActivity.this, "Lỗi khi lưu sản phẩm vào Realtime Database", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void poppuGetListPayment() {
        String[] listPayment = {"Payment on delivery", "Pay with wallet"};
        txtPayment.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(orderDetailsActivity.this, txtPayment);
            for (String address : listPayment) {
                popupMenu.getMenu().add(address);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String selectedAddress = item.getTitle().toString();
                    txtPayment.setText(selectedAddress);
                    return true;
                }
            });
            popupMenu.show();
        });
    }
    private void checkSufficientWalletAmount(String buyerID, double amount, String newKey, String idBuyer, String date) {
        DatabaseReference buyerRef = userRef.child("user").child(buyerID).child("wallet");
        buyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double walletAmount = snapshot.getValue(Double.class);
                boolean isWalletEnough = walletAmount >= amount;
                if (isWalletEnough) {
                    performNextSteps(newKey, idBuyer, date);
                } else {
                    Toast.makeText(orderDetailsActivity.this, "Số tiền trong ví không đủ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn Firebase
            }
        });
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
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    Intent intent = new Intent(orderDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }
}