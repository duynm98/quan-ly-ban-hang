package com.duynm.qlbanhang.ui.configstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.base.BaseActivity;
import com.duynm.qlbanhang.utils.pref.PrefKeys;
import com.duynm.qlbanhang.utils.pref.PreferenceHelper;

public class ConfigStoreActivity extends BaseActivity implements View.OnClickListener {

    private Button btnSaveData;
    private EditText edtStoreName, edtStorePhone, edtStoreAddress;
    private ImageView ivBack;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_store);

        preferenceHelper = new PreferenceHelper(this, PrefKeys.PREF_NAME);

        initViewComponent();
        initUI();
    }

    private void initViewComponent() {
        btnSaveData = findViewById(R.id.btn_save_data);
        edtStoreName = findViewById(R.id.edt_store_name);
        edtStorePhone = findViewById(R.id.edt_store_phone_number);
        edtStoreAddress = findViewById(R.id.edt_store_address);
        ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(this);
        btnSaveData.setOnClickListener(this);
    }

    private void initUI() {
        String storeName = preferenceHelper.getStoreName();

        String storePhone = preferenceHelper.getStorePhone();
        String storeAddress = preferenceHelper.getStoreAddress();

        if (storeName != null && !storeName.isEmpty()) edtStoreName.setText(storeName);
        if (storePhone != null && !storePhone.isEmpty()) edtStorePhone.setText(storePhone);
        if (storeAddress != null && !storeAddress.isEmpty()) edtStoreAddress.setText(storeAddress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_save_data:
                String storeName = edtStoreName.getText().toString();
                if (storeName.isEmpty()) {
                    Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
                } else {
                    preferenceHelper.setStoreName(storeName.trim());
                }

                String storePhone = edtStorePhone.getText().toString();
                if (!storePhone.isEmpty()) preferenceHelper.setStorePhone(storePhone.trim());

                String storeAddress = edtStoreAddress.getText().toString();
                if (!storeAddress.isEmpty()) preferenceHelper.setStoreAddress(storeAddress.trim());

                finish();
                break;
        }
    }
}
