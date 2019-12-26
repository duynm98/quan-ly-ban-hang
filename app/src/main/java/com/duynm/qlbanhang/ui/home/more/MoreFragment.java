package com.duynm.qlbanhang.ui.home.more;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.base.BaseFragment;
import com.duynm.qlbanhang.ui.configstore.ConfigStoreActivity;
import com.duynm.qlbanhang.ui.login.LoginActivity;

/**
 * Created by DuyNM on 15/12/2019
 */
public class MoreFragment extends BaseFragment {

    private TextView tvLogout, tvConfigStore;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initUI() {
        tvLogout = getView().findViewById(R.id.tv_log_out);
        tvConfigStore = getView().findViewById(R.id.tv_config_store);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        tvConfigStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ConfigStoreActivity.class));
            }
        });
    }
}
