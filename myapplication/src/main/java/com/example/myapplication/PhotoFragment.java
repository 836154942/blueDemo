package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by shipinchao on 2018/3/24.
 */

public class PhotoFragment extends Fragment {
    public static final String EXTRA_RESID = "resid";
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_photo, null);
        imageView = (ImageView) view.findViewById(R.id.image);
//        Glide.with(this)
//                .load(getArguments().getInt(EXTRA_RESID))
//                .centerCrop()
//                .into(imageView);
        imageView.setImageResource(getArguments().getInt(EXTRA_RESID));
        return view;
    }

    public void updatePhoto(int width, int height) {

        imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        if (width == 0 && height == 0) {
//            Glide.with(this)
//                    .load(getArguments().getInt(EXTRA_RESID))
//                    .into(imageView);
//            return;
//        }
//        Glide.with(this)
//                .load(getArguments().getInt(EXTRA_RESID))
//                .override(width, height)
//                .into(imageView);
    }
}
