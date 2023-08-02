package com.example.thtfood.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.thtfood.Model.Product;
import com.example.thtfood.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<Product> products;
    private OnMenuItemDeleteListener listener; // Listener của activity

    public void setOnMenuItemDeleteListener(OnMenuItemDeleteListener listener) {
        this.listener = listener;
    }
    public interface OnMenuItemDeleteListener {
        void onMenuItemDelete(int position);
    }


    public MenuAdapter(List<Product> products) {
        this.products = products;
    }
    @NonNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card, parent, false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewMenu;
        private TextView textViewMenu, textViewPrice;
        private ImageButton imageButtonDeleteFood, imageButtonEditFood;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMenu = itemView.findViewById(R.id.ImageViewMenu);
            textViewMenu = itemView.findViewById(R.id.textViewMemnu);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageButtonDeleteFood = itemView.findViewById(R.id.imageButtonDeleteFood);
            imageButtonEditFood = itemView.findViewById(R.id.imageButtonEditFood);
            imageButtonDeleteFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onMenuItemDelete(position);
                    }
                }
            });
        }
        public void bind(Product productItem) {
            productItem.getName();
            // Đổ dữ liệu từ MenuItem vào các thành phần trong itemView
            Glide.with(itemView.getContext())
                    .load(productItem.getImage()) // Chuyển đường dẫn ảnh từ MenuItem vào đây
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))) // Tuỳ chỉnh góc bo tròn nếu cần
                    .into(imageViewMenu);
            // Thay thế bằng ảnh từ MenuItem (nếu có)
            textViewMenu.setText(productItem.getName());
            textViewPrice.setText(String.valueOf(productItem.getPrice()));
            // Thiết lập các sự kiện onClick cho imageButton (nếu có)
        }
    }
}
