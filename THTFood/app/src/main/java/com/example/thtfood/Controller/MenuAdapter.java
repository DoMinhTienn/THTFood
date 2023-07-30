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
import com.example.thtfood.Model.Menu;
import com.example.thtfood.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<Menu> menuItems;

    public MenuAdapter(List<Menu> menuItems) {
        this.menuItems = menuItems;
    }
    @NonNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card, parent, false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
        Menu menuItem = menuItems.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewMenu;
        private TextView textViewMenu, textViewPrice;
        private ImageButton imageButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMenu = itemView.findViewById(R.id.ImageViewMenu);
            textViewMenu = itemView.findViewById(R.id.textViewMemnu);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageButton = itemView.findViewById(R.id.imageButton);
        }
        public void bind(Menu menuItem) {
            menuItem.getName();
            // Đổ dữ liệu từ MenuItem vào các thành phần trong itemView
            Glide.with(itemView.getContext())
                    .load(menuItem.getImage()) // Chuyển đường dẫn ảnh từ MenuItem vào đây
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))) // Tuỳ chỉnh góc bo tròn nếu cần
                    .into(imageViewMenu);
            // Thay thế bằng ảnh từ MenuItem (nếu có)
            textViewMenu.setText(menuItem.getName());
            textViewPrice.setText(String.valueOf(menuItem.getPrice()));
            // Thiết lập các sự kiện onClick cho imageButton (nếu có)
        }
    }
}
