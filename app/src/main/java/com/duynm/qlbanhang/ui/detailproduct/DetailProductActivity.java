package com.duynm.qlbanhang.ui.detailproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.base.BaseActivity;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;

public class DetailProductActivity extends BaseActivity {

    private ImageView ivBack, imgDetailProduct;
    private TextView txtDetailName, txtDetailPrice, txtDetailType, txtDetailDescription;

    private int productID = -1;

    private ProductController productController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        productID = getIntent().getIntExtra("PRODUCT_ID", -1);
        productController = new ProductController(this, null);

        if (productID < 0) {
            finish();
        }

        initViewComponent();
        initUI();
    }

    private void initViewComponent() {
        ivBack = findViewById(R.id.iv_back);
        imgDetailProduct = findViewById(R.id.imgDetailProduct);
        txtDetailName = findViewById(R.id.txtDetailName);
        txtDetailPrice = findViewById(R.id.txtDetailPrice);
        txtDetailType = findViewById(R.id.txtDetailType);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        Product product = productController.selectProductByID(productID);

        byte[] bitmapData = product.getImage();
        if (bitmapData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            imgDetailProduct.setImageBitmap(bitmap);
        } else {
            imgDetailProduct.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.product));
        }

        txtDetailName.setText(product.getName());
        txtDetailDescription.setText(product.getDescription());
        txtDetailType.setText(product.getType());
        txtDetailPrice.setText(getString(R.string.product_price, product.getPrice()));
    }
}
