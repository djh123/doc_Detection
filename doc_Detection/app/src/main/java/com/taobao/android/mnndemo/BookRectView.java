package com.taobao.android.mnndemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

public class BookRectView extends View {
    private Paint paint = new Paint();//画笔
    private List<Point> list;
    private Point point;
    private int color = Color.GREEN;
    private int screenW;
    private int screenH;
    private float strokeWidth = 4;
    private float radius = 2;
    private float scaleX = -1;
    private float scaleY = -1;

    public BookRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPoint();
    }

    public BookRectView(Context context) {
        super(context);
        initPoint();
    }

    private void initPoint(){
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        screenW = (int) ScreenUtils.getScreenWidth(getContext());
        screenH = (int) ScreenUtils.getScreenHeight(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d("djh","onDraw");
        if( list != null && !list.isEmpty()){
            Point top_left = list.get(3);
            Point top_right = list.get(1);
            Point bottom_right = list.get(0);
            Point bottom_left = list.get(2);
            scaleToViewSize(top_left);
            scaleToViewSize(top_right);
            scaleToViewSize(bottom_right);
            scaleToViewSize(bottom_left);
            /** 上 **/
            canvas.drawLine(top_left.getX(), top_left.getY(), top_right.getX(), top_right.getY(), paint);
            /** 右 **/
            canvas.drawLine(top_right.getX(), top_right.getY(), bottom_right.getX(), bottom_right.getY(), paint);
            /** 下 **/
            canvas.drawLine(bottom_left.getX(), bottom_left.getY(), bottom_right.getX(), bottom_right.getY(), paint);
            /** 左 **/
            canvas.drawLine(top_left.getX(), top_left.getY(), bottom_left.getX(), bottom_left.getY(), paint);
        }
        if( point != null ){
            scaleToViewSize(point);
//            if(AppManager.getCameraDevice() instanceof V100_Device){
//                canvas.drawCircle(screenW - point.getX(), point.getY(), radius, paint);
//            }else {
//                canvas.drawCircle(point.getX(), point.getY(), radius, paint);
//            }
        }
        super.onDraw(canvas);
    }

    private void scaleToViewSize(Point point){
        if( scaleX < 0 ){
            scaleX = (float)getWidth() / (float)screenW;
        }
        if( scaleY < 0 ){
            scaleY = (float)getHeight() / (float)screenH;
        }
        point.setX((int) (point.getX() * scaleX));
        point.setY((int) (point.getY() * scaleY));
    }

    public void setPoints(List<Point> list){
        this.list = list;
//        Log.d("djh","postInvalidate");
        postInvalidate();
    }

    public void setPoint(Point p){
        this.point = p;
        postInvalidate();
    }

    public void setPoint(int x, int y){
        if( point == null ){
            point = new Point(x, y);
        }else {
            point.setX(x);
            point.setY(y);
        }
        postInvalidate();
    }

    public Point getPoint() {
        return point;
    }

    public void reset(){
        list = null;
        point = null;
        postInvalidate();
    }

    public void setColor(int color){
        this.color = color;
        paint.setColor(color);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        postInvalidate();
    }

    public void setStrokeWidth(int width){
        this.strokeWidth = width;
        paint.setStrokeWidth(width);
    }
}

