package com.duynm.qlbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.data.order.Order;
import com.duynm.qlbanhang.data.order.OrderController;
import com.duynm.qlbanhang.ui.detailorder.DetailOrderActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DuyNM on 24/12/2019
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Order> orders;
    private OrderController orderController;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.orderController = new OrderController(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Order order = orders.get(position);

        holder.tvCustomer.setText(order.getCustomer());
        holder.tvAddress.setText(order.getAddress());
        holder.tvPrice.setText(String.valueOf(orderController.getSumPrice(order)));

        bindStatus(holder, order.isPaid());

        Date date = new Date(order.getDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        String dateText = simpleDateFormat.format(date);
        holder.tvDate.setText(dateText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailOrderActivity.class);
                intent.putExtra("ORDER_ID", order.getId());
                context.startActivity(intent);
            }
        });

        holder.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean paid = order.isPaid();
                new AlertDialog.Builder(context)
                        .setMessage(context.getString(paid ? R.string.mark_this_is_unpaid : R.string.mark_this_is_paid))
                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                order.setPaid(!paid);
                                orderController.updateOrder(order);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void bindStatus(ViewHolder viewHolder, boolean paid) {
        viewHolder.ivStatus.setImageDrawable(ContextCompat.getDrawable(context, paid ? R.drawable.ic_tick : R.drawable.ic_no_tick));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void replaceData(ArrayList<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView tvCustomer = itemView.findViewById(R.id.tv_customer);
        TextView tvAddress = itemView.findViewById(R.id.tv_address);
        TextView tvPrice = itemView.findViewById(R.id.tv_price);
        TextView tvDate = itemView.findViewById(R.id.tv_date);
        ImageView ivStatus = itemView.findViewById(R.id.iv_status);
    }
}
