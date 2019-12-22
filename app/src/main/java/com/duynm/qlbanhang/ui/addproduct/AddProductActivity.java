package com.duynm.qlbanhang.ui.addproduct;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.duynm.qlbanhang.data.product.ProductNavigator;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity implements ProductNavigator {
    Button btnTakePhoto, btnSelectPhoto, btnAddProduct;
    ImageView imgProduct, ivBack;
    EditText txtName, txtPrice, txtDescription, txtAddOtherType;
    Spinner spinnerAddType;
    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    private ProductController productController;
    private String type = "";
    private ArrayList<String> allTypes;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productController = new ProductController(this, this);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnAddProduct = findViewById(R.id.btnAddProduct);
        imgProduct = (ImageView) findViewById(R.id.imgAddProduct);
        ivBack = findViewById(R.id.iv_back);
        txtName = findViewById(R.id.txtAddName);
        txtPrice = findViewById(R.id.txtAddPrice);
        txtDescription = findViewById(R.id.txtAddDescription);
        spinnerAddType = findViewById(R.id.spinnerAddType);
        txtAddOtherType = findViewById(R.id.txtAddOtherType);
    }

    private void addEvents() {
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogUploadImage();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check and add new Product
                if (txtName.getText().toString().isEmpty() || txtPrice.getText().toString().isEmpty()) {
                    Toast.makeText(AddProductActivity.this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
                } else {
                    if (type.isEmpty()) type = txtAddOtherType.getText().toString();
                    if (bitmap == null) {
                        productController.addProduct(new Product(
                                txtName.getText().toString(),
                                txtDescription.getText().toString(),
                                null,
                                Double.parseDouble(txtPrice.getText().toString()),
                                type.trim()
                        ));
                    } else {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        bitmap.recycle();
                        productController.addProduct(new Product(
                                txtName.getText().toString(),
                                txtDescription.getText().toString(),
                                byteArray,
                                Double.parseDouble(txtPrice.getText().toString()),
                                type.trim()
                        ));
                    }

                    Toast.makeText(AddProductActivity.this, type, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        //Spinner
        allTypes = productController.selectAllType();
        if (allTypes == null) allTypes = new ArrayList();
        allTypes.add(getString(R.string.new_type));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddType.setAdapter(adapter);
        spinnerAddType.setSelection(0);
        spinnerAddType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == allTypes.size() - 1) {
                    txtAddOtherType.setVisibility(View.VISIBLE);
                    type = "";
                } else {
                    txtAddOtherType.setVisibility(View.GONE);
                    type = allTypes.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void openDialogUploadImage() {
        final Dialog dialog = new Dialog(AddProductActivity.this);
        dialog.setContentView(R.layout.dialog_upload_image);
        btnTakePhoto = (Button) dialog.findViewById(R.id.btnTakePhoto);
        btnSelectPhoto = (Button) dialog.findViewById(R.id.btnSelectPhoto);
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
                    imgProduct.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgProduct.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void displayProducts(ArrayList<Product> products) {
    }
}
