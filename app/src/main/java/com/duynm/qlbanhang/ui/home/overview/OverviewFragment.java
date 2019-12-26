package com.duynm.qlbanhang.ui.home.overview;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.base.BaseFragment;
import com.duynm.qlbanhang.ui.configstore.ConfigStoreActivity;
import com.duynm.qlbanhang.utils.pref.PrefKeys;
import com.duynm.qlbanhang.utils.pref.PreferenceHelper;

/**
 * Created by DuyNM on 15/12/2019
 */
public class OverviewFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout layoutStoreInfo, layoutAddStorePhone, layoutAddStoreAddress, btnConfigStore;
    private TextView tvStoreName, tvStorePhone, tvStoreAddress;
    private ImageView ivStorePhone, ivStoreAddress;

    private PreferenceHelper preferenceHelper;

    private String storeName, storePhone, storeAddress;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_overview;
    }

    @Override
    protected void initViewComponent() {
        layoutStoreInfo = getView().findViewById(R.id.layout_store_info);
        layoutAddStorePhone = getView().findViewById(R.id.layout_add_store_phone);
        layoutAddStoreAddress = getView().findViewById(R.id.layout_add_store_address);
        btnConfigStore = getView().findViewById(R.id.btn_config_store);
        tvStoreName = getView().findViewById(R.id.tv_store_name);
        tvStorePhone = getView().findViewById(R.id.tv_store_phone);
        tvStoreAddress = getView().findViewById(R.id.tv_store_address);
        ivStorePhone = getView().findViewById(R.id.iv_store_phone);
        ivStoreAddress = getView().findViewById(R.id.iv_store_address);

        btnConfigStore.setOnClickListener(this);
        layoutAddStorePhone.setOnClickListener(this);
        layoutAddStoreAddress.setOnClickListener(this);
    }

    @Override
    protected void initUI() {
        preferenceHelper = new PreferenceHelper(getContext(), PrefKeys.PREF_NAME);

        storeName = preferenceHelper.getStoreName();

        storePhone = preferenceHelper.getStorePhone();
        storeAddress = preferenceHelper.getStoreAddress();

        if (storeName == null || storeName.isEmpty()) {
            layoutStoreInfo.setVisibility(View.GONE);
            btnConfigStore.setVisibility(View.VISIBLE);
        } else {
            layoutStoreInfo.setVisibility(View.VISIBLE);
            btnConfigStore.setVisibility(View.GONE);
            tvStoreName.setText(storeName);
        }

        if (storePhone == null || storePhone.isEmpty()) {
            ivStorePhone.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add));
            tvStorePhone.setText(getString(R.string.add_phone_number));
        } else {
            ivStorePhone.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_phone));
            tvStorePhone.setText(storePhone);
        }

        if (storeAddress == null || storeAddress.isEmpty()) {
            ivStoreAddress.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add));
            tvStoreAddress.setText(getString(R.string.add_address));
        } else {
            ivStoreAddress.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mail));
            tvStoreAddress.setText(storeAddress);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_config_store:
                if (storeName == null || storeName.isEmpty())
                    startActivity(new Intent(getContext(), ConfigStoreActivity.class));
                break;
            case R.id.layout_add_store_phone:
                if (storePhone == null || storePhone.isEmpty())
                    startActivity(new Intent(getContext(), ConfigStoreActivity.class));
                break;
            case R.id.layout_add_store_address:
                if (storeAddress == null || storeAddress.isEmpty())
                    startActivity(new Intent(getContext(), ConfigStoreActivity.class));
                break;
        }
    }
}
