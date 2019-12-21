package com.duynm.qlbanhang.ui.home.listproduct;

import androidx.recyclerview.widget.RecyclerView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.adapter.ProductAdapter;
import com.duynm.qlbanhang.base.BaseFragment;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;

import java.util.ArrayList;

/**
 * Created by DuyNM on 15/12/2019
 */
public class ListProductFragment extends BaseFragment {

    //View components
    private RecyclerView rvProducts;

    //Variables
    private ProductController productController;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_product;
    }

    @Override
    protected void initViewComponent() {
        rvProducts = getView().findViewById(R.id.rv_products);
    }

    @Override
    protected void initUI() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(getContext(), new ArrayList<Product>());

        rvProducts.setAdapter(productAdapter);
    }
}
