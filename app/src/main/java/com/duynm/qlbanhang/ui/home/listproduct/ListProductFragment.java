package com.duynm.qlbanhang.ui.home.listproduct;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.adapter.ProductAdapter;
import com.duynm.qlbanhang.base.BaseFragment;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;
import com.duynm.qlbanhang.data.product.ProductNavigator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by DuyNM on 15/12/2019
 */
public class ListProductFragment extends BaseFragment implements ProductNavigator, View.OnClickListener {

    //View components
    private RecyclerView rvProducts;
    private FloatingActionButton fabAdd;
    private SearchView searchView;
    private Spinner spnSort;

    //Variables
    private ProductController productController;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_product;
    }

    @Override
    protected void initViewComponent() {
        rvProducts = getView().findViewById(R.id.rv_products);
        fabAdd = getView().findViewById(R.id.fab_add);
        searchView = getView().findViewById(R.id.searchView);
        spnSort = getView().findViewById(R.id.spn_sort);
    }

    @Override
    protected void initUI() {
        productController = new ProductController(getContext(), this);
        productController.getAllProducts();

        fakeData();
//        fakeData2();
        initRecyclerView();
        initFAB();
        initSearchView();
        initSpinner();
    }

    private void fakeData() {
        if (productController.getProductCount() == 0)
            for (int i = 0; i < 10; i++) {
                productController.addProduct(new Product(
                        "Sản phẩm" + i,
                        i * 1000
                ));
            }
    }

    private void fakeData2() {
        productController.addProduct(new Product("Chuột", 99000));
        productController.addProduct(new Product("Bàn phím", 99000));
        productController.addProduct(new Product("Màn hình", 99000));
        productController.addProduct(new Product("Kính cường lực", 99000));
        productController.addProduct(new Product("Cáp sạc", 99000));
        productController.addProduct(new Product("Tản nhiệt", 99000));
        productController.addProduct(new Product("Tai nghe", 99000));
        productController.addProduct(new Product("Chuột không dây", 99000));
        productController.addProduct(new Product("Chuột có dây", 99000));
    }

    private void initRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(getContext(), new ArrayList<Product>(), productController);

        rvProducts.setAdapter(productAdapter);
    }

    private void initFAB() {
        fabAdd.setOnClickListener(this);
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    productController.getAllProducts();
                else
                    productController.getProductsByKeyword(newText);
                return true;
            }
        });
    }

    private void initSpinner() {
        if (getActivity() == null) return;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSort.setAdapter(adapter);
        spnSort.setSelection(0);
        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        productController.setSortType(ProductController.SortType.NAME_INC);
                        break;
                    case 1:
                        productController.setSortType(ProductController.SortType.NAME_DEC);
                        break;
                    case 2:
                        productController.setSortType(ProductController.SortType.PRICE_INC);
                        break;
                    case 3:
                        productController.setSortType(ProductController.SortType.PRICE_DEC);
                        break;
                }
                if (searchView != null && !searchView.getQuery().toString().isEmpty()) {
                    productController.getProductsByKeyword(searchView.getQuery().toString().trim());
                } else {
                    productController.getAllProducts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void displayProducts(ArrayList<Product> products) {
        Log.d("__PRODUCT", products.size() + "");
        ProductAdapter productAdapter = (ProductAdapter) rvProducts.getAdapter();
        if (productAdapter != null) {
            productAdapter.replaceData(products);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                Toast.makeText(getContext(), "Add new", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
