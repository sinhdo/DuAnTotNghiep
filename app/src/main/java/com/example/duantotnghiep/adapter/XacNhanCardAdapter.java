package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.Card;
import com.example.duantotnghiep.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class XacNhanCardAdapter extends RecyclerView.Adapter<XacNhanCardAdapter.XacNhanCardViewHolder> {
    private List<Card> cardList;
    private Context context; // Thêm context
    private List<Card> pendingCards;
    private List<Card> successCards;


    public XacNhanCardAdapter(Context context, List<Card> cardList) {
        this.context = context;

        this.cardList = cardList;
        this.pendingCards = new ArrayList<>();
        this.successCards = new ArrayList<>();
    }

    @NonNull
    @Override
    public XacNhanCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcardnap_item, parent, false);
        return new XacNhanCardViewHolder(view);
    }

    // Trong XacNhanCardAdapter.java
    @Override
    public void onBindViewHolder(@NonNull XacNhanCardViewHolder holder, int position) {
        Card card = cardList.get(position);

        // Hiển thị thông tin thẻ lên giao diện
        holder.textCardSerial.setText("Mã thẻ: " + card.getCardSerial());
        holder.textCardPin.setText("Mã PIN: " + card.getCardPin());
        holder.textCardProvider.setText("Nhà cung cấp: " + card.getCardProvider());
        holder.textCardValue.setText("Giá trị thẻ: " + card.getCardValue());
        holder.textTime.setText("Thời gian: " + card.getTime());
        holder.textUsername.setText("Người dùng: " + card.getUsername());
        holder.textStatus.setText("Trạng thái: " + card.getStatus());

        // Xử lý sự kiện khi nút được nhấn
        // Xử lý sự kiện khi nút được nhấn
        holder.btnToggleStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.updateStatusAndCreditUser();
                notifyDataSetChanged();
            }
        });
        // Xử lý sự kiện khi nút "Chuyển đổi Trạng thái (Không thành công)" được nhấn
        holder.btnToggleFailedStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.updateFailedStatus();
                notifyDataSetChanged();
            }
        });


    }
    public void setCardLists(List<Card> pendingCards) {
        this.cardList.clear();
        this.cardList.addAll(pendingCards);
        notifyDataSetChanged();
    }



    // Phương thức hiển thị Toast
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class XacNhanCardViewHolder extends RecyclerView.ViewHolder {
        TextView textCardSerial;
        TextView textCardPin;
        TextView textCardProvider;
        TextView textCardValue;
        TextView textTime;
        TextView textUsername;
        TextView textStatus;
        Button btnToggleStatus;
        Button btnToggleFailedStatus;


        public XacNhanCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textCardSerial = itemView.findViewById(R.id.textCardSerial);
            textCardPin = itemView.findViewById(R.id.textCardPin);
            textCardProvider = itemView.findViewById(R.id.textCardProvider);
            textCardValue = itemView.findViewById(R.id.textCardValue);
            textTime = itemView.findViewById(R.id.textTime);
            textUsername = itemView.findViewById(R.id.textUsername);
            textStatus = itemView.findViewById(R.id.textStatus);
            btnToggleStatus = itemView.findViewById(R.id.btnToggleStatus);
            btnToggleFailedStatus = itemView.findViewById(R.id.btnToggleFailedStatus);

        }
    }
}
