package com.duynm.qlbanhang.ui.detailproduct;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.base.BaseActivity;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;
import com.duynm.qlbanhang.ui.addproduct.AddProductActivity;
import com.duynm.qlbanhang.ui.home.MainActivity;
import com.duynm.qlbanhang.ui.updateproduct.UpdateProductActivity;

public class DetailProductActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, imgDetailProduct;
    private TextView txtDetailName, txtDetailPrice, txtDetailType, txtDetailDescription;
    private Button btnEdit, btnDelete;

    private int productID = -1;
    private Product product;

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
//        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (productID > 0) {
            product = productController.selectProductByID(productID);
            if (product != null) {
                initUI();
            }
        }
    }

    private void initViewComponent() {
        ivBack = findViewById(R.id.iv_back);
        imgDetailProduct = findViewById(R.id.imgDetailProduct);
        txtDetailName = findViewById(R.id.txtDetailName);
        txtDetailPrice = findViewById(R.id.txtDetailPrice);
        txtDetailType = findViewById(R.id.txtDetailType);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);
        btnEdit = findViewById(R.id.btn_edit_product);
        btnDelete = findViewById(R.id.btn_delete_product);

        ivBack.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void initUI() {

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.btn_delete_product:
                new AlertDialog.Builder(this)
                        .setTitle(product.getName())
                        .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_product))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                productController.deleteProduct(product);
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;

            case R.id.btn_edit_product:
                Intent intent = new Intent(this, UpdateProductActivity.class);

                intent.putExtra("PRODUCT_ID", productID);

                startActivity(intent);
                break;
        }
    }
}
