package com.zjf.transaction.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.user.api.impl.LocationApiImpl;
import com.zjf.transaction.user.api.impl.UserApiImpl;
import com.zjf.transaction.user.model.City;
import com.zjf.transaction.user.model.Province;
import com.zjf.transaction.user.model.University;
import com.zjf.transaction.user.model.User;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.ListUtil;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.qiniu.QiNiuUtil;
import com.zjf.transaction.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@SuppressLint("CheckResult")
public class RegisterActivity extends BaseActivity {

    private static final String UPLOAD_IMAGE_KEY = "image";

    private ImageView ivUserPic;
    private EditText etAccount, etPassword;
    private Spinner spinnerProvince, spinnerCity, spinnerUniversity;
    private TextView tvRegister;
    private String userId;
    private String userPic;
    private User user = new User();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<University> universityList;
    private boolean registerSuccess = false;
    private boolean userNameExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ScreenUtil.hideStatusBarLight(this);

//        userId = getRandomUserIdOnly(10);
        LogUtil.d("user id -> %s", userId);

        initUserView();
        initSpinnerLayout();
    }


    private void initSpinnerLayout() {
        spinnerProvince = findViewById(R.id.spinner_province);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerUniversity = findViewById(R.id.spinner_university);
        tvRegister = findViewById(R.id.tv_register);
        setSpinnerProvinceData();
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = getRandomUserIdOnly(10);
                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(etAccount.getText().toString())
                        || TextUtils.isEmpty(etPassword.getText().toString())
                        || TextUtils.isEmpty(spinnerProvince.getSelectedItem().toString())
                        || TextUtils.isEmpty(spinnerCity.getSelectedItem().toString())
                        || TextUtils.isEmpty(spinnerUniversity.getSelectedItem().toString())) {
                    Toast.makeText(RegisterActivity.this, "请完善所有信息!", Toast.LENGTH_LONG).show();
                    return;
                }
                LogUtil.d(spinnerProvince.getSelectedItem().toString() + "---" + spinnerCity.getSelectedItem().toString()
                        + "---" + spinnerUniversity.getSelectedItem().toString());
                user.setUserId(userId);
                user.setUserName(etAccount.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.setProvince(spinnerProvince.getSelectedItem().toString());
                user.setCity(spinnerCity.getSelectedItem().toString());
                user.setUniversity(spinnerUniversity.getSelectedItem().toString());
                user.setUserPicUrl("");
                register();
            }
        });

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!ListUtil.isEmpty(provinceList)) {
                    final Province province = provinceList.get(position);
                    final int provinceId = province.getProvinceId();
                    setSpinnerCityData(provinceId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!ListUtil.isEmpty(cityList)) {
                    final City city = cityList.get(position);
                    final int cityId = city.getCityId();
                    setSpinnerUniversityData(cityId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSpinnerProvinceData() {
        LocationApiImpl.getProvince().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<List<Province>>>() {
                    @Override
                    public void accept(final DataResult<List<Province>> provinceDataResult) throws Exception {
                        if (provinceDataResult != null && provinceDataResult.code == DataResult.CODE_SUCCESS) {
                            if (!ListUtil.isEmpty(provinceDataResult.data)) {
                                provinceList = provinceDataResult.data;
                                final List<String> provinceNameList = new ArrayList<>();
                                for (Province province : provinceDataResult.data) {
                                    provinceNameList.add(province.getProvinceName());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this,
                                        R.layout.spinner_item, provinceNameList);
                                adapter.setDropDownViewResource(R.layout.spinner_item);
                                spinnerProvince.setAdapter(adapter);
                            }
                        } else {
                            if (provinceDataResult != null) {
                                LogUtil.e("get province error, code = %d, msg = %s", provinceDataResult.code, provinceDataResult.msg);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("get province throwable -> %s", throwable.getMessage());
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void setSpinnerCityData(int provinceId) {
        LocationApiImpl.getCityByProvinceId(provinceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<List<City>>>() {
                    @Override
                    public void accept(final DataResult<List<City>> cityDataResult) throws Exception {
                        if (cityDataResult != null && cityDataResult.code == DataResult.CODE_SUCCESS) {
                            if (!ListUtil.isEmpty(cityDataResult.data)) {
                                cityList = cityDataResult.data;
                                final List<String> cityNameList = new ArrayList<>();
                                for (City city : cityDataResult.data) {
                                    cityNameList.add(city.getCityName());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this,
                                        R.layout.spinner_item, cityNameList);
                                adapter.setDropDownViewResource(R.layout.spinner_item);
                                spinnerCity.setAdapter(adapter);
                            }
                        } else {
                            if (cityDataResult != null) {
                                LogUtil.e("get province error, code = %d, msg = %s", cityDataResult.code, cityDataResult.msg);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("get city throwable -> %s", throwable.getMessage());
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void setSpinnerUniversityData(int cityId) {
        LocationApiImpl.getUniversityByCityId(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<List<University>>>() {
                    @Override
                    public void accept(final DataResult<List<University>> universityDataResult) throws Exception {
                        if (universityDataResult != null && universityDataResult.code == DataResult.CODE_SUCCESS) {
                            if (!ListUtil.isEmpty(universityDataResult.data)) {
                                universityList = universityDataResult.data;
                                final List<String> universityNameList = new ArrayList<>();
                                for (University university : universityDataResult.data) {
                                    universityNameList.add(university.getUniversityName());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this,
                                        R.layout.spinner_item, universityNameList);
                                adapter.setDropDownViewResource(R.layout.spinner_item);
                                spinnerUniversity.setAdapter(adapter);
                            }
                        } else {
                            if (universityDataResult != null) {
                                LogUtil.e("get province error, code = %d, msg = %s", universityDataResult.code, universityDataResult.msg);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void initUserView() {
        ivUserPic = findViewById(R.id.iv_user_pic);
        ivUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(RegisterActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .previewImage(true)
                        .enableCrop(true)
                        .compress(true)
                        .freeStyleCropEnabled(true)
                        .circleDimmedLayer(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);

        etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    UserApiImpl.isUserNameExisted(etAccount.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<DataResult<String>>() {
                                @Override
                                public void accept(DataResult<String> stringDataResult) throws Exception {
                                    if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                                        if (stringDataResult.data != null) {
                                            //用户名存在
                                            userNameExist = true;
                                            Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_LONG).show();
                                        } else {
                                            userNameExist = false;
                                        }
                                    } else {
                                        LogUtil.e("userName is null");
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    LogUtil.e("throwable -> %s", throwable.getMessage());
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (!selectList.isEmpty()) {
                        LocalMedia localMedia = selectList.get(0);
                        LogUtil.d("path -> %s", localMedia.getPath() + " --- " + localMedia.getCutPath() + " --- " + localMedia.getCompressPath());
                        userPic = localMedia.getCompressPath();
                        ImageUtil.loadImage(ivUserPic, localMedia.getPath());
//                        PictureFileUtils.deleteCacheDirFile(RegisterActivity.this);
                    }
                    break;
            }
        }
    }

    @SuppressLint("CheckResult")
    private void register() {
        if (user == null || userNameExist) {
            return;
        }
        final AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                .setView(R.layout.layout_logining)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (registerSuccess) {
                            LogUtil.d("register success");
                            Toast.makeText(RegisterActivity.this, "注册成功，跳转登录中", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("activity", "RegisterActivity");
                            bundle.putParcelable("user", user);
                            LoginActivity.start(RegisterActivity.this, bundle, LoginActivity.class);
                            RegisterActivity.this.finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
        dialog.show();
        QiNiuUtil.upLoadImageWithSimpleToken(userPic, userId, new QiNiuUtil.ActionListener() {
            @Override
            public void success(String url) {
                PictureFileUtils.deleteCacheDirFile(RegisterActivity.this);
                user.setUserPicUrl(url);
                LogUtil.d(user.toString());
                UserApiImpl.register(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<DataResult<User>>() {
                            @Override
                            public void accept(DataResult<User> userDataResult) throws Exception {
                                if (userDataResult.code == DataResult.CODE_SUCCESS) {
                                    LogUtil.d("user -> %s", "register success");
                                    UserConfig.inst().setUser(userDataResult.data);  //将user保存到配置文件
                                    registerSuccess = true;
                                    dialog.dismiss();
                                } else {
                                    LogUtil.d("user -> register failed, msg = %s", userDataResult.msg);
                                    registerSuccess = false;
                                    dialog.dismiss();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.e(throwable.getMessage());
                                registerSuccess = false;
                                dialog.dismiss();
                            }
                        });
            }
        });
    }

    /**
     * 生成用户随机id
     *
     * @param length id长度，一般为20
     * @return
     */
    private String getRandomUserId(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }

    @SuppressLint("CheckResult")
    private String getRandomUserIdOnly(final int length) {
        if (length < 10) {
            throw new IllegalArgumentException("the length of userId must be more than 20");
        }
        String userId = getRandomUserId(length);
        UserApiImpl.getUser(userId).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<DataResult<User>>() {
                    @Override
                    public void accept(DataResult<User> userDataResult) throws Exception {
                        if (userDataResult != null && userDataResult.code == 1 && userDataResult.data != null) {//说明这个userId已经存在了
                            getRandomUserIdOnly(length);
                        }
                    }
                });
        return userId;
    }
}
