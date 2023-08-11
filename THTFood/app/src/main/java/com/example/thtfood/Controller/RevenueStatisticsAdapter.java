package com.example.thtfood.Controller;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thtfood.R;

import java.util.List;

public class RevenueStatisticsAdapter extends RecyclerView.Adapter<RevenueStatisticsAdapter.ViewHolder> {
    private List<String> statisticItems;
    private int selectedPosition;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Thêm setter cho clickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public RevenueStatisticsAdapter(List<String> statisticItems) {
        this.statisticItems = statisticItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String statisticItem = statisticItems.get(position);
        holder.textViewstatisticItem.setText(statisticItem);
        if (selectedPosition == position) {
            holder.textViewstatisticItem.setTextColor(Color.RED);
        } else {
            holder.textViewstatisticItem.setTextColor(Color.BLACK); // Màu mặc định khi không được chọn
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition(); // Lấy vị trí item đang được xử lý
                if (clickListener != null) {
                    clickListener.onItemClick(clickedPosition); // Truyền vị trí item được xử lý vào sự kiện
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return statisticItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewstatisticItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewstatisticItem = itemView.findViewById(R.id.textViewstatisticItem);
        }
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
}
