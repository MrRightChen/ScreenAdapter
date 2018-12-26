package com.empsun.cd.screenlibrary.layout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.empsun.cd.screenlibrary.UiUtils;


/**
 * Created by Administrator on 2018/12/14/014.
 */

public class AdapterFrameLayout extends FrameLayout {


    private static boolean isFlag = true;

    public AdapterFrameLayout(@NonNull Context context) {
        super(context);
    }

    public AdapterFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                if (layoutParams.width!= LayoutParams.MATCH_PARENT&&layoutParams.width!= LayoutParams.WRAP_CONTENT){
                    layoutParams.width = (int) (layoutParams.width * scaleX);

                }
                if (layoutParams.height!= LayoutParams.MATCH_PARENT&&layoutParams.height!= LayoutParams.WRAP_CONTENT){
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
