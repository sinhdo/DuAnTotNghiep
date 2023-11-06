package com.example.duantotnghiep.adapter;




import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.example.duantotnghiep.R;
import com.example.duantotnghiep.databinding.ItemProductBinding;
import com.example.duantotnghiep.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;



public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    Context context;

    List<String> sizes;
    List<Product> productList;

    List<Uri> selectedImageUris = new ArrayList<>();

    int YOUR_REQUEST_CODE = 1;

    DatabaseReference databaseReference;
    FirebaseStorage storage;
    MutilpleColorAdapter mAdapter = new MutilpleColorAdapter();


    Product product;
    private Product editingProduct;
    List<Uri> imageUris;


    ImageView chooseImgEdit, chooseColor;

    EditText editTitle, editPrice, editQuantity, editBrand, editDes;
    Button editProduct;

    RecyclerView rvMultipleColorEdit, rvMultipleImgEdit;
    private List<Integer> selectedColors = new ArrayList<>();

    Spinner sizeSpinnerEdit;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void updateProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        product = productList.get(position);
        holder.binding.tvProduct.setText(product.getName());
        holder.binding.priceProduct.setText(String.valueOf(product.getPrice()));
        holder.binding.quantity.setText("Số lượng " + product.getQuantity());
        holder.binding.sold.setText(String.valueOf(product.getSold()));
        if (product.getImgProduct() != null && !product.getImgProduct().isEmpty()) {
            Uri firstImageUri = Uri.parse(product.getImgProduct().get(0));
            Glide.with(context).load(firstImageUri).into(holder.binding.imageProduct);
        } else {
            holder.binding.imageProduct.setImageResource(R.drawable.tnf);
        }

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogEdit(productList.get(position));
            }
        });
        holder.binding.imvDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    product = productList.get(adapterPosition);
                    String productId = product.getId();
                    Log.d("ProductAdapter", "Deleting product: " + productId);

                    productList.remove(adapterPosition);
                    notifyDataSetChanged();

                    deleteProduct(productId, product.getImgProduct());
                }
            }
        });
    }


    private void showDialogEdit(Product productToEdit) {

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                // Tạo và hiển thị Dialog ở đây


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.edit_dialog_product, null);
                builder.setView(view);
                editProduct = view.findViewById(R.id.btnEditProduct);
                editTitle = view.findViewById(R.id.titleProductEdit);
                editDes = view.findViewById(R.id.descriptionProductEdit);
                editBrand = view.findViewById(R.id.BrandProductEdit);
                editPrice = view.findViewById(R.id.priceProductSellerEdit);
                editQuantity = view.findViewById(R.id.QuantityProductEdit);
                chooseColor = view.findViewById(R.id.btnColorEdit);
                chooseImgEdit = view.findViewById(R.id.chooseImgEdit);
                sizeSpinnerEdit = view.findViewById(R.id.spinnerSizeEdit);
                rvMultipleColorEdit = view.findViewById(R.id.rvMutilpeColorEdit);
                rvMultipleImgEdit = view.findViewById(R.id.mutilpeImgEdit);

                editTitle.setText(productToEdit.getName());
                editPrice.setText(String.valueOf(productToEdit.getPrice()));
                editDes.setText(productToEdit.getDescription());
                editBrand.setText(productToEdit.getBrand());
                editQuantity.setText(String.valueOf(productToEdit.getQuantity()));


                String[] productTypeValues = context.getResources().getStringArray(R.array.product_types); // Sử dụng tên tài nguyên thay thế cho các giá trị này


                String selectedProductType = productToEdit.getProductType().toString();


                int position = -1;
                for (int i = 0; i < productTypeValues.length; i++) {
                    if (productTypeValues[i].equals(selectedProductType)) {
                        position = i;
                        break;
                    }
                }


                if (position >= 0) {
                    sizeSpinnerEdit.setSelection(position);
                }


                List<Integer> selectedColors = productToEdit.getColor();
                if (selectedColors != null && !selectedColors.isEmpty()) {
                    mAdapter.updateSelectedColors(selectedColors);

                    rvMultipleColorEdit.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    rvMultipleColorEdit.setAdapter(mAdapter);
                } else {
                    Toast.makeText(context, "Danh sách màu không hợp lệ", Toast.LENGTH_SHORT).show();
                }


                List<String> imageUrls = productToEdit.getImgProduct();


                imageUris = new ArrayList<>();
                for (String imageUrl : imageUrls) {
                    imageUris.add(Uri.parse(imageUrl));
                }
                MutilpleImgAdapter imgAdapter = new MutilpleImgAdapter(context, imageUris);

                rvMultipleImgEdit.setAdapter(imgAdapter);


                chooseImgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                        openImagePicker();
                    }


                });
                chooseColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogColor();
                    }
                });


                AlertDialog alertDialog = builder.create();
                editProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String updatedTitle = editTitle.getText().toString();
                        String updatedDescription = editDes.getText().toString();
                        String updatedBrand = editBrand.getText().toString();
                        int updatedPrice = Integer.parseInt(editPrice.getText().toString());
                        int updatedQuantity = Integer.parseInt(editQuantity.getText().toString());
                        List<Integer> updatedColors = mAdapter.getSelectedColors();
                        String selectedSize = (String) sizeSpinnerEdit.getSelectedItem();


                        productToEdit.setName(updatedTitle);
                        productToEdit.setDescription(updatedDescription);
                        productToEdit.setBrand(updatedBrand);
                        productToEdit.setPrice(updatedPrice);
                        productToEdit.setQuantity(updatedQuantity);

                        productToEdit.setColor(updatedColors);

                        productToEdit.setProductType(Product.ProductType.valueOf(selectedSize));
                        String selectedProductType = (String) sizeSpinnerEdit.getSelectedItem();
                        List<String> sizes = new ArrayList<>();
                        if ("CLOTHING".equals(selectedProductType)) {
                            sizes.addAll(Arrays.asList("XS", "S", "M", "L", "XL", "XXL"));
                        } else if ("FOOTWEAR".equals(selectedProductType)) {
                            sizes.addAll(Arrays.asList("36", "37", "38", "39", "40", "41", "42", "43", "44"));
                        }

                        productToEdit.setSize(sizes);


                        saveProductChanges(productToEdit);
                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
            }
        }
    }
//    private void requestPermissions() {
//
//        TedPermission.Builder builderTed = TedPermission.create();
//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//
//            }
//            @Override
//            public void onPermissionDenied(List<String> deniedPermissions) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Chú ý");
//                builder.setMessage("Bạn cần cấp quyền thì mới sử dụng được ứng dụng");
//                builder.setNegativeButton("Cấp quyến", (dialogInterface, i) -> {
//                    dialogInterface.dismiss();
//                    builderTed.check();
//                });
//                builder.setPositiveButton("Thoát", (dialogInterface, i) -> System.exit(0));
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        };
//        builderTed.setPermissionListener(permissionlistener)
//                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//                .check();
//    }
    private void openImagePicker() {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
//        requestPermissions();

        TedImagePicker
                .with(context)

                .startMultiImage(uriList -> {

                    imageUris = new ArrayList<>(uriList);

                });


    }


    private void showDialogColor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_color, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        GridLayout gridLayout = view.findViewById(R.id.grid);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final ImageButton button = (ImageButton) gridLayout.getChildAt(i);
            final int color = ((ColorDrawable) button.getBackground()).getColor();


            if (mAdapter.getSelectedColors().contains(color)) {
                button.setSelected(true);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mAdapter.updateColor(color);

                    button.setSelected(!button.isSelected());
                }
            });
        }

        Button addButton = view.findViewById(R.id.b21);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }


    private void saveProductChanges(Product productToEdit) {
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        DatabaseReference productRef = databaseReference.child(productToEdit.getId());

        productRef.setValue(productToEdit).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Lỗi khi sửa thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deleteProduct(String productId, List<String> imageUrls) {
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        storage = FirebaseStorage.getInstance();

        databaseReference.child(productId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        for (String imageUrl : imageUrls) {
                            StorageReference imageRef = storage.getReferenceFromUrl(imageUrl);
                            imageRef.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }

                        Product productToRemove = null;
                        int positionToRemove = -1;
                        for (int i = 0; i < productList.size(); i++) {
                            if (productList.get(i).getId().equals(productId)) {
                                productToRemove = productList.get(i);
                                positionToRemove = i;
                                break;
                            }
                        }
                        if (productToRemove != null) {
                            productList.remove(positionToRemove);
                            notifyDataSetChanged();
                        }

                        if (productList.isEmpty()) {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
