package com.duynm.qlbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.data.order.OrderController;
import com.duynm.qlbanhang.data.order.OrderDetail;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;

import java.util.ArrayList;

/**
 * Created by DuyNM on 25/12/2019
 */
public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetail> orderDetails;
    private ProductController productController;
    private boolean viewOnlyMode;

    public DetailOrderAdapter(Context context, ArrayList<OrderDetail> orderDetails, boolean viewOnlyMode) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.viewOnlyMode = viewOnlyMode;

        this.productController = new ProductController(context, null);
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_product_ordred, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final OrderDetail orderDetail = orderDetails.get(position);
        Product product = productController.selectProductByID(orderDetail.getProductID());

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.valueOf(product.getPrice()));

        if (viewOnlyMode) {
            holder.edtProductAmount.setVisibility(View.GONE);
            holder.ivRemove.setVisibility(View.GONE);
            holder.ivPlus.setVisibility(View.GONE);
            holder.ivMinus.setVisibility(View.GONE);
            holder.tvProductAmount.setVisibility(View.VISIBLE);

            holder.tvProductAmount.setText(String.valueOf(orderDetail.getAmount()));
        } else {
            holder.edtProductAmount.setText(String.valueOf(orderDetail.getAmount()));

            holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderDetails.remove(orderDetail);
                    notifyDataSetChanged();
                }
            });

            holder.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderDetail.setAmount(orderDetail.getAmount() + 1);
                    bindAmount(holder, orderDetail.getAmount());
                }
            });

            holder.ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderDetail.getAmount() > 0) {
                        orderDetail.setAmount(orderDetail.getAmount() - 1);
                        bindAmount(holder, orderDetail.getAmount());
                    }
                }
            });
        }
    }

    public void addProduct(Product product) {
        orderDetails.add(new OrderDetail(product.getId(), 1));
        notifyDataSetChanged();
    }

    private void bindAmount(ViewHolder holder, int amount) {
        holder.edtProductAmount.setText(String.valueOf(amount));
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        ImageView ivRemove = itemView.findViewById(R.id.iv_remove);
        ImageView ivPlus = itemView.findViewById(R.id.iv_plus);
        ImageView ivMinus = itemView.findViewById(R.id.iv_minus);
        TextView tvProductName = itemView.findViewById(R.id.tv_product_name);
        TextView tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        TextView tvProductAmount = itemView.findViewById(R.id.tv_product_amount);
        EditText edtProductAmount = itemView.findViewById(R.id.edt_product_amount);
    }
}
