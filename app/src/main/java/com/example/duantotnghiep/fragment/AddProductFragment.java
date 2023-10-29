package com.example.duantotnghiep.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Activity.ManagerProductActivity;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.adapter.ColorAdapter;
import com.example.duantotnghiep.adapter.MutilpleColorAdapter;
import com.example.duantotnghiep.adapter.MutilpleImgAdapter;
import com.example.duantotnghiep.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddProductFragment extends Fragment {
    ImageView btnColor;
    private FirebaseAuth firebaseAuth;

    private List<Uri> selectedImageUris = new ArrayList<>();

    private List<Integer> selectedColors = new ArrayList<>();
    private MutilpleColorAdapter mAdapter = new MutilpleColorAdapter();
    private ImageView chooseImg;

    List<String> selectedSize;
    MutilpleImgAdapter adapter;
    private boolean isAddingProduct = false;

    private RecyclerView multipleImg;
    private static final int REQUEST_CODE_SELECT_IMAGES = 1;

    EditText edtTitle, edtPrice, edtQuantity, edtBrand, edtDes;
    Button addProduct;
    private Spinner sizeSpinner;
    Product product;
    private StorageReference storageReference;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.add_product_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        sizeSpinner = root.findViewById(R.id.spinnerSize);
        addProduct = root.findViewById(R.id.btnAddProduct);
        btnColor = root.findViewById(R.id.btnColor);
        multipleImg = root.findViewById(R.id.mutilpeImg);
        edtTitle = root.findViewById(R.id.titleProduct);
        edtPrice = root.findViewById(R.id.priceProductSeller);
        edtQuantity = root.findViewById(R.id.QuantityProduct);
        edtDes = root.findViewById(R.id.descriptionProduct);
        edtBrand = root.findViewById(R.id.BrandProduct);
        chooseImg = root.findViewById(R.id.chooseImg);
        multipleImg = root.findViewById(R.id.mutilpeImg);
        RecyclerView rvMutilpeColor = root.findViewById(R.id.rvMutilpeColor);
        rvMutilpeColor.setAdapter(mAdapter);


        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogColor();
            }
        });

        ((ManagerProductActivity) requireActivity()).hideFloatingActionButton();

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), REQUEST_CODE_SELECT_IMAGES);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        multipleImg.setLayoutManager(layoutManager);


        adapter = new MutilpleImgAdapter(getContext(), selectedImageUris);
        multipleImg.setAdapter(adapter);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAddingProduct) {
                    isAddingProduct = true;
                    saveProductToRealtimeDatabase();
                }
            }
        });

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);


        sizeAdapter.add("CLOTHING");
        sizeAdapter.add("FOOTWEAR");


        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedProductType = (String) sizeSpinner.getSelectedItem();
                selectedSize = new ArrayList<>();

                if ("CLOTHING".equals(selectedProductType)) {

                    selectedSize.addAll(Arrays.asList("XS", "S", "M", "L", "XL", "XXL"));
                } else if ("FOOTWEAR".equals(selectedProductType)) {

                    selectedSize.addAll(Arrays.asList("36", "37", "38", "39", "40", "41", "42", "43", "44"));
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ClipData clipData = data.getClipData();

                if (clipData != null) {

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        if (imageUri != null) {
                            selectedImageUris.add(imageUri);
                        }
                    }
                } else {

                    Uri imageUri = data.getData();
                    if (imageUri != null) {
                        selectedImageUris.add(imageUri);
                    }
                }


                if (adapter != null) {
                    adapter.setImageList(selectedImageUris);
                }
            }
        }
    }


    private void showDialogColor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());

        View view = layoutInflater.inflate(R.layout.dialog_color, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        GridLayout gridLayout = view.findViewById(R.id.grid);
        final List<Integer> selectedColors = new ArrayList<>();

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final ImageButton button = (ImageButton) gridLayout.getChildAt(i);
            final int color = ((ColorDrawable) button.getBackground()).getColor();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedColors.contains(color)) {
                        selectedColors.remove(selectedColors.indexOf(color));
                    } else {
                        selectedColors.add(color);
                    }
                    updateRecyclerView(view, selectedColors);
                }
            });
        }
        Button addButton = view.findViewById(R.id.b21);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.updateSelectedColors(selectedColors);
                alertDialog.dismiss();
            }
        });
    }
    private void updateRecyclerView(View view, List<Integer> selectedColors) {
        RecyclerView recyclerView = view.findViewById(R.id.rvChosseCL);
        ColorAdapter colorAdapter = new ColorAdapter(selectedColors);
        recyclerView.setAdapter(colorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void saveProductToRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("products");
        storageReference = FirebaseStorage.getInstance().getReference();

        String productId = productsRef.push().getKey();

        String Title = String.valueOf(edtTitle.getText());
        int Price = Integer.parseInt(String.valueOf(edtPrice.getText()));
        String Des = String.valueOf(edtDes.getText());
        String Brand = String.valueOf(edtBrand.getText());

        String selectedProductType = (String) sizeSpinner.getSelectedItem();
        int Quantity = Integer.parseInt(String.valueOf(edtQuantity.getText()));

        Product.ProductType productType = Product.ProductType.valueOf(selectedProductType);
        List<String> imageUrls = new ArrayList<>();
        List<String> imageUriStrings = new ArrayList<>();
        for (Uri imageUri : selectedImageUris) {
            String imageName = "product_images/" + productId + "/" + imageUri.getLastPathSegment();
            StorageReference imageRef = storageReference.child(imageName);
            UploadTask uploadTask = imageRef.putFile(imageUri);
            imageUriStrings.add(imageUri.toString());
            uploadTask.addOnSuccessListener(taskSnapshot -> {

                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    imageUrls.add(imageUrl);


                    if (imageUrls.size() == selectedImageUris.size()) {

                        String userId = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference userProductsRef = database.getReference("user").child(userId);

                        userProductsRef.child(productId).setValue(product);


                        product = new Product(
                                productId, userId, Title, productType,
                                "categoryID", Brand, Des, imageUrls, selectedColors, 1000, "ngon", Quantity, Price, selectedSize
                        );


                        productsRef.child(productId).setValue(product);
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack(); // Quay lại màn hình trước đó

                        ((ManagerProductActivity) requireActivity()).showFloatingActionButton();
                        isAddingProduct = false;
                    }
                });
            });

        }


    }


}
