package com.taobao.android.mnndemo;

import android.content.Context;
import android.view.WindowManager;

public class ScreenUtils {

    public ScreenUtils() {
    }

    public static float[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService("window");
        float SCREEN_WIDTH = (float)wm.getDefaultDisplay().getWidth();
        float SCREEN_HEIGHT = (float)wm.getDefaultDisplay().getHeight();
        return new float[]{SCREEN_WIDTH, SCREEN_HEIGHT};
    }

    public static float getScreenWidth(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService("window");
        float SCREEN_WIDTH = (float)wm.getDefaultDisplay().getWidth();
        return SCREEN_WIDTH;
    }

    public static float getScreenHeight(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService("window");
        float SCREEN_HEIGHT = (float)wm.getDefaultDisplay().getHeight();
        return SCREEN_HEIGHT;
    }
}
