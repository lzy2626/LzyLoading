package com.lzy.loading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;


/**
 * 弹窗浮动加载进度条
 */
public class LoadingDialog {


    /**
     * 提示语
     */
    private String msg;
    private int msgColor;
    /**
     * 是否可取消
     */
    private boolean cancelable;
    /**
     * 点击dialog以外的区域是否关闭
     */
    private boolean canceledOnTouchOutside;
    /**
     * 菊花的颜色
     */
    private int color;
    /**
     * 背景色
     */
    private int background;

    /**
     * image图片
     */
    private int image;
    /**
     * gif图片
     */
    private int gifImage;
    /**
     *宽高
     */
    private int width;
    private int height;

    private Context context;

    /**
     * 加载数据对话框
     */
    private Dialog mLoadingDialog;

    private LoadingDialog(Builder builder) {
        msg = builder.msg;
        msgColor = builder.msgColor;
        cancelable = builder.cancelable;
        canceledOnTouchOutside = builder.canceledOnTouchOutside;
        color = builder.color;
        background = builder.background;
        image = builder.image;
        gifImage = builder.gifImage;
        width = builder.width;
        height = builder.height;
        context = builder.mContext;
    }


    /**
     * 三种加载样式：1.系统默认图，可设置颜色 2.自定义图片 3.Gif
     * 比重递减：
     * gif有数据，自定义有数据，显示gif
     * gif无数据，自定义有数据，显示自定义图片
     * gif无数据，自定义无数据，显示系统样式
     */
    public Dialog showDialogForLoading() {
        View view;
        if (gifImage != -1) {//设置了gif
            view = LayoutInflater.from(context).inflate(R.layout.dialog_loadinggif, null);
            GifImageView gifImageView = view.findViewById(R.id.gifimageview);
            gifImageView.setImageResource(gifImage);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
            ProgressBar progressBar = view.findViewById(R.id.progressbar);
            LinearLayout llProgress = view.findViewById(R.id.ll_progress);

            //设置背景色
            if (background != -1) {
                llProgress.setBackgroundColor(context.getResources().getColor(background));
            }


            if (image != -1) {//设置了image图片，没有设置的话，使用系统样式
                Drawable wrapDrawable = DrawableCompat.wrap(context.getResources().getDrawable(image));
                progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
            } else {//使用系统默认图片
                //使用系统样式可以设置样式颜色
                if (color != -1) {
                    //21以上处理方式和21以下版本的处理方式不同
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);

                    } else {
                        Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
                        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color));
                        progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
                    }
                }

            }


        }
        TextView loadingText = view.findViewById(R.id.id_tv_loading_dialog_text);
        //设置提示语
        if (TextUtils.isEmpty(msg)) {
            loadingText.setVisibility(View.GONE);
        } else {
            loadingText.setVisibility(View.VISIBLE);
            loadingText.setText(msg);
            if (msgColor != -1)
                loadingText.setTextColor(context.getResources().getColor(msgColor));
        }

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        Window window = mLoadingDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.width = height;
        window.setAttributes(params);

        mLoadingDialog.show();
        return mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public void cancelDialogForLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
    }

    public static final class Builder {

        //设置默认值
        private String msg;
        private int msgColor = -1;
        private boolean cancelable = true;
        private boolean canceledOnTouchOutside = true;
        private int color = -1;
        private int background = -1;
        private int image = -1;
        private int gifImage = -1;
        private int width = -1;
        private int height = -1;
        private Context mContext;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder() {
        }

        public Builder msg(String val) {
            msg = val;
            return this;
        }

        public Builder msgColor(int val) {
            msgColor = val;
            return this;
        }

        public Builder cancelable(boolean val) {
            cancelable = val;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean val) {
            canceledOnTouchOutside = val;
            return this;
        }

        public Builder color(int val) {
            color = val;
            return this;
        }

        public LoadingDialog build() {
            return new LoadingDialog(this);
        }

        public Builder background(int val) {
            background = val;
            return this;
        }

        /**
         * 设置圆形图
         *
         * @param val
         * @return
         */
        public Builder image(int val) {
            image = val;
            return this;
        }

        /**
         * 设置gif图片
         *
         * @param val
         * @return
         */
        public Builder gifImage(int val) {
            gifImage = val;
            return this;
        }

        /**
         * 设置宽
         *
         * @param dp
         * @return 转成px
         */
        public Builder width(int dp) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
            return this;
        }

        /**
         * 设置高
         *
         * @param dp
         * @return 转成px
         */
        public Builder height(int dp) {
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
            return this;
        }

    }
}
