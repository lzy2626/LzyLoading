package com.lzy.loadingtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lzy.loading.LoadingDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadingDialog loadingDialog = new LoadingDialog.Builder(MainActivity.this)
                .msg("加载中...")
                .color(R.color.colorPrimary)
                .image(R.drawable.loading_img_anim)
                .gifImage(R.mipmap.loading_gif)
                .build();
        loadingDialog.show();
        loadingDialog.show();
        loadingDialog.show();
        loadingDialog.show();
        loadingDialog.show();
    }
}
