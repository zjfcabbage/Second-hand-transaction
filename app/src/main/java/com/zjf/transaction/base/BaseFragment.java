package com.zjf.transaction.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjf.transaction.util.LogUtil;

/**
 * Created by zhengjiafeng on 2019/3/13
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public abstract class BaseFragment extends Fragment {

    private View contentCacheView;

    public abstract View onCreateContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("=== onAttach : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("=== onCreate : %s ===", getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("=== onCreateView : %s ===", getClass().getSimpleName());
        if (contentCacheView != null) {
            ViewGroup contentParent = (ViewGroup) contentCacheView.getParent();
            if (contentParent != null) {
                contentParent.removeView(contentCacheView);
            }
            return contentCacheView;
        } else {
            contentCacheView = onCreateContent(inflater, container, savedInstanceState);
            return contentCacheView;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("=== onActivityCreated : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("=== onStart : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("=== onResume : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("=== onPause : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("=== onStop : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("=== onDestroyView : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("=== onDestroy : %s ===", getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("=== onDetach : %s ===", getClass().getSimpleName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && contentCacheView != null) {
            onResume();
        }
    }

    public String getTitle() {
        return getClass().getSimpleName();
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == View.NO_ID || contentCacheView == null) {
            return null;
        }
        return contentCacheView.findViewById(id);
    }
}
