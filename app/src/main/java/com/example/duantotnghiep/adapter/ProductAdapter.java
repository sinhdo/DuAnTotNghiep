package com.example.duantotnghiep.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    private List<Product> productList;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    Product product;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductAdapter.ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void updateProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
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
                showDialogEdit();
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


                    deleteProduct(productId,product.getImgProduct());
                }
            }
        });



    }




    private void showDialogEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.edit_dialog_product, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
