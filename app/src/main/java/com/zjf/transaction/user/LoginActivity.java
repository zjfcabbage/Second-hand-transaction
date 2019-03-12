package com.zjf.transaction.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private EditText etAccount, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/3/12 登陆操作
            }
        });
        findViewById(R.id.tv_no_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.start(LoginActivity.this, RegisterActivity.class);
            }
        });
    }
}
