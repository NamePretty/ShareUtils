package com.admin.shareproject.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * author: zdj
 * created on: 2019/1/24 09:09
 * description:Toast统一管理类
 */
public class ToastUtil {

    private static Toast mToast;
    private static long nextTimeMillis;
    private static int yOffset;

    private ToastUtil(Context context) {

    }

    public static Toast init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context should not be null!!!");
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            yOffset = mToast.getYOffset();
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, yOffset);
        mToast.setMargin(0, 0);
        return mToast;
    }

    public static void show(Context context, int rid) {
        show(context, context.getResources().getString(rid));
    }

    public static void show(Context context, String content) {
        show(context, content, Gravity.BOTTOM);
    }

    public static void showLong(Context context, String content) {
        show(context, content, Gravity.BOTTOM, Toast.LENGTH_LONG);
    }

    public static void show(Context context, String content, int gravity) {
        show(context, content, gravity, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String content, int gravity, int duration) {
        long current = System.currentTimeMillis();
        if (current < nextTimeMillis) return;
        if (mToast == null) init(context);
        mToast.setText(content);
        mToast.setDuration(duration);
        mToast.setGravity(gravity, 0, yOffset);
        nextTimeMillis = current + (duration == Toast.LENGTH_LONG ? 3500 : 2000);
        mToast.show();
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}

