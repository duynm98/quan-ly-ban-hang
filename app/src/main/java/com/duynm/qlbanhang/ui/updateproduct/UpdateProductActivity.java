package com.duynm.qlbanhang.ui.updateproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;
import com.duynm.qlbanhang.ui.addproduct.AddProductActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UpdateProductActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack, imgUpdateProduct;
    private EditText txtUpdateName, txtUpdatePrice, txtUpdateOtherType, txtUpdateDescription;
    private Button btnUpdateProduct;
    private Spinner spnUpdateType;

    private ProductController productController;

    private int productID = -1;
    private Product product;
    private String type = "";
    private ArrayList<String> allTypes;
    private Bitmap bitmap;

    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        productController = new ProductController(this, null);

        productID = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productID < 0) {
            finish();
        }

        product = productController.selectProductByID(productID);

        initViewComponent();
        initUI();
    }

    private void initViewComponent() {
        ivBack = findViewById(R.id.iv_back);
        imgUpdateProduct = findViewById(R.id.imgUpdateProduct);
        txtUpdateName = findViewById(R.id.txtUpdateName);
        txtUpdatePrice = findViewById(R.id.txtUpdatePrice);
        txtUpdateOtherType = findViewById(R.id.txtUpdateOtherType);
        txtUpdateDescription = findViewById(R.id.txtUpdateDescription);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        spnUpdateType = findViewById(R.id.spinnerUpdateType);

        btnUpdateProduct.setOnClickListener(this);
        imgUpdateProduct.setOnClickListener(this);
    }

    private void initUI() {

        initSpinner();

        byte[] bitmapData = product.getImage();
        if (bitmapData != null) {
            bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            imgUpdateProduct.setImageBitmap(bitmap);
        } else {
            imgUpdateProduct.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.product));
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtUpdateName.setText(product.getName());
        txtUpdateDescription.setText(product.getDescription());
//        txtUpdateOtherType.setText(product.getType());
        txtUpdatePrice.setText(String.valueOf(product.getPrice()));
    }

    private void initSpinner() {
        //Spinner
        allTypes = productController.selectAllType();
        if (allTypes == null) allTypes = new ArrayList();
        allTypes.add(getString(R.string.new_type));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUpdateType.setAdapter(adapter);
        spnUpdateType.setSelection(0);
        spnUpdateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == allTypes.size() - 1) {
                    txtUpdateOtherType.setVisibility(View.VISIBLE);
                    type = "";
                    txtUpdateOtherType.requestFocus();
                } else {
                    txtUpdateOtherType.setVisibility(View.GONE);
                    type = allTypes.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void updateProduct() {
        if (txtUpdateName.getText().toString().isEmpty() || txtUpdatePrice.getText().toString().isEmpty() || !Pattern.compile(pricePattern).matcher(txtUpdatePrice.getText().toString()).matches()) {
            Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
        } else {
            if (type.isEmpty()) type = txtUpdateOtherType.getText().toString();
            product.setName(txtUpdateName.getText().toString());
            product.setDescription(txtUpdateDescription.getText().toString());
            product.setPrice(Double.parseDouble(txtUpdatePrice.getText().toString()));
            product.setType(type.trim());
            if (bitmap == null) {
                product.setImage(null);
            } else {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();
                product.setImage(byteArray);
            }

            productController.update(product);
//            Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void openDialogUploadImage() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_upload_image);
        Button btnTakePhoto = (Button) dialog.findViewById(R.id.btnTakePhoto);
        Button btnSelectPhoto = (Button) dialog.findViewById(R.id.btnSelectPhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                dialog.dismiss();
            }
        });
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(is);
                    imgUpdateProduct.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgUpdateProduct.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgUpdateProduct:
                openDialogUploadImage();
                break;
            case R.id.btnUpdateProduct:
                updateProduct();
                break;
        }
    }
}
