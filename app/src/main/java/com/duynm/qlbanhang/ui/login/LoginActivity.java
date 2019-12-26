package com.duynm.qlbanhang.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duynm.qlbanhang.R;
import com.duynm.qlbanhang.ui.home.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String DEFAULT_USERNAME = "huanhoahong";
    private final static String DEFAULT_PASSWORD = "123456";

    private TextView txtSignUp;
    private EditText txtAccount, txtPassword;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViewComponent();
        initUI();

    }

    private void initUI() {
        addLineForSignUp();
    }

    private void initViewComponent() {
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtAccount = findViewById(R.id.txtAcount);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    private void addLineForSignUp() {
        String mystring = "Đăng kí";
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        txtSignUp.setText(content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (isCorrectAcount()){
                    startActivity(new Intent(this, MainActivity.class));
                    break;
                }else {
                    Toast.makeText(this,"Sai thông tin đăng nhập",Toast.LENGTH_LONG).show();
                    break;
                }

            case R.id.txtSignUp:
                Toast.makeText(this, "Chưa có chức năng đăng ký", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean isCorrectAcount(){
        return (txtAccount.getText().toString().equals(DEFAULT_USERNAME)&& txtPassword.getText().toString().equals(DEFAULT_PASSWORD));

    }
}
