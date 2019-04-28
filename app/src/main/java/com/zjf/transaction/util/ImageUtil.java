package com.zjf.transaction.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.zjf.transaction.R;
import com.zjf.transaction.util.glide.GlideApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * Created by zjfcabbage on 2019/2/6
 *
 * @author 糟老头子 zjfcabbage
 */
public class ImageUtil {
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageView == null || TextUtils.isEmpty(imageUrl)) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.icon_placeholder)
                .into(imageView);
    }
    public static void loadImage(ImageView imageView, @DrawableRes @NonNull int drawableId) {
        if (imageView == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(drawableId)
                .placeholder(R.drawable.icon_placeholder)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, Bitmap bitmap) {
        if (imageView == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(bitmap)
                .placeholder(R.drawable.icon_placeholder)
                .into(imageView);
    }
    public static File compressImage(String sourceFile, String targetFile, int quality) {
        Bitmap bitmap = narrowBitmap(sourceFile);
        File outputFile = new File(targetFile);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdir();
            } else {
                outputFile.delete();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return outputFile;
    }


    private static Bitmap narrowBitmap(String sourceFile) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(sourceFile, options);
        options.inSampleSize = calculateInSampleSize(options, 400, 400);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(sourceFile);
    }

    /**
     * 计算缩放比例
     * @param options
     * @param reqWidth 期望的宽度
     * @param reqHeight 期望的高度
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
