package com.example.myapplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by shipinchao on 2018/3/24.
 */

public class PhotoActivity extends AppCompatActivity implements BlueToothUtils.BlueToothStatusListener, ViewPager.OnPageChangeListener {

    private int[] photoId = {R.mipmap.bg1, R.mipmap.bg2, R.mipmap.bg3,
            R.mipmap.bg5, R.mipmap.bg6};
    ViewPager viewPager;
    private int currentIndex;

    private List<PhotoFragment> fragments = new ArrayList<>();
    private static final String RIGHT_STRING = "r"; // 右
    private static final String LEFT_STRING = "l";   // 左
    private static final String BIG_STRING = "b";  //  放大
    private static final String SMALL_STRING = "s";  // 缩小

    int screedWidth;
    int screenHeight;
    private float scal = 1F;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        screedWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        initView();
        BlueToothUtils.getIntence().addListener(this);
    }

    private void initView() {
        for (int i = 0; i < photoId.length; i++) {
            fragments.add(new PhotoFragment());
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                PhotoFragment fragment = fragments.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(PhotoFragment.EXTRA_RESID, photoId[position]);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @Override
    public void startScan() {

    }

    @Override
    public void scnanDevicesOver(List<BluetoothDevice> list) {

    }

    @Override
    public void haveNewData(String data) {

        if (isFinishing()) {
            return;
        }

        ToastUtils.show("收到   " + data);
        if (RIGHT_STRING.equals(data)) {
            if (currentIndex == photoId.length - 1) {
                currentIndex = -1;
            }
            viewPager.setCurrentItem(++currentIndex);
        } else if (LEFT_STRING.equals(data)) {
            if (currentIndex == 0) {
                currentIndex = photoId.length;
            }
            viewPager.setCurrentItem(--currentIndex);
        } else if (BIG_STRING.equals(data)) {
            scal += 0.5;
            if (scal > 3.0) {
                scal = 3.0F;
            }
            fragments.get(currentIndex).updatePhoto((int) (screedWidth * scal), (int) (screenHeight * scal));
        } else if (SMALL_STRING.equals(data)) {
            scal -= 0.5;
            if (scal < 1.0) {
                scal = 1.0F;
            }
            fragments.get(currentIndex).updatePhoto((int) (screedWidth * scal), (int) (screenHeight * scal));
        }

    }

    @Override
    public void connectServerSuccess() {

    }

    @Override
    public void connectServerException() {

    }

    @Override
    public void startConnect() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BlueToothUtils.getIntence().remove(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
