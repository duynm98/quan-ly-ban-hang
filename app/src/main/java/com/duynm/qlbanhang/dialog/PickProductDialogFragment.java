package com.duynm.qlbanhang.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.adapter.ProductAdapter;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;
import com.duynm.qlbanhang.data.product.ProductNavigator;
import com.duynm.qlbanhang.utils.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by DuyNM on 25/12/2019
 */
public class PickProductDialogFragment extends AppCompatDialogFragment implements ProductNavigator {

    private RecyclerView rvAddProduct;
    private ProductController productController;

    private OnItemClickListener<Product> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<Product> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return inflater.inflate(R.layout.dialog_add_order_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvAddProduct = getView().findViewById(R.id.rv_add_product);

        productController = new ProductController(getContext(), this);

        initUI();

        productController.getAllProducts();
    }

    private void initUI() {
        ProductAdapter productAdapter = new ProductAdapter(getContext(), new ArrayList<Product>(), productController);
        productAdapter.setOnItemClickListener(onItemClickListener);

        rvAddProduct.setAdapter(productAdapter);
    }

    @Override
    public void displayProducts(ArrayList<Product> products) {
        ProductAdapter productAdapter = (ProductAdapter) rvAddProduct.getAdapter();
        if (productAdapter != null) productAdapter.replaceData(products);
    }
}
