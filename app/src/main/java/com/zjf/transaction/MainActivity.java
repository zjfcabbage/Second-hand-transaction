package com.zjf.transaction;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zjf.transaction.base.BaseActivity;
import com.zjf.transaction.base.BaseFragment;
import com.zjf.transaction.main.MainFragment;
import com.zjf.transaction.mine.MineFragment;
import com.zjf.transaction.msg.MsgFragment;
import com.zjf.transaction.shopcart.ShopcartFragment;

public class MainActivity extends BaseActivity {

    private final SparseArray<BaseFragment> fragmentArray = new SparseArray<>(4);
    private static final SparseIntArray checkFragmentIndex = new SparseIntArray();
    private static final SparseIntArray titleArray = new SparseIntArray(4);
    private RadioGroup radioGroup;
    private int defaultIndex = 0;
    private ViewGroup layoutTitle;
    private TextView tvTitle;

    static {
        checkFragmentIndex.put(0, R.id.btn_main_page);
        checkFragmentIndex.put(1, R.id.btn_shopcart);
        checkFragmentIndex.put(2, R.id.btn_message);
        checkFragmentIndex.put(3, R.id.btn_mine);

        checkFragmentIndex.put(R.id.btn_main_page, 0);
        checkFragmentIndex.put(R.id.btn_shopcart, 1);
        checkFragmentIndex.put(R.id.btn_message, 2);
        checkFragmentIndex.put(R.id.btn_mine, 3);

        titleArray.put(R.id.btn_main_page, R.string.activity_main_main_page);
        titleArray.put(R.id.btn_shopcart, R.string.activity_main_shop_cart);
        titleArray.put(R.id.btn_message, R.string.activity_main_message);
        titleArray.put(R.id.btn_mine, R.string.activity_main_mine);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTitleLayout();
        initBottomBar();
        performCheck(checkFragmentIndex.get(defaultIndex));
    }

    private void initTitleLayout() {
        layoutTitle = findViewById(R.id.layout_title);
        layoutTitle.findViewById(R.id.iv_common_back).setVisibility(View.GONE);
        layoutTitle.findViewById(R.id.iv_common_more).setVisibility(View.GONE);
        tvTitle = layoutTitle.findViewById(R.id.tv_common_title);
    }

    private void initBottomBar() {
        radioGroup = findViewById(R.id.layout_bottom_bar);
        final View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int checkId = v.getId();
                final int previousCheckIndex = checkFragmentIndex.get(defaultIndex);
                if (previousCheckIndex != checkId) { //还是原先的tab，忽略
                    performCheck(checkId);
                }
            }
        };
        radioGroup.findViewById(R.id.btn_main_page).setOnClickListener(radioButtonClickListener);
        radioGroup.findViewById(R.id.btn_message).setOnClickListener(radioButtonClickListener);
        radioGroup.findViewById(R.id.btn_shopcart).setOnClickListener(radioButtonClickListener);
        radioGroup.findViewById(R.id.btn_mine).setOnClickListener(radioButtonClickListener);
    }

    private void performCheck(int checkId) {
        radioGroup.check(checkId);
        tvTitle.setText(titleArray.get(checkId));
        final int previousCheckId = checkFragmentIndex.get(defaultIndex);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        final BaseFragment previousFragment = fragmentArray.get(previousCheckId); //获取之前显示的fragment

        if (previousFragment != null) {
            transaction.hide(previousFragment);
        }
        defaultIndex = checkFragmentIndex.get(checkId); //更新显示的fragment的id
        BaseFragment currentFragment = fragmentArray.get(checkId);
        if (currentFragment == null) {
            currentFragment = obtainFragment(checkId);
            transaction.add(R.id.fragment_container, currentFragment, currentFragment.getTitle());
        }
//        currentFragment.setArguments();  //传递参数
        currentFragment.setUserVisibleHint(true);
        transaction.show(currentFragment);
        transaction.commitAllowingStateLoss();
    }

    private BaseFragment obtainFragment(int checkId) {
        BaseFragment fragment = fragmentArray.get(checkId);
        if (fragment != null) {
            return fragment;
        }
        switch (checkId) {
            case R.id.btn_main_page:
                fragment = new MainFragment();
                break;
            case R.id.btn_message:
                fragment = new MsgFragment();
                break;
            case R.id.btn_shopcart:
                fragment = new ShopcartFragment();
                break;
            case R.id.btn_mine:
                fragment = new MineFragment();
                break;
            default:
                throw new IllegalArgumentException("checkId doesn't match any fragment");
        }
        fragmentArray.put(checkId, fragment);
        return fragment;
    }


}
