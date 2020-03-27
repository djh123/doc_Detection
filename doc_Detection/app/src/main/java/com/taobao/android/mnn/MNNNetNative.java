package com.taobao.android.mnn;

import android.graphics.Bitmap;
import android.util.Log;

import com.taobao.android.utils.Common;

public class MNNNetNative {
    // load libraries
    static {
        System.loadLibrary("MNN");
        try {
            System.loadLibrary("MNN_CL");
            System.loadLibrary("MNN_GL");
            System.loadLibrary("MNN_Vulkan");
        } catch (Throwable ce) {
            Log.w(Common.TAG, "load MNN GPU so exception=%s", ce);
        }
        System.loadLibrary("mnncore");
    }
    //doc init
    public static native long nativeDocInit(String modelName);
    public static native long nativeDocDetection(byte[] bufferData,int srcW, int srcH, int srcC,int dstW, int dstH,int dstC,int[] outx, int[] outy,int degrees);
    public static native long nativeDocDetectionRgb(float[] bufferData, int[] outx, int[] outy);
    public static native long nativeDocRelease();

   

}
