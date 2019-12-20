package com.duynm.qlbanhang.ui.product;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.duynm.qlbanhang.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddProductActivity extends AppCompatActivity {
    Button btnTakePhoto,btnSelectPhoto;
    ImageView imgProduct;
    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        addControls();
        addEvents();
    }

    private void addControls() {
        imgProduct=(ImageView) findViewById(R.id.imgAddProduct);
    }

    private void addEvents() {
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogUploadImage();
            }
        });
    }

    private void openDialogUploadImage() {
        final Dialog dialog=new Dialog(AddProductActivity.this);
        dialog.setContentView(R.layout.dialog_upload_image);
        btnTakePhoto=(Button) dialog.findViewById(R.id.btnTakePhoto);
        btnSelectPhoto=(Button) dialog.findViewById(R.id.btnSelectPhoto);
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

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }
    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CHOOSE_PHOTO){
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgProduct.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == RESQUEST_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgProduct.setImageBitmap(bitmap);
            }
        }
    }




}
