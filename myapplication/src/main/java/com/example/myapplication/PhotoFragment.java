package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        imageView.setImageResource(getArguments().getInt(EXTRA_RESID));
        return view;
    }
}
