
### 前言
 将等待加载框进行集成，可以根据不同的需求，显示不同的等待加载框。Github地址：[[https://github.com/lzy2626/LzyLoading](https://github.com/lzy2626/LzyLoading)
]([https://github.com/lzy2626/LzyLoading](https://github.com/lzy2626/LzyLoading)
)
### 效果
![设置了gif图显示](https://upload-images.jianshu.io/upload_images/11207183-a8362ab381642280.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)

### 使 用

[![](https://jitpack.io/v/lzy2626/LzyLoading.svg)](https://jitpack.io/#lzy2626/LzyLoading)

To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.** Add the dependency

```
	dependencies {
	        implementation 'com.github.lzy2626:LzyLoading:v1.0'
	}

```
**Step3.** 代码调用

```
       new LoadingDialog.Builder(MainActivity.this)
                .msg("加载中...")
                .color(R.color.colorPrimary)
                .image(R.drawable.loading_dialog_progressbar)
                .gifImage(com.lzy.loading.R.mipmap.num86)
                .build()
                .showDialogForLoading(MainActivity.this).show();
```

### 实现
**1.核心代码**
```
  /**
     * 三种加载样式：1.系统默认图，可设置颜色 2.自定义图片 3.Gif
     * 比重递减：
     * gif有数据，自定义有数据，显示gif
     * gif无数据，自定义有数据，显示自定义图片
     * gif无数据，自定义无数据，显示系统样式
     */
    public Dialog showDialogForLoading(Activity context) {
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

```
**2.参数**
```
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
```
