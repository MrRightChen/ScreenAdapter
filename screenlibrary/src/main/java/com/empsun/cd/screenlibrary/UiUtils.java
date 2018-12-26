package com.empsun.cd.screenlibrary;

/**
 * Created by Administrator on 2018/12/14/014.
 */

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 获得缩放系数
 * 假定Ui设计为1920 * 1080
 */
public class UiUtils {

    private Context mContext;
    private static UiUtils mInstance;
    //UI 设计尺寸
    public final float STANDARD_WIDTH = 1080f;
    public final float STANDARD_HEIGHT = 1920f;
    private final String DIMEN_CLASS = "com.android.internal.R$dimen";

    //当前设备实际宽高
    public float mDisplayMetricsWidth ;
    public float mDisplayMetricsHeight ;
    //单例模式
    public static UiUtils getInstance(Context context){
        if (mInstance==null){
            mInstance = new UiUtils(context);
        }
        return mInstance;
    }

    //私有构造方法
    private UiUtils(Context mContext) {
        this.mContext = mContext;
        //拿到窗口管理器
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //加载当前界面信息
        DisplayMetrics displayMetrics =new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        if (mDisplayMetricsWidth == 0.0f || mDisplayMetricsHeight == 0.0f){

            //获取状态栏的高度
            int systemBarHeight = getValue(mContext,"system_bar_height",48);
            if(displayMetrics.widthPixels > displayMetrics.heightPixels){
                this.mDisplayMetricsWidth = displayMetrics.heightPixels;
                this.mDisplayMetricsHeight = displayMetrics.widthPixels - systemBarHeight;
            }else{
                this.mDisplayMetricsWidth = displayMetrics.widthPixels;
                this.mDisplayMetricsHeight = displayMetrics.heightPixels - systemBarHeight;
            }
        }
    }
    //获取状态栏高度
    public int getValue(Context context,String systemid,int defValue) {

        try {
            Class<?> clazz = Class.forName(DIMEN_CLASS);
            Object r = clazz.newInstance();
            Field field = clazz.getField(systemid);
            int x = (int) field.get(r);
            return context.getResources().getDimensionPixelOffset(x);

        } catch (Exception e) {
            return defValue;
        }
    }
    //获取水平的缩放系数
    public float getHorizontalScaleValue(){
        return mDisplayMetricsWidth / STANDARD_WIDTH;
    }

    //获取垂直缩放系数
    public float getVerticalScaleValue(){

        Log.i("","displayMetricsHeight:"+mDisplayMetricsHeight);
        return mDisplayMetricsHeight / STANDARD_HEIGHT;
    }

}
