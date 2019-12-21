package com.duynm.qlbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;
import com.duynm.qlbanhang.ui.detailproduct.DetailProductActivity;

import java.util.ArrayList;

/**
 * Created by DuyNM on 16/12/2019
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private ProductController productController;

    public ProductAdapter(Context context, ArrayList<Product> products, ProductController productController) {
        this.context = context;
        this.products = products;
        this.productController = productController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = products.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(context.getResources().getString(R.string.product_price, product.getPrice()));
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(product.getName())
                        .setMessage(context.getString(R.string.are_you_sure_you_want_to_delete_this_product))
                        .setCancelable(true)
                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                productController.deleteProduct(product);
                            }
                        })
                        .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        byte[] bitmapData = product.getImage();
        if (bitmapData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            holder.ivProduct.setImageBitmap(bitmap);
        } else {
            holder.ivProduct.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.product));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailProductActivity.class);

                intent.putExtra("PRODUCT_ID", product.getId());

                context.startActivity(intent);
            }
        });
    }

    public void replaceData(ArrayList<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView tvProductName = itemView.findViewById(R.id.tv_product_name);
        TextView tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        ImageView ivDelete = itemView.findViewById(R.id.iv_delete);
        ImageView ivEdit = itemView.findViewById(R.id.iv_edit);
        ImageView ivProduct = itemView.findViewById(R.id.iv_product);
    }
}
