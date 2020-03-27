package com.taobao.android.mnndemo;

import android.content.Context;

public class Point {

    private int x = -1;
    private int y = -1;
    private int score;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void scaleToDataSize(Context context, int dataW, int dataH){
        float screenW = ScreenUtils.getScreenWidth(context);
        float screenH = ScreenUtils.getScreenHeight(context);

        float imageScaleX = (float) dataW / screenW;
        float imageScaleY = (float) dataH / screenH;

        setX((int) (getX() * imageScaleX));
        setY((int) (getY() * imageScaleY));
    }

    public Point scaleToScreenSize(Context context, int width, int height){
        float screenW = ScreenUtils.getScreenWidth(context);
        float screenH = ScreenUtils.getScreenHeight(context);
        float imageScaleX, imageScaleY;
        imageScaleX = Math.min(width, height) / Math.min(screenW, screenH);
        imageScaleY = Math.max(width, height) / Math.max(screenW, screenH);
        setX((int) (getX() / imageScaleX));
        setY((int) (getY() / imageScaleY));
        return this;
    }
}
