package com.taobao.android.mnndemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taobao.android.mnn.MNNNetNative;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class VideoActivity extends AppCompatActivity {

    private final String TAG = "VideoActivity";

    private String mMobileModelPath = "/sdcard/frozen_model.mnn";
//    private String mMobileModelPath = "/sdcard/train_dir_isTrainFalseChangeNode.mnn";

    private CameraView mCameraView;


    private TextView mFirstResult;
    private TextView mSecondResult;
    private TextView mThirdResult;
    private TextView mTimeTextView;


    HandlerThread mThread;
    Handler mHandle;

    private AtomicBoolean mLockUIRender = new AtomicBoolean(false);
    private AtomicBoolean mDrop = new AtomicBoolean(false);


    private BookRectView mBookeView;
    int mX_coords[] = {0,0,0,0};
    int mY_coords[] = {0,0,0,0};


    /**
     * 监听屏幕旋转
     */
    void detectScreenRotate() {
        OrientationEventListener orientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {

                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;  //手机平放时，检测不到有效的角度
                }

            }
        };


        if (orientationListener.canDetectOrientation()) {
            orientationListener.enable();
        } else {
            orientationListener.disable();
        }
    }



    private void prepareNet() {

        MNNNetNative.nativeDocInit(mMobileModelPath);

        mLockUIRender.set(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        detectScreenRotate();




        mFirstResult = findViewById(R.id.firstResult);
        mSecondResult = findViewById(R.id.secondResult);
        mThirdResult = findViewById(R.id.thirdResult);
        mTimeTextView = findViewById(R.id.timeTextView);

        mBookeView = (BookRectView)findViewById(R.id.bookrect_view);



        // init sub thread handle
        mLockUIRender.set(true);
        clearUIForPrepareNet();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 10);
            } else {
                handlePreViewCallBack();
            }
        } else {
            handlePreViewCallBack();
        }

        mThread = new HandlerThread("MNNNet");
        mThread.start();
        mHandle = new Handler(mThread.getLooper());

        mHandle.post(new Runnable() {
            @Override
            public void run() {
                prepareNet();
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (10 == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handlePreViewCallBack();
            } else {
                Toast.makeText(this, "没有获得必要的权限", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void handlePreViewCallBack() {

        ViewStub stub = (ViewStub) findViewById(R.id.stub);
        stub.inflate();

        mCameraView = (CameraView) findViewById(R.id.camera_view);

        mCameraView.setPreviewCallback(new CameraView.PreviewCallback() {
            @Override
            public void onGetPreviewOptimalSize(int optimalWidth, int optimalHeight) {

                // adjust video preview size according to screen size
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                int fixedVideoHeight = metric.widthPixels * optimalWidth / optimalHeight;

                FrameLayout layoutVideo = findViewById(R.id.videoLayout);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutVideo.getLayoutParams();
                params.height = fixedVideoHeight;
                layoutVideo.setLayoutParams(params);
            }

            @Override
            public void onPreviewFrame(final byte[] data, final int imageWidth, final int imageHeight, final int angle) {
                Log.d("djh", " imageWidth : " + imageWidth + " imageHeight : " + imageHeight);
                if (mLockUIRender.get()) {
                    return;
                }

                if (mDrop.get()) {
                    Log.w(TAG, "drop frame , net running too slow !!");
                } else {
                    mDrop.set(true);
                    mHandle.post(new Runnable() {
                        @Override
                        public void run() {

                            mDrop.set(false);
                            if (mLockUIRender.get()) {
                                return;
                            }

                            final long startTimestamp = System.nanoTime();

                            MNNNetNative.nativeDocDetection(data,imageWidth,imageHeight,3,600,800,4,mX_coords,mY_coords,90);

                            final long endTimestamp = System.nanoTime();
                            final float inferenceTimeCost = (endTimestamp - startTimestamp) / 1000000.0f;
                            Log.d("djh","mX_coords : " + mX_coords[0] + ' ' + mX_coords[1]+ ' ' + mX_coords[2]+ ' ' + mX_coords[3]);
                            Log.d("djh","mY_coords : " + mY_coords[0] + ' ' + mY_coords[1]+ ' ' + mY_coords[2]+ ' ' + mY_coords[3]);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mTimeTextView.setText("cost time：" + inferenceTimeCost + "ms");

                                    List<Point> points = new ArrayList();

                                    Point point_top_left = new Point(mX_coords[0],mY_coords[0]);
                                    Point point_top_right = new Point(mX_coords[1],mY_coords[1]);
                                    Point point_bottom_right = new Point(mX_coords[3],mY_coords[3]);
                                    Point point_bottom_left = new Point(mX_coords[2],mY_coords[2]);


                                    points.add(point_bottom_right);
                                    points.add(point_top_right);
                                    points.add(point_bottom_left);
                                    points.add(point_top_left);

                                    mBookeView.setPoints(points);

                                    mBookeView = (BookRectView)findViewById(R.id.bookrect_view);
                                }
                            });

                        }
                    });
                }
            }
        });
    }



    private void clearUIForPrepareNet() {
        mFirstResult.setText("prepare net ...");
        mSecondResult.setText("");
        mThirdResult.setText("");
        mTimeTextView.setText("");
    }



    @Override
    protected void onPause() {
        mCameraView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.onResume();
    }


    @Override
    protected void onDestroy() {
        mThread.interrupt();

        MNNNetNative.nativeDocRelease();


        super.onDestroy();
    }
}