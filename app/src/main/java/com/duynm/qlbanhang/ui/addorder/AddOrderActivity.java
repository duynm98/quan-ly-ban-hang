package com.duynm.qlbanhang.ui.addorder;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.adapter.DetailOrderAdapter;
import com.duynm.qlbanhang.adapter.ProductAdapter;
import com.duynm.qlbanhang.base.BaseActivity;
import com.duynm.qlbanhang.data.order.Order;
import com.duynm.qlbanhang.data.order.OrderController;
import com.duynm.qlbanhang.data.order.OrderDetail;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;
import com.duynm.qlbanhang.data.product.ProductNavigator;
import com.duynm.qlbanhang.dialog.PickProductDialogFragment;
import com.duynm.qlbanhang.utils.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AddOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, ivAddProduct;
    private EditText edtCustomer, edtAddress, edtPhone;
    private Button btnCreateOrder;
    private TextView tvAddProduct;
    private RecyclerView rvOrderProducts;

    private OrderController orderController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        orderController = new OrderController(this);

        initViewComponent();
        initUI();
    }

    private void initViewComponent() {
        ivBack = findViewById(R.id.iv_back);
        ivAddProduct = findViewById(R.id.iv_add_product);
        edtCustomer = findViewById(R.id.edt_customer);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        btnCreateOrder = findViewById(R.id.btn_create_order);
        tvAddProduct = findViewById(R.id.tv_add_product);
        rvOrderProducts = findViewById(R.id.rv_order_products);

        ivBack.setOnClickListener(this);
        ivAddProduct.setOnClickListener(this);
        tvAddProduct.setOnClickListener(this);
        btnCreateOrder.setOnClickListener(this);
    }

    private void initUI() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(this, new ArrayList<OrderDetail>(), false);

        rvOrderProducts.setAdapter(detailOrderAdapter);
    }

    private void showPickProductDialog() {
//        final PickProductDialogFragment dialogFragment = new PickProductDialogFragment();
//        dialogFragment.setOnItemClickListener(new OnItemClickListener<Product>() {
//            @Override
//            public void onItemClick(Product item) {
//                DetailOrderAdapter detailOrderAdapter = (DetailOrderAdapter) rvOrderProducts.getAdapter();
//                if (detailOrderAdapter != null) detailOrderAdapter.addProduct(item);
//                dialogFragment.dismiss();
//            }
//        });
//
//        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());

    }

    private void displayFilterDialog() {

        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogLayout = layoutInflater.inflate(R.layout.dialog_add_order_product, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogBuilder
                .setView(dialogLayout)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

        final RecyclerView rvAddProduct = dialogLayout.findViewById(R.id.rv_add_product);

        ProductController productController = new ProductController(this, new ProductNavigator() {
            @Override
            public void displayProducts(ArrayList<Product> products) {
                ProductAdapter productAdapter = (ProductAdapter) rvAddProduct.getAdapter();
                if (productAdapter != null) productAdapter.replaceData(products);
            }
        });

        ProductAdapter productAdapter = new ProductAdapter(this, new ArrayList<Product>(), productController);
        productAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item) {
                DetailOrderAdapter detailOrderAdapter = (DetailOrderAdapter) rvOrderProducts.getAdapter();
                if (detailOrderAdapter != null) detailOrderAdapter.addProduct(item);
                alertDialog.dismiss();
            }
        });

        rvAddProduct.setAdapter(productAdapter);

        productController.getAllProducts();

//        new AlertDialog.Builder(this)
//                .setTitle(getString(R.string.filter))
//                .setView(dialogLayout)
//                .setCancelable(false)
//                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .create()
//                .show();
    }

    private void createNewOrder() {
        DetailOrderAdapter adapter = (DetailOrderAdapter) rvOrderProducts.getAdapter();
        if (adapter == null) return;
        ArrayList<OrderDetail> orderDetails = adapter.getOrderDetails();
        if (orderDetails == null ||
                orderDetails.isEmpty() ||
                edtCustomer.getText().toString().isEmpty() ||
                edtAddress.getText().toString().isEmpty() ||
                edtPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
        } else {
            String customer = edtCustomer.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            Order order = new Order(
                    customer,
                    address,
                    phone,
                    orderDetails,
                    false,
                    Calendar.getInstance().getTime().getTime()
            );

            orderController.addOrder(order, null);

            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_add_product:
            case R.id.tv_add_product:
                displayFilterDialog();
                break;
            case R.id.btn_create_order:
                createNewOrder();
                break;

        }
    }
}
