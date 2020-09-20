package com.ozgurberat.foodproject.util;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.request.RequestOptions;
import com.ozgurberat.foodproject.R;

public class GlideHelper {

    private static CircularProgressDrawable createCircularProgressBar(Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    public static RequestOptions setRequestOptions(Context context, boolean isCircle) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions
                .error(R.drawable.no_image_available)
                .placeholder(createCircularProgressBar(context));

        if (isCircle) {
            requestOptions = requestOptions.circleCrop();
        }

        return requestOptions;

    }

}
