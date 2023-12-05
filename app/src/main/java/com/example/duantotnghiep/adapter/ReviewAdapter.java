package com.example.duantotnghiep.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.R;
import com.example.duantotnghiep.model.Reviews;
import com.example.duantotnghiep.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<Reviews> reviewList1 ;

    private List<String> userIds;




    public ReviewAdapter(Context context, List<Reviews> reviewList1) {
        this.context = context;
        this.reviewList1 = reviewList1;
    }


    // Các phương thức khác của adapter

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Reviews review = reviewList1.get(position);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user").child(review.getUserId());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String displayName = dataSnapshot.child("username").getValue(String.class);
                    // Hiển thị displayName lên TextView tvUserName
                    holder.tvUserName.setText(displayName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn dữ liệu từ Firebase
            }
        });

        holder.tvComment.setText(review.getComment());

    }




    @Override
    public int getItemCount() {
        if (reviewList1 != null) {
            return reviewList1.size(); // Kiểm tra xem danh sách có giá trị null hay không trước khi gọi size()
        } else {
            return 0; // Trả về 0 hoặc giá trị mặc định tùy thuộc vào logic của bạn
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.userNameTextView);
            tvComment = itemView.findViewById(R.id.commentTextView);
//            tv_noReviewsTextView = itemView.findViewById(R.id.noReviewsTextView)
        }
    }
}
