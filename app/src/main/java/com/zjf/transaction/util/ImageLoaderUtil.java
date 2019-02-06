package com.zjf.transaction.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.zjf.transaction.R;
import com.zjf.transaction.app.glide.GlideApp;


/**
 * Created by zjfcabbage on 2019/2/6
 *
 * @author 糟老头子 zjfcabbage
 */
public class ImageLoaderUtil {
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageView == null || TextUtils.isEmpty(imageUrl)) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeImage)
                .into(imageView);
    }
}
