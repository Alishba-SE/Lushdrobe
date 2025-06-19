package com.example.lushdrobe.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private OnTotalPriceChangeListener totalPriceListener;
    private OnItemRemoveListener removeListener;
    private OnQuantityChangeListener quantityListener;

    public interface OnTotalPriceChangeListener {
        void onTotalPriceChange(double total);
    }

    public interface OnItemRemoveListener {
        void onItemRemove(int cartItemId);
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange(int cartItemId, int quantity, double price);
    }

    public CartAdapter(Context context, List<CartItem> cartItems,
                       OnTotalPriceChangeListener totalPriceListener,
                       OnItemRemoveListener removeListener,
                       OnQuantityChangeListener quantityListener) {
        this.context = context;
        this.cartItems = cartItems;
        this.totalPriceListener = totalPriceListener;
        this.removeListener = removeListener;
        this.quantityListener = quantityListener;
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        // Safe image loading
        String imageName = cartItem.getProductImage();
        Log.d("CartAdapter", "Image name: " + imageName); // for debugging

        if (imageName != null && !imageName.trim().isEmpty()) {
            int imageRes = context.getResources().getIdentifier(
                    imageName, "drawable", context.getPackageName());

            if (imageRes != 0) {
                Glide.with(context)
                        .load(imageRes)
                        .placeholder(R.drawable.placeholder)
                        .into(holder.productImage);
            } else {
                // Drawable resource not found
                Glide.with(context)
                        .load(R.drawable.placeholder)
                        .into(holder.productImage);
            }
        } else {
            // imageName is null or empty
            Glide.with(context)
                    .load(R.drawable.placeholder)
                    .into(holder.productImage);
        }

        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText(String.format("$%.2f", cartItem.getProductPrice()));
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.itemTotal.setText(String.format("$%.2f", cartItem.getTotalPrice()));

        holder.increaseQuantity.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            quantityListener.onQuantityChange(cartItem.getId(), newQuantity, cartItem.getProductPrice());
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int newQuantity = cartItem.getQuantity() - 1;
                quantityListener.onQuantityChange(cartItem.getId(), newQuantity, cartItem.getProductPrice());
            }
        });

        holder.removeItem.setOnClickListener(v -> {
            removeListener.onItemRemove(cartItem.getId());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity, itemTotal;
        ImageView increaseQuantity, decreaseQuantity, removeItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            itemTotal = itemView.findViewById(R.id.itemTotal);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            removeItem = itemView.findViewById(R.id.removeItem);
        }
    }
}
