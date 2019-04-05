package com.zjf.transaction.user;

import android.Manifest;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.util.ImageLoaderUtil;
import com.zjf.transaction.util.ScreenUtil;
import com.zjf.transaction.util.permission.GiveMePermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends BaseActivity implements GiveMePermission.OnRequestPermissionCallback {

    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    public final int CODE_SELECT_PHOTO = 2;//相册RequestCode
    public final int CODE_PERMISSION_TAKE_PHOTO_WRITE_EXTERNAL = 1; //相机和存储RequestCode
    public final int CODE_PERMISSION_WRITE_EXTERNAL = 2; //相机和存储RequestCode


    private ImageView ivUserPic;
    private EditText etAccount, etPassword;
    private ViewGroup spinnerLayout;
    private Spinner spinnerProvince, spinnerCity, spinnerUniversity;
    private TextView tvRegister;
    private ViewGroup getPictureLayout;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ScreenUtil.hideStatusBarLight(this);
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
                        .permission(Manifest.permission.CAMERA)
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
        spinnerLayout = findViewById(R.id.layout_spinner);
        spinnerProvince = spinnerLayout.findViewById(R.id.spinner_province);
        spinnerCity = spinnerLayout.findViewById(R.id.spinner_city);
        spinnerUniversity = spinnerLayout.findViewById(R.id.spinner_university);
        tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/4 登录操作
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
    }

    private Uri getMediaFileUri() {
        File photoPath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "userPic");
        if (!photoPath.exists()) {
            if (!photoPath.mkdirs()) {
                return null;
            }
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        File photoFile = new File(photoPath.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
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
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        ImageLoaderUtil.loadImage(ivUserPic, bitmap);
        saveUserPicTODatabaseAndService(bitmap);
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
        Bitmap bitmap = null;
        if (data != null) {
            if (data.hasExtra("data")) {
                bitmap = data.getParcelableExtra("data");
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeFile(photoUri.getPath());
            }
        }
        ImageLoaderUtil.loadImage(ivUserPic, bitmap);
        saveUserPicTODatabaseAndService(bitmap);
    }


    /**
     * 将用户的图片保存到数据库和服务器
     *
     * @param bitmap
     */
    private void saveUserPicTODatabaseAndService(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        // TODO: 2019/4/5
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
}
