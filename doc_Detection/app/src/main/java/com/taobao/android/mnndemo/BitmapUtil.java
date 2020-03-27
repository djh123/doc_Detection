package com.taobao.android.mnndemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapUtil {
    private static final String TAG = "BitmapUtil";

    public BitmapUtil() {
    }

    public static Bitmap Bytes2Bitmap(byte[] b) {
        return b.length != 0 ? BitmapFactory.decodeByteArray(b, 0, b.length) : null;
    }

    public static boolean isJpegFile(byte[] buffer) {
        return "FFD8FF".equals(bytesToHexString(buffer));
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src != null && src.length > 0) {
            for(int i = 0; i < 3; ++i) {
                String hv = Integer.toHexString(src[i] & 255).toUpperCase();
                if (hv.length() < 2) {
                    builder.append(0);
                }

                builder.append(hv);
            }

            return builder.toString();
        } else {
            return null;
        }
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, int orientationDegree, boolean recycle) {
        if (bm != null && !bm.isRecycled()) {
            Matrix matrix = new Matrix();
            matrix.postRotate((float)orientationDegree);
            int width = bm.getWidth();
            int height = bm.getHeight();
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            if (recycle && bm != bm1) {
                bm.recycle();
                bm = null;
            }

            return bm1;
        } else {
            return null;
        }
    }

    public static Bitmap adjustPhotoFlip(Bitmap bm, boolean recycle) {
        if (bm != null && !bm.isRecycled()) {
            Matrix matrix = new Matrix();
            matrix.setScale(-1.0F, 1.0F);
            int width = bm.getWidth();
            int height = bm.getHeight();
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            if (recycle && bm != bm1) {
                bm.recycle();
                bm = null;
            }

            return bm1;
        } else {
            return null;
        }
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

    }

    public static void saveBitmap(Bitmap mBitmap, String savePath, int quality) {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            File file = new File(savePath);
            if (file.exists()) {
                file.delete();
            }

            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            File f = new File(savePath);
            FileOutputStream fOut = null;

            try {
                fOut = new FileOutputStream(f);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
            } catch (FileNotFoundException var17) {
                var17.printStackTrace();
            } catch (Exception var18) {
                var18.printStackTrace();
            } finally {
                try {
                    if (fOut != null) {
                        fOut.flush();
                    }

                    if (fOut != null) {
                        fOut.close();
                    }
                } catch (IOException var16) {
                    var16.printStackTrace();
                }

            }

        } else {
            Log.i("BitmapUtil", "saveBitmap bitmap is null or mBitmap isRecycled");
        }
    }

    public static void saveBitmap(Bitmap mBitmap, String savePath) {
        saveBitmap(mBitmap, savePath, 100);
    }

    public static void saveFileByByte(byte[] bytes, String path) {
        File file = new File(path);
        FileOutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            if (file.exists()) {
                file.delete();
            }

            file.getParentFile().mkdirs();
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception var16) {
                    var16.printStackTrace();
                }
            }

        }

    }

    public static Bitmap getBitmapByPath(String path) {
        return isFileExist(path) ? BitmapFactory.decodeFile(path) : null;
    }

    public static boolean isFileExist(String aFilePath) {
        File mFile = null;
        boolean mIsExist = false;
        if (checkFileSystemIsOk() && null != aFilePath) {
            mFile = new File(aFilePath);
            mIsExist = mFile.exists();
        }

        return mIsExist;
    }

    private static boolean checkFileSystemIsOk() {
        boolean mIsOk = false;
        if (Environment.getExternalStorageState().equals("mounted")) {
            mIsOk = true;
        }

        return mIsOk;
    }

    public static Bitmap scale(Bitmap bitmap, float scale, boolean recycle) {
        if (bitmap == null) {
            return null;
        } else if (scale == 1.0F) {
            return bitmap;
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (recycle) {
                bitmap.recycle();
            }

            return resizeBmp;
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        return Bitmap2Bytes(bm, 100);
    }

    public static Bitmap InputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bm == null) {
            return null;
        } else if (bm.isRecycled()) {
            return null;
        } else {
            bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            return baos.toByteArray();
        }
    }

    public static Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = null;

        try {
            Bitmap cache = view.getDrawingCache();
            if (null != cache && !cache.isRecycled()) {
                bitmap = Bitmap.createBitmap(cache);
            }
        } catch (OutOfMemoryError var7) {
            var7.printStackTrace();
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }

        return bitmap;
    }



    /***
     * YUV420 转化成 RGB
     */
    public static int[] decodeYUV420SP(byte[] yuv420sp, int width, int height)
    {
        final int frameSize = width * height;
        int rgb[] = new int[frameSize];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                        | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
        return rgb;
    }

    public static Bitmap getBitmapByYUV420(byte[] yuv420sp, int imageWidth, int imageHeight){

        int rgb[] = decodeYUV420SP(yuv420sp, imageWidth, imageHeight);
//        Matrix matrix = new Matrix();
//        matrix.postScale(-1, 1);// 使用后乘
//        matrix.setRotate(270, (float) imageWidth/ 2, (float) imageHeight / 2);
        Bitmap bitmap2 = Bitmap.createBitmap(rgb, 0, imageWidth, imageWidth, imageHeight,
                android.graphics.Bitmap.Config.ARGB_8888);

//        Bitmap newBM = Bitmap.createBitmap(bitmap2, 0, 0, imageWidth, imageHeight, matrix, false);

        return  bitmap2;
    }

    public static byte[] getYUVByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return new byte[0];
        } else {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int size = width * height;
            int[] pixels = new int[size];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            return rgb2YCbCr420(pixels, width, height);
        }
    }

    public static byte[] rgb2YCbCr420(int[] pixels, int width, int height) {
        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                int rgb = pixels[i * width + j] & 16777215;
                int r = rgb & 255;
                int g = rgb >> 8 & 255;
                int b = rgb >> 16 & 255;
                int y = (66 * r + 129 * g + 25 * b + 128 >> 8) + 16;
                int u = (-38 * r - 74 * g + 112 * b + 128 >> 8) + 128;
                int v = (112 * r - 94 * g - 18 * b + 128 >> 8) + 128;
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                yuv[i * width + j] = (byte)y;
                yuv[len + (i >> 1) * width + (j & -2) + 0] = (byte)u;
                yuv[len + (i >> 1) * width + (j & -2) + 1] = (byte)v;
            }
        }

        return yuv;
    }

    public static Bitmap toGrayScale(Bitmap bmpOriginal, boolean recycle) {
        int height = bmpOriginal.getHeight();
        int width = bmpOriginal.getWidth();
        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0F);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0.0F, 0.0F, paint);
        if (recycle) {
            recycleBitmap(bmpOriginal);
        }

        return bmpGrayScale;
    }

    public static boolean copyFile(String oldPath, String newPath) {
        try {
            File file = new File(newPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            File oldfile = new File(oldPath);
            if (!oldfile.exists()) {
                return false;
            } else {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[512];

                int byteread;
                while((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }

                inStream.close();
                fs.close();
                return true;
            }
        } catch (Exception var8) {
            System.out.println("复制单个文件操作出错");
            var8.printStackTrace();
            return false;
        }
    }

    public static void convertToJpg(String pngFilePath, String jpgFilePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(pngFilePath);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jpgFilePath));
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
                bos.flush();
            }

            bos.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            bitmap.recycle();
            bitmap = null;
        }

        if (!pngFilePath.equals(jpgFilePath)) {
            File oldImg = new File(pngFilePath);
            oldImg.delete();
        }

    }

    public static Bitmap getBitmapScale(Bitmap srcBitmap, int size) {
        long start = System.currentTimeMillis();
        float srcWidth = (float)srcBitmap.getWidth();
        float srcHeight = (float)srcBitmap.getHeight();
        float inSampleSize = 1.0F;
        if (srcHeight > (float)size || srcWidth > (float)size) {
            if (srcWidth < srcHeight) {
                inSampleSize = (float)size / srcHeight;
            } else {
                inSampleSize = (float)size / srcWidth;
            }
        }

        Bitmap scaleBitmap = scale(srcBitmap, inSampleSize, false);
//        DLog.e("BitmapUtil", "scale cost = " + (System.currentTimeMillis() - start));
        return scaleBitmap;
    }

    public static Bitmap drawPointOnBitmap(Bitmap srcBitmap, int x, int y) {
        return drawPointOnBitmap(srcBitmap, x, y, -65536);
    }

    public static Bitmap drawPointOnBitmap(Bitmap srcBitmap, int x, int y, int color) {
        Canvas canvas = new Canvas(srcBitmap);
        canvas.drawBitmap(srcBitmap, 0.0F, 0.0F, (Paint)null);
        int width = 8;
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float)(x - width), (float)(y - width), (float)width, paint);
        return srcBitmap;
    }

    public static Bitmap drawRectOnBitmap(Bitmap srcBitmap, Rect rect) {
        return drawRectOnBitmap(srcBitmap, rect, -16711936);
    }

    public static Bitmap drawRectOnBitmap(Bitmap srcBitmap, Rect rect, int color) {
        Canvas canvas = new Canvas(srcBitmap);
        canvas.drawBitmap(srcBitmap, 0.0F, 0.0F, (Paint)null);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6.0F);
        canvas.drawRect(rect, paint);
        return srcBitmap;
    }

    public static Bitmap compressQuality(Bitmap bitmap, int quality, boolean recycle) {
        long start = System.currentTimeMillis();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        byte[] bytes = bos.toByteArray();
        if (recycle) {
            bitmap.recycle();
        }

        Bitmap result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        DLog.e("BitmapUtil", "compress cost = " + (System.currentTimeMillis() - start));
        return result;
    }

    public static String getSaveHeadName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
}
