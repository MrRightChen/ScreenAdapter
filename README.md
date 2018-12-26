# 前言
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;相信Android开发人员经常遇见的一个问题是屏幕适配。一看到屏幕适配就头疼，不同的手机分辨率导致绘制出来效果不同。从最开始在values文件夹下放置不同分辨率的px资源文件进行适配，到现在百分比布局，以及AutoLayout自动适配的布局，Android适配在局部上任然存在一定局限性。本篇文章将会从源码的角度进行全局的屏幕适配。
#### 使用：
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
dependencies {
                implementation 'com.github.MrRightChen:ScreenAdapter:v1.0.0'
	}
```

#### 原理：
###### Step1:根据UI设计稿的尺寸大小计算出当前使用的设备缩放比例（本工具设计比例是1080X1920,计算出来横向和纵向缩放比例）
```
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
```

###### Step2:更具计算出来的缩放比例自定义布局（备注：这里以RelativeLayout为例）
```
package com.empsun.cd.screenlibrary.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.empsun.cd.screenlibrary.UiUtils;


/**
 * Created by Administrator on 2018/12/14/014.
 */

public class AdapterRelativeLayout extends RelativeLayout {
    private static boolean isFlag = true;

    public AdapterRelativeLayout(Context context) {
        super(context);
    }

    public AdapterRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(isFlag){
            int count = this.getChildCount();
            float scaleX =  UiUtils.getInstance(this.getContext()).getHorizontalScaleValue();
            float scaleY =  UiUtils.getInstance(this.getContext()).getVerticalScaleValue();

            Log.i("","x系数:"+scaleX);
            Log.i("","y系数:"+scaleY);
            for (int i = 0;i < count;i++){
                //获得当前布局的子布局
                View child = this.getChildAt(i);
                //代表的是当前空间的所有属性列表
                LayoutParams layoutParams = ( LayoutParams) child.getLayoutParams();
                int matchParent = LayoutParams.MATCH_PARENT;
                int wrapContent = LayoutParams.WRAP_CONTENT;
                if (layoutParams.width!=LayoutParams.MATCH_PARENT&&layoutParams.width!=LayoutParams.WRAP_CONTENT){
                    layoutParams.width = (int) (layoutParams.width * scaleX);
                }
                if (layoutParams.height!=LayoutParams.MATCH_PARENT&&layoutParams.height!=LayoutParams.WRAP_CONTENT){
                    layoutParams.height = (int) (layoutParams.height * scaleY);
                }
                layoutParams.rightMargin = (int) (layoutParams.rightMargin * scaleX);
                layoutParams.leftMargin = (int) (layoutParams.leftMargin * scaleX);
                layoutParams.topMargin = (int) (layoutParams.topMargin * scaleY);
                layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * scaleY);
            }
            isFlag = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }
}

```

###### Step3:在xml布局文件中直接使用自定义的布局
```
 <com.empsun.cd.screenlibrary.layout.AdapterRelativeLayout
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            >
</com.empsun.cd.screenlibrary.layout.AdapterRelativeLayout>
```
###### Step4:效果对比
分辨率：1080X2032
![123.jpg](https://upload-images.jianshu.io/upload_images/2406435-2b08c349a090b185.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
分辨率：720X1280
![456.jpg](https://upload-images.jianshu.io/upload_images/2406435-537b1815db64c859.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

从上面的对比图可以看到：
1、在1080X2032分辨率手机上：横向宽度一样的，纵向的不一样，做适配区域根据手机实际分辨率自动调整大小。
2、在780X1280分辨率手机上：横向、纵向宽度不一样的，做适配区域根据手机实际分辨率自动调整大小。

存在一个小问题，如果布局中全部都使用这种方式的话，那么在性能上存在一定的消耗，建议在不好做适配的地方，采用这种方式。

目前支持常用的五大布局：![image.png](https://upload-images.jianshu.io/upload_images/2406435-bab81609b91f8047.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果喜欢的话，欢迎点赞！

