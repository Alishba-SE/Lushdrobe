package com.example.lushdrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.Order;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderHistoryAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderId.setText(String.format(Locale.getDefault(), "Order #%d", order.getId()));
        holder.orderDate.setText(order.getDate());
        holder.orderTotal.setText(NumberFormat.getCurrencyInstance().format(order.getTotalPrice()));
        holder.orderStatus.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderTotal, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }
}