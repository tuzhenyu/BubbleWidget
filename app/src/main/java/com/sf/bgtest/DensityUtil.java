package com.sf.bgtest;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DensityUtil {

    public static float pixel2Dp(Resources resources, int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value,
                resources.getDisplayMetrics());
    }

    public static float dp2Pixel(Resources resources, int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                resources.getDisplayMetrics());
    }

    public static float dp2Pixel(Resources resources, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                resources.getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
