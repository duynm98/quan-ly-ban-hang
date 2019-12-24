package com.duynm.qlbanhang.ui.home.listorder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.adapter.OrderAdapter;
import com.duynm.qlbanhang.base.BaseFragment;
import com.duynm.qlbanhang.data.order.Order;
import com.duynm.qlbanhang.data.order.OrderController;
import com.duynm.qlbanhang.ui.addorder.AddOrderActivity;

import java.util.ArrayList;

/**
 * Created by DuyNM on 15/12/2019
 */
public class ListOrderFragment extends BaseFragment implements View.OnClickListener, OrderController.OnDataListener {

    private ImageView ivBack;

    private RecyclerView rvOrders;
    private LinearLayout btnNewOrder;

    private OrderController orderController;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_order;
    }

    @Override
    protected void initViewComponent() {
        rvOrders = getView().findViewById(R.id.rv_orders);
        btnNewOrder = getView().findViewById(R.id.btn_new_order);

        btnNewOrder.setOnClickListener(this);
    }

    @Override
    protected void initUI() {
        initRecyclerView();

        orderController = new OrderController(getContext());
        orderController.getAllOrder(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (orderController != null) {
            orderController.getAllOrder(this);
        }
    }

    private void initRecyclerView() {
        OrderAdapter orderAdapter = new OrderAdapter(getContext(), new ArrayList<Order>());

        rvOrders.setAdapter(orderAdapter);
    }

    @Override
    public void onData(ArrayList<Order> listOrder) {
        OrderAdapter orderAdapter = (OrderAdapter) rvOrders.getAdapter();
        if (orderAdapter != null) {
            orderAdapter.replaceData(listOrder);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new_order:
                startActivity(new Intent(getContext(), AddOrderActivity.class));
                break;
        }
    }
}
