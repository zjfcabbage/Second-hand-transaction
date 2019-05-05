package com.zjf.transaction.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zjf.transaction.MainActivity;
import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.user.api.impl.UserApiImpl;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.LogUtil;

import androidx.appcompat.app.AlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class LoginActivity extends BaseActivity {

    private EditText etAccount, etPassword;
    private AlertDialog dialog;
    private final UserConfig userConfig = UserConfig.inst();
    private boolean loginSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        startByRegisterActivity();  //是否是注册后直接登录的
    }

    private void startByRegisterActivity() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(KEY_BUNDLE);
            if (bundle != null) {
                String activity = bundle.getString("activity");
                User user = bundle.getParcelable("user");
                if ("RegisterActivity".equalsIgnoreCase(activity) && user != null) {
                    login(user.getUserName(), user.getPassword());
                }
            }
        }
    }

    /**
     * 登录
     */
    private void login(String userName, String password) {
        dialog = new AlertDialog.Builder(LoginActivity.this)
                .setView(R.layout.layout_logining)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (loginSuccess) {
                            MainActivity.start(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
        dialog.show();
        UserApiImpl.login(userName, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<User>>() {
                    @Override
                    public void accept(DataResult<User> userDataResult) throws Exception {
                        if (userDataResult.code == DataResult.CODE_SUCCESS) {
                            final User currentUser = userDataResult.data;
                            if (currentUser != null) {
                                userConfig.setUserName(currentUser.getUserName());
                                userConfig.setUserPassword(currentUser.getPassword());
                                userConfig.setUserProvince(currentUser.getProvince());
                                userConfig.setUserCity(currentUser.getCity());
                                userConfig.setUserUniversity(currentUser.getUniversity());
                                userConfig.setUserId(currentUser.getUserId());
                                userConfig.setUserPicUrl(currentUser.getUserPicUrl());
                                userConfig.setUser(currentUser);
                                userConfig.setLastLoginTime(System.currentTimeMillis());
                                loginSuccess = true;
                            } else {
                                loginSuccess = false;
                            }
                        } else {
                            LogUtil.d("login error, msg -> %s", userDataResult.msg);
                        }
                        dialog.cancel();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getMessage());
                        dialog.cancel();
                    }
                });
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account = etAccount.getText().toString();
                final String password = etPassword.getText().toString();
                login(account, password);
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
