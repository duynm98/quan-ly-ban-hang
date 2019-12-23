package com.duynm.qlbanhang.ui.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.duynm.qlbanhang.R;

public class LoginActivity extends AppCompatActivity {
    TextView txtSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViewComponent();
        initUI();
        
    }

    private void initUI() {
    }

    private void initViewComponent() {
        txtSignUp=(TextView) findViewById(R.id.txtSignUp); 
        addLineForSignUp();
    }

    private void addLineForSignUp() {
        String mystring=new String("Đăng kí");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        txtSignUp.setText(content);
    }
}
