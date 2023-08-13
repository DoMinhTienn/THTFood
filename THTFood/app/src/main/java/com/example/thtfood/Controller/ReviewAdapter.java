package com.example.thtfood.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thtfood.Model.Reviews;
import com.example.thtfood.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Reviews> reviews;

    public ReviewAdapter(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reviews review = reviews.get(position);

        holder.usernameTextView.setText(review.getUsername()); // Thay thế bằng dữ liệu người dùng thực tế
        holder.ratingBar.setRating(review.getRating());
        holder.commentTextView.setText(review.getComment()); // Thay thế bằng dữ liệu bình luận thực tế
        Glide.with(holder.userImageView.getContext())
                .load(review.getImage())
                .into(holder.userImageView);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView usernameTextView;
        RatingBar ratingBar;
        TextView commentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }
    }
}

