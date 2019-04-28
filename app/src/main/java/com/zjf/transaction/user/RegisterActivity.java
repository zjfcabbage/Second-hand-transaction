package com.zjf.transaction.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zjf.transaction.util.ScreenUtil;
import com.zjf.transaction.util.permission.GiveMePermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@SuppressLint("CheckResult")
public class RegisterActivity extends BaseActivity implements GiveMePermission.OnRequestPermissionCallback {

    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    public final int CODE_SELECT_PHOTO = 2;//相册RequestCode
    public final int CODE_PERMISSION_TAKE_PHOTO_WRITE_EXTERNAL = 1; //相机和存储RequestCode
    public final int CODE_PERMISSION_WRITE_EXTERNAL = 2; //相机和存储RequestCode
    private static final String UPLOAD_IMAGE_KEY = "image";


    private ImageView ivUserPic;
    private EditText etAccount, etPassword;
    private Spinner spinnerProvince, spinnerCity, spinnerUniversity;
    private TextView tvRegister;
    private ViewGroup getPictureLayout;
    private Uri photoUri;
    private String userId;
    private Bitmap userPic;
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

        userId = getRandomUserIdOnly(10);
        LogUtil.d("user id -> %s", userId);

        initUserView();
        initSpinnerLayout();
        initGetPictureLayout();
    }

    private void initGetPictureLayout() {
        getPictureLayout = findViewById(R.id.layout_get_picture);
        final View backgroundView = getPictureLayout.findViewById(R.id.view_background);
        final TextView tvGetPictureFromCamera = getPictureLayout.findViewById(R.id.tv_get_from_camera);
        final TextView tvGetPictureFromPhoto = getPictureLayout.findViewById(R.id.tv_get_from_photo);
        backgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGetPictureLayoutVisibility(View.GONE); //更改状态栏字体为深色调
            }
        });
        tvGetPictureFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求相机权限和存储的权限，授权之后会跳转到onPermissionGranted和onPermissionDenied
                GiveMePermission.with(RegisterActivity.this)
                        .permissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .positiveText("确定")
                        .negativeText("取消")
                        .rationale(R.string.permission_rationale_text)
                        .requestCode(CODE_PERMISSION_TAKE_PHOTO_WRITE_EXTERNAL)
                        .request();
            }
        });
        tvGetPictureFromPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiveMePermission.with(RegisterActivity.this)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .positiveText("确定")
                        .negativeText("取消")
                        .rationale("从相册选择图片需要用到存储权限")
                        .requestCode(CODE_PERMISSION_WRITE_EXTERNAL)
                        .request();
            }
        });
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
                setGetPictureLayoutVisibility(View.VISIBLE);
                ScreenUtil.hideStatusBar(RegisterActivity.this);  //更改状态栏字体为浅色调
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

    private Uri getMediaFileUri() {
        File photoPath = new File(Environment.getExternalStorageDirectory(), getPackageName());
        if (!photoPath.exists()) {
            if (!photoPath.mkdirs()) {
                return null;
            }
        }
        final String userPicName = userId + "_IMG_" + System.currentTimeMillis() + ".jpg";
        File photoFile = new File(photoPath, userPicName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
        } else {
            return Uri.fromFile(photoFile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setGetPictureLayoutVisibility(View.GONE);
        GiveMePermission.onRequestPermissionResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setGetPictureLayoutVisibility(View.GONE);
        if (requestCode == CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            setUserPicFromCamera(data);
        } else if (requestCode == CODE_SELECT_PHOTO && resultCode == RESULT_OK) {
            setUserPicFromAlbum(data);
        }
    }

    private void setUserPicFromAlbum(Intent data) {
        Uri selectImageUri = data.getData();
        if (selectImageUri == null)
            return;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
        if (cursor == null) {
            return;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String photoPath = cursor.getString(columnIndex);
        cursor.close();
        userPic = BitmapFactory.decodeFile(photoPath);
        ImageUtil.loadImage(ivUserPic, userPic);
    }

    private void setGetPictureLayoutVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            getPictureLayout.setVisibility(View.VISIBLE);
            ScreenUtil.hideStatusBarLight(this);
        } else {
            getPictureLayout.setVisibility(View.GONE);
            ScreenUtil.hideStatusBar(this);
        }
    }

    private void setUserPicFromCamera(Intent data) {
        if (data != null) {
            if (data.hasExtra("data")) {
                userPic = data.getParcelableExtra("data");
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    userPic = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                userPic = BitmapFactory.decodeFile(photoUri.getPath());
            }
        }
        ImageUtil.loadImage(ivUserPic, userPic);
    }


    /**
     * 将用户的图片保存到数据库和服务器
     */
    @SuppressLint("CheckResult")
    private void uploadUserPic(final AlertDialog dialog) {
        if (userPic == null) {
            return;
        }
        final String fileName = userId + "_IMG_" + System.currentTimeMillis() + ".jpg";
        final File userPicFile = new File(Environment.getExternalStorageDirectory() + File.separator + getPackageName(), fileName);
        if (!userPicFile.exists()) {
            userPicFile.getParentFile().mkdir();
        }
        try {
            //图片压缩
            FileOutputStream outputStream = new FileOutputStream(userPicFile);
            userPic.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), userPicFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData(UPLOAD_IMAGE_KEY, fileName, requestBody);
        UserApiImpl.uploadUserPic(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResult<String>>() {
                    @Override
                    public void accept(DataResult<String> stringDataResult) throws Exception {
                        LogUtil.d("upload user pic success");
                        UserConfig.inst().setUserPicUrl(stringDataResult.data);
                        registerSuccess = true;
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        registerSuccess = false;
                        dialog.dismiss();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void register() {
        if (user == null || userNameExist) {
            return;
        }
        LogUtil.d(user.toString());
        final AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                .setCancelable(false)
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
        UserApiImpl.register(user)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<DataResult<User>>() {
                    @Override
                    public void accept(DataResult<User> userDataResult) throws Exception {
                        if (userDataResult.code == DataResult.CODE_SUCCESS) {
                            LogUtil.d("user -> %s", "register success");
                            UserConfig.inst().setUser(userDataResult.data);  //将user保存到配置文件
                            uploadUserPic(dialog);  //上传用户头像
//                            registerSuccess = true;
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

    @Override
    public void onPermissionGranted(int requestCode, String[] permissions) {
        if (requestCode == CODE_PERMISSION_TAKE_PHOTO_WRITE_EXTERNAL) { //打开相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoUri = getMediaFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, CODE_TAKE_PHOTO);
        } else if (requestCode == CODE_PERMISSION_WRITE_EXTERNAL) { //打开相册
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, CODE_SELECT_PHOTO);
        }
    }

    @Override
    public void onPermissionDenied(int requestCode, String[] deniedPermissions) {
        Toast.makeText(this, "拒绝权限将无法正常使用此应用的部分功能", Toast.LENGTH_SHORT).show();
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
