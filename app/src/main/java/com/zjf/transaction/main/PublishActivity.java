package com.zjf.transaction.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import android.util.Pair;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zjf.transaction.R;
import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.DataResult;
import com.zjf.transaction.main.api.impl.MainApiImpl;
import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.user.UserConfig;
import com.zjf.transaction.util.ImageUtil;
import com.zjf.transaction.util.LogUtil;
import com.zjf.transaction.util.ScreenUtil;
import com.zjf.transaction.util.qiniu.QiNiuUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PublishActivity extends BaseActivity {

    private static final int PIC_COUNT = 4;
    private ViewGroup picLayout;
    private EditText etMsg, etPrice;
    private ImageView ivMore;
    private Button btnPublish;

    private SparseArray<Pair<String, String>> picSparseArray = new SparseArray<>(PIC_COUNT);
    private boolean isPublishSuccess;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ScreenUtil.hideStatusBar(this);

        initView();
        dialog = new AlertDialog.Builder(PublishActivity.this)
                .setView(R.layout.layout_logining)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (isPublishSuccess) {
                            Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    }
                })
                .create();
    }

    private void initView() {
        initTitleLayout();

        etMsg = findViewById(R.id.et_msg);
        etPrice = findViewById(R.id.et_price);

        picLayout = findViewById(R.id.layout_pic);
        ivMore = picLayout.findViewById(R.id.iv_more);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PublishActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .previewImage(true)
                        .compress(true)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .maxSelectNum(PIC_COUNT - picLayout.getChildCount() + 1)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
        btnPublish = findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final String userId = UserConfig.inst().getUserId();
                final long publishTime = System.currentTimeMillis();
                final String id = userId + "_" + publishTime;
                final String msg = etMsg.getText().toString();
                final String priceString = etPrice.getText().toString();
                if (msg.isEmpty() || priceString.isEmpty() || picSparseArray.size() == 0) {
                    Toast.makeText(PublishActivity.this, "请完善所有内容再发布", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    return;
                }
                dialog.show();
                final ArrayList<Pair<String, String>> picList = new ArrayList<>();
                for (int i = 0; i < picSparseArray.size(); i++) {
                    picList.add(picSparseArray.get(i));
                }
                QiNiuUtil.uploadImageList(picList, new QiNiuUtil.ActionListener() {
                    @Override
                    public void success(String url) {
                        final Commodity commodity = new Commodity(id, userId, url, msg, priceString, publishTime);
                        LogUtil.d(commodity.toString());
                        MainApiImpl.publish(commodity)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<DataResult<String>>() {
                                    @Override
                                    public void accept(DataResult<String> stringDataResult) throws Exception {
                                        if (stringDataResult.code == DataResult.CODE_SUCCESS) {
                                            LogUtil.d("publish success");
                                            isPublishSuccess = true;
                                            dialog.dismiss();
                                        } else {
                                            LogUtil.e("publish failed, msg -> %s", stringDataResult.msg);
                                            isPublishSuccess = false;
                                            dialog.dismiss();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        LogUtil.e("publish failed, throwable -> %s", throwable.getMessage());
                                        isPublishSuccess = false;
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });
    }


    private void initTitleLayout() {
        ViewGroup titleLayout = findViewById(R.id.layout_title);
        titleLayout.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
        ((TextView) titleLayout.findViewById(R.id.tv_common_title)).setText("发布");
        final ImageView ivBack = titleLayout.findViewById(R.id.iv_common_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (!selectList.isEmpty()) {
                    int index = picSparseArray.size();
                    for (int i = 0; i < selectList.size(); i++) {
                        addPic(selectList.get(i), i + index);
                    }
                }
                if (picLayout.getChildCount() == PIC_COUNT + 1) {
                    ivMore.setVisibility(View.INVISIBLE);
                } else {
                    ivMore.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void addPic(LocalMedia localMedia, int index) {
        final String sourcePath = localMedia.getPath(); //原图片路径
        final String compressPath = localMedia.getCompressPath(); //压缩后的路径
        final int width = ScreenUtil.dp2px(this, 80);
        final int height = ScreenUtil.dp2px(this, 80);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height, Gravity.CENTER);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        ImageUtil.loadImage(imageView, compressPath);

        ImageView ivDelete = new ImageView(this);
        ivDelete.setImageResource(R.drawable.icon_delete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parent = (ViewGroup) v.getParent().getParent();  //linearLayout
                int childIndex = parent.indexOfChild((View) v.getParent());
                picLayout.removeViewAt(childIndex);
                final int count = picSparseArray.size();
                for (int i = childIndex + 1; i < count; i++) {
                    if (picSparseArray.get(i) != null) {
                        picSparseArray.put(i - 1, picSparseArray.get(i));
                    }
                }
                picSparseArray.remove(count - 1);
                LogUtil.d("删除掉了：%d, === 图片数量：%d", childIndex, picSparseArray.size());
                if (parent.getChildCount() < PIC_COUNT + 1 && ivMore.getVisibility() != View.VISIBLE) {
                    ivMore.setVisibility(View.VISIBLE);
                }
            }
        });

        FrameLayout frameLayout = new FrameLayout(this);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ScreenUtil.dp2px(this, 80),
                ScreenUtil.dp2px(this, 80));
        marginLayoutParams.rightMargin = ScreenUtil.dp2px(this, 10);
        frameLayout.setLayoutParams(marginLayoutParams);
        frameLayout.addView(imageView, new FrameLayout.LayoutParams(new FrameLayout.LayoutParams(ScreenUtil.dp2px(this, 80),
                ScreenUtil.dp2px(this, 80), Gravity.CENTER)));

        FrameLayout.LayoutParams deleteParams = new FrameLayout.LayoutParams(ScreenUtil.dp2px(this, 30),
                ScreenUtil.dp2px(this, 30), Gravity.TOP | Gravity.END);
        frameLayout.addView(ivDelete, deleteParams);
        picLayout.addView(frameLayout, index);
        Pair<String, String> pair = new Pair<>(UserConfig.inst().getUserId() + System.currentTimeMillis(), sourcePath);
        picSparseArray.put(index, pair);
    }
}
