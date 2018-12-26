package com.ly.chen.screenadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.empsun.cd.screenlibrary.layout.AdapterRelativeLayout;
import com.empsun.cd.screenlibrary.layout.AdapterTableLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mSrceen =(TextView) findViewById(R.id.screen);
        final TextView noAdapterHeight =(TextView) findViewById(R.id.noAdapterHeight);
        final TextView adapterHeight_tv =(TextView) findViewById(R.id.adapterHeight);
        mSrceen.setText("分辨率："+getWindowWidth(this)+"X"+getWindowHeigh(this));
        noAdapterHeight.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noAdapterHeight.setText("最右边灰色区域高度："+noAdapterHeight.getHeight());
                    }
                });
            }
        });
        adapterHeight_tv.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterHeight_tv.setText("最右边灰色区域高度："+adapterHeight_tv.getHeight());
                    }
                });
            }
        });
    }
        public  int getWindowWidth(Context context){
            // 获取屏幕分辨率
            WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            int mScreenWidth = dm.widthPixels;
            return mScreenWidth;
        }
        public  int getWindowHeigh(Context context){
            // 获取屏幕分辨率
            WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            int mScreenHeigh = dm.heightPixels;
            return mScreenHeigh;
        }


}
