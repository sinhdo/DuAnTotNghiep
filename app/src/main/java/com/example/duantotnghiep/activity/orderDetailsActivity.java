package com.example.duantotnghiep.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.duantotnghiep.R;

public class orderDetailsActivity extends AppCompatActivity {

private Button btnOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOrder();

            }
        });

    }
    private void showDialogOrder(){
        ConstraintLayout successDialog = findViewById(R.id.successDialog);
        View view = LayoutInflater.from(orderDetailsActivity.this).inflate(R.layout.success_dialog_order,successDialog);
        Button successDone = view.findViewById(R.id.btnDone);
        AlertDialog.Builder builder = new AlertDialog.Builder(orderDetailsActivity.this);
        builder.setView(view);
        final  AlertDialog alertDialog = builder.create();
        successDone.findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(orderDetailsActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        }
        alertDialog.show();
    }
}