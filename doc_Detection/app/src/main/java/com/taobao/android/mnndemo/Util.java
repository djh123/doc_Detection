package com.taobao.android.mnndemo;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static void saveFrameData(final byte[] yuvData, String headName, final int width, final int height, final int cursorX, final int cursorY, final String format, final int cameraId){
        String debugYPath = Environment.getExternalStorageDirectory().getPath() + "/config/PointAndAsk" + "/p_data/" + headName + "_" + cameraId + "_" + cursorX + "_" + cursorY + "_" + width + "_" + height + format;
        File debugFile = new File(debugYPath);
        try{
            debugFile.getParentFile().mkdirs();
            if(debugFile.exists()){
                debugFile.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(debugFile);
            fileOutputStream.write(yuvData);
            fileOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String saveFrameData(Bitmap bitmap, String headName, final int cursorX, final int cursorY, final String format, final int cameraId){
        String debugYPath = Environment.getExternalStorageDirectory().getPath() + "/" + headName + "_" + cameraId + "_" + cursorX + "_" + cursorY + "_" + bitmap.getWidth() + "_" + bitmap.getHeight() + format;
        File debugFile = new File(debugYPath);
        try{
            debugFile.getParentFile().mkdirs();
            if( debugFile.exists() ){
                debugFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(debugYPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return debugYPath;
    }

    public static String getSaveHeadName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
}
