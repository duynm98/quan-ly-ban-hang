package com.duynm.qlbanhang.ui.detailorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.adapter.DetailOrderAdapter;
import com.duynm.qlbanhang.base.BaseActivity;
import com.duynm.qlbanhang.data.order.Order;
import com.duynm.qlbanhang.data.order.OrderController;
import com.duynm.qlbanhang.data.order.OrderDetail;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;

import java.util.ArrayList;

public class DetailOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, ivPaid;
    private TextView tvCustomer, tvAddress, tvPhone, tvSumPrice;
    private RecyclerView rvOrderProducts;
    private Button btnChangeOrderStatus, btnDeleteOrder;

    private OrderController orderController;

    private int orderID;
    private Order order;
    private ArrayList<OrderDetail> orderDetails;
    private boolean isPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        orderController = new OrderController(this);

        orderID = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderID < 0) {
            finish();
        }
        order = orderController.getOrderByID(orderID);
        isPaid = order.isPaid();
        orderDetails = order.getListProduct();

        orderController = new OrderController(this);

        initViewComponent();
        initUI();
    }

    private void initViewComponent() {
        ivBack = findViewById(R.id.iv_back);
        ivPaid = findViewById(R.id.iv_paid);
        tvCustomer = findViewById(R.id.tv_customer);
        tvPhone = findViewById(R.id.tv_phone_number);
        tvAddress = findViewById(R.id.tv_address);
        rvOrderProducts = findViewById(R.id.rv_order_products);
        btnChangeOrderStatus = findViewById(R.id.btn_change_order_status);
        btnDeleteOrder = findViewById(R.id.btn_delete_order);
        tvSumPrice = findViewById(R.id.tv_sum_price);

        ivBack.setOnClickListener(this);
        btnChangeOrderStatus.setOnClickListener(this);
        btnDeleteOrder.setOnClickListener(this);
    }

    private void initUI() {
        tvCustomer.setText(order.getCustomer());
        tvPhone.setText(order.getPhone());
        tvAddress.setText(order.getAddress());

        bindPaid();

        initRecyclerView();
        initSumPrice();
    }

    private void initRecyclerView() {
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(this, orderDetails, true);

        rvOrderProducts.setAdapter(detailOrderAdapter);
    }

    private void initSumPrice() {
        ProductController productController = new ProductController(this, null);
        double sumPrice = 0;
        for (OrderDetail orderDetail : orderDetails) {
            Product product = productController.selectProductByID(orderDetail.getProductID());
            sumPrice += product.getPrice() * orderDetail.getAmount();
        }
        tvSumPrice.setText(String.valueOf(sumPrice));
    }

    private void bindPaid() {
        ivPaid.setVisibility(isPaid ? View.VISIBLE : View.GONE);
        btnChangeOrderStatus.setText(getString(isPaid ? R.string.mark_unpaid : R.string.mark_paid));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_change_order_status:
                new AlertDialog.Builder(this)
                        .setMessage(getString(isPaid ? R.string.mark_this_is_unpaid : R.string.mark_this_is_paid))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isPaid = !isPaid;
                                order.setPaid(isPaid);
                                orderController.updateOrder(order);
                                bindPaid();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.btn_delete_order:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.confirm))
                        .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_order))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                orderController.deleteOrder(order, null);
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
        }
    }
}
