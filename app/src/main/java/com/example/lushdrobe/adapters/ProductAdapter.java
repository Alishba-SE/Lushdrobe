package com.example.lushdrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onAddToCartClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.productBrand.setText(product.getBrand());

        if (product.getRating() > 0) {
            holder.productRating.setText(String.format("★ %.1f", product.getRating()));
        } else {
            holder.productRating.setVisibility(View.GONE);
        }

        // Load product image
        int imageResource = getImageResourceForProduct(product.getName());
        Glide.with(context)
                .load(imageResource)
                .placeholder(R.drawable.placeholder)
                .into(holder.productImage);

        // Set click listeners
        holder.itemView.setOnClickListener(v -> listener.onItemClick(product));
        holder.addToCartBtn.setOnClickListener(v -> listener.onAddToCartClick(product));
    }

    private int getImageResourceForProduct(String productName) {
        String name = productName.toLowerCase();

        // Expanded product categories
        if (name.contains("eyeshadow")) return R.drawable.eyeshadow;
        if (name.contains("mascara")) return R.drawable.mascara;
        if (name.contains("foundation")) return R.drawable.foundation;
        if (name.contains("lipstick")) return R.drawable.lipstick;
        if (name.contains("concealer")) return R.drawable.concealer;
        if (name.contains("blush")) return R.drawable.blush_pan;
        if (name.contains("highlighter")) return R.drawable.highlighter;
        if (name.contains("eyeliner")) return R.drawable.eyeliner;
        if (name.contains("lip gloss")) return R.drawable.lip_gloss;
        if (name.contains("setting spray")) return R.drawable.setting_spray;
        if (name.contains("primer")) return R.drawable.face_primer;
        if (name.contains("bronzer")) return R.drawable.bronzer;
        if (name.contains("eyebrow")) return R.drawable.eyebrow_pencil;
        if (name.contains("nail polish")) return R.drawable.nail_polish;
        if (name.contains("brush")) return R.drawable.makeup_brush;
        if (name.contains("sponge")) return R.drawable.beauty_sponge;
        if (name.contains("skincare")) return R.drawable.skincare_set;

        return R.drawable.placeholder;
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, addToCartBtn;
        TextView productName, productPrice, productBrand, productRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productBrand = itemView.findViewById(R.id.productBrand);
            productRating = itemView.findViewById(R.id.productRating);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }

    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    public void filterList(List<Product> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }
}