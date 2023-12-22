package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.databinding.CircleColorBinding;
import com.example.duantotnghiep.model.ColorProduct;

import java.util.ArrayList;
import java.util.List;

public class MutilpleColorAdapter extends RecyclerView.Adapter<MutilpleColorAdapter.ViewHolder> {
    private List<ColorProduct> colorProductList;
    private List<String> selectedSize = new ArrayList<>();

    public MutilpleColorAdapter() {
        this.colorProductList = new ArrayList<>();
    }
    public MutilpleColorAdapter(List<ColorProduct> colorProductList) {
        this.colorProductList = colorProductList;
    }

    public void setColorList(List<ColorProduct> colorProductList) {
        this.colorProductList = colorProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext(); // Get the Context
        CircleColorBinding binding = CircleColorBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding, context);
    }
    public void updateColor(int color) {
//        if (colorList.contains(color)) {
//
//            colorList.remove(colorList.get(color));
//        } else {
//
//            colorList.add(color);
//        }
        notifyDataSetChanged();
    }





    @Override

    public void onBindViewHolder(@NonNull MutilpleColorAdapter.ViewHolder holder, int position) {
        ColorProduct colorProduct = colorProductList.get(position);

        holder.bind(colorProduct);
    }


    @Override

    public int getItemCount() {
        return colorProductList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleColorBinding binding;
        private final ImageView colorImageView;
        private final Context context;
        public ViewHolder(CircleColorBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            colorImageView = binding.colorImageView1;
        }


        public void bind(ColorProduct colorProduct) {

            GradientDrawable circle = new GradientDrawable();
            circle.setShape(GradientDrawable.OVAL);
            circle.setColor(colorProduct.getColor());

            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{context.getResources().getDrawable(R.drawable.circle_background), circle});
            binding.price1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setPrice1(Integer.parseInt(editable.toString()));
                    }
                }
            });
            binding.quantity1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setPrice1(Integer.parseInt(editable.toString()));
                    }
                }
            });
            binding.price2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setPrice2(Integer.parseInt(editable.toString()));
                    }
                }
            });
            binding.quantity2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setQuantity2(Integer.parseInt(editable.toString()));
                    }
                }
            });
            binding.price3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setPrice3(Integer.parseInt(editable.toString()));
                    }
                }
            });
            binding.quantity3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setQuantity3(Integer.parseInt(editable.toString()));
                    }
                }
            });

            binding.price4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setPrice4(Integer.parseInt(editable.toString()));
                    }
                }
            });
            binding.quantity4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setQuantity4(Integer.parseInt(editable.toString()));
                    }
                }
            });

            binding.price5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setPrice5(Integer.parseInt(editable.toString()));
                    }
                }
            });
            binding.quantity5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        colorProductList.get(getAdapterPosition()).setQuantity5(Integer.parseInt(editable.toString()));
                    }
                }
            });

            binding.price1.setText(String.valueOf(colorProduct.getPrice1()));
            binding.price2.setText(String.valueOf(colorProduct.getPrice2()));
            binding.price3.setText(String.valueOf(colorProduct.getPrice3()));
            binding.price4.setText(String.valueOf(colorProduct.getPrice4()));
            binding.price5.setText(String.valueOf(colorProduct.getPrice5()));
            binding.quantity1.setText(String.valueOf(colorProduct.getQuantity1()));
            binding.quantity2.setText(String.valueOf(colorProduct.getQuantity2()));
            binding.quantity3.setText(String.valueOf(colorProduct.getQuantity3()));
            binding.quantity4.setText(String.valueOf(colorProduct.getQuantity4()));
            binding.quantity5.setText(String.valueOf(colorProduct.getQuantity5()));
            if (!selectedSize.isEmpty()) {
                binding.title1.setText(selectedSize.get(0));
                binding.title2.setText(selectedSize.get(1));
                binding.title3.setText(selectedSize.get(2));
                binding.title4.setText(selectedSize.get(3));
                binding.title5.setText(selectedSize.get(4));
            }
            colorImageView.setBackground(layerDrawable);
        }

    }
    public void updateSelectedColors(List<ColorProduct> selectedColorProducts, List<String> selectedSize) {
        if (selectedColorProducts != null) {
            colorProductList.clear();
            colorProductList.addAll(selectedColorProducts);
            this.selectedSize.clear();
            this.selectedSize.addAll(selectedSize);
        }
        notifyDataSetChanged();
    }
    public List<ColorProduct> getSelectedColors() {
        return colorProductList;
    }



}
