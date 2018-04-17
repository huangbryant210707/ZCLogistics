package com.huangbrayant.citypicker;

import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.huangbrayant.citypicker.adapter.OnPickListener;
import com.huangbrayant.citypicker.model.City;
import com.huangbrayant.citypicker.model.LocateState;
import com.huangbrayant.citypicker.model.LocatedCity;
import com.huangbrayant.citypicker.model.HotCity;

import java.util.List;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/6 17:52
 */
public class CityPicker {
    private static final String TAG = "CityPicker";

    private static CityPicker mInstance;
    private String title = "城市选择";

    private CityPicker() {
    }

    public static CityPicker getInstance() {
        if (mInstance == null) {
            synchronized (CityPicker.class) {
                if (mInstance == null) {
                    mInstance = new CityPicker();
                }
            }
        }
        return mInstance;
    }

    private FragmentManager mFragmentManager;
    private Fragment mTargetFragment;

    private boolean enableAnim;
    private int mAnimStyle;
    private List<City> mLocatedCity;
    private List<City> mHotCities;
    private OnPickListener mOnPickListener;

    public CityPicker setFragmentManager(FragmentManager fm) {
        this.mFragmentManager = fm;
        return this;
    }

    public CityPicker setTargetFragment(Fragment targetFragment) {
        this.mTargetFragment = targetFragment;
        return this;
    }

    /**
     * 设置动画效果
     *
     * @param animStyle
     * @return
     */
    public CityPicker setAnimationStyle(@StyleRes int animStyle) {
        this.mAnimStyle = animStyle;
        return this;
    }

    /**
     * 设置当前已经定位的城市
     *
     * @param location
     * @return
     */
    public void setLocatedCity(List<City> location) {
        mLocatedCity = location;
    }

    public CityPicker setHotCities(List<City> data) {
        this.mHotCities = data;
        return this;
    }

    /**
     * 启用动画效果，默认为false
     *
     * @param enable
     * @return
     */
    public CityPicker enableAnimation(boolean enable) {
        this.enableAnim = enable;
        return this;
    }

    /**
     * 设置title
     *
     * @param title
     * @return
     */
    public CityPicker title(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置选择结果的监听器
     *
     * @param listener
     * @return
     */
    public CityPicker setOnPickListener(OnPickListener listener) {
        this.mOnPickListener = listener;
        return this;
    }

    public void show() {
        if (mFragmentManager == null) {
            throw new UnsupportedOperationException("CityPicker：method setFragmentManager() must be called.");
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        final Fragment prev = mFragmentManager.findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev).commit();
            ft = mFragmentManager.beginTransaction();
        }
        ft.addToBackStack(null);
        final CityPickerDialogFragment cityPickerFragment =
                CityPickerDialogFragment.newInstance(enableAnim);
        cityPickerFragment.setLocatedCity(mLocatedCity);
        cityPickerFragment.setHotCities(mHotCities);
        cityPickerFragment.setAnimationStyle(mAnimStyle);
        cityPickerFragment.setTitle(title);
        cityPickerFragment.setOnPickListener(mOnPickListener);
        if (mTargetFragment != null) {
            cityPickerFragment.setTargetFragment(mTargetFragment, 0);
        }
        cityPickerFragment.show(ft, TAG);
    }

    /**
     * 定位完成
     *
     * @param location
     * @param state
     */
    public void locateComplete(LocatedCity location, @LocateState.State int state) {
        CityPickerDialogFragment fragment = (CityPickerDialogFragment) mFragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            fragment.locationChanged(location, state);
        }
    }
}
