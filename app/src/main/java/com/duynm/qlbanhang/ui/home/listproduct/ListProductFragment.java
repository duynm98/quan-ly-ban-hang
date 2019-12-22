package com.duynm.qlbanhang.ui.home.listproduct;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.duynm.qlbanhang.ui.addproduct.AddProductActivity;
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
    private ImageView ivFilter;

    //Variables
    private ProductController productController;

    private ArrayList<String> allTypes;

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
        ivFilter = getView().findViewById(R.id.iv_filter);

        fabAdd.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
    }

    @Override
    protected void initUI() {
        productController = new ProductController(getContext(), this);
        productController.getAllProducts();

//        fakeData();
//        fakeData2();
        initRecyclerView();
        initSearchView();
        initSpinner();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
        if (productController != null) productController.getAllProducts();
    }

    private void refreshListProduct() {
        if (searchView != null && !searchView.getQuery().toString().isEmpty()) {
            productController.getProductsByKeyword(searchView.getQuery().toString().trim());
        } else {
            productController.getAllProducts();
        }
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
                refreshListProduct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void displayFilterDialog() {
        if (getContext() == null) return;
        LayoutInflater layoutInflater = getLayoutInflater();
        View filterAlertLayout = layoutInflater.inflate(R.layout.layout_filter_dialog, null);

        final EditText edtPriceFrom = filterAlertLayout.findViewById(R.id.edt_price_from);
        final EditText edtPriceTo = filterAlertLayout.findViewById(R.id.edt_price_to);
        Spinner spnFilterType = filterAlertLayout.findViewById(R.id.spn_filter_type);

        allTypes = productController.selectAllType();
        if (allTypes == null) allTypes = new ArrayList();
        allTypes.add(0, getString(R.string.all));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, allTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilterType.setAdapter(adapter);
        spnFilterType.setSelection(0);
        spnFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    productController.setType("");
                } else {
                    productController.setType(allTypes.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                productController.setType("");
            }
        });

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.filter))
                .setView(filterAlertLayout)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.apply), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String minPriceText = edtPriceFrom.getText().toString().trim();
                        String maxPriceText = edtPriceTo.getText().toString().trim();
                        double minPrice = minPriceText.isEmpty() ? -1 : Double.parseDouble(minPriceText);
                        double maxPrice = maxPriceText.isEmpty() ? ProductController.MAX_PRICE : Double.parseDouble(maxPriceText);

                        if (minPrice > maxPrice) {
                            Toast.makeText(getContext(), getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
                        } else {
                            productController.setMinPrice(minPrice);
                            productController.setMaxPrice(maxPrice);
                            refreshListProduct();
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        productController.resetFilter();
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
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
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_filter:
                displayFilterDialog();
                break;
        }
    }
}
