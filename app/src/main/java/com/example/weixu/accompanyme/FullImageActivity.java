package com.example.weixu.accompanyme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullImageActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                downloadFile(file);
                delayedHide(AUTO_HIDE_DELAY_MILLIS);

            }
            return false;
        }
    };


    private PhotoView largeImage;
    private BmobFile file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_image);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);



        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        largeImage=(PhotoView) findViewById(R.id.iv_large_image);
        largeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        largeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                toggle();
                return false;
            }
        });
        Intent intent = getIntent();
        String url = intent.getStringExtra("imageUrl");
        // Toast.makeText(FullImageActivity.this,url.substring(url.lastIndexOf("/")+1)+url,Toast.LENGTH_LONG).show();
        file = new BmobFile(url.substring(url.lastIndexOf("/")+1),"",url);
        openFile(file);
    }
    private void openFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        //File saveFile = new File(Environment.getExternalStorageDirectory()+"/AccompanyMe/temp", file.getFilename());
        file.download(new DownloadFileListener() {
            @Override
            public void onStart() {
            }
            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    String s = savePath;
                    Uri uri = Uri.fromFile(new File(s));
                    largeImage.setImageURI(uri);

                }else{
                    Toast.makeText(FullImageActivity.this,"加载失败"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                // Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }

    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory()+"/AccompanyMe/photo", file.getFilename());
        file.download(saveFile,new DownloadFileListener() {

            @Override
            public void onStart() {
                //Toast.makeText(FullImageActivity.this,"开始下载...",Toast.LENGTH_LONG).show();
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    String s = savePath;
                    Toast.makeText(FullImageActivity.this,"下载完成",Toast.LENGTH_LONG).show();
                    Uri uri = Uri.fromFile(new File(s));
                    largeImage.setImageURI(uri);

                }else{
                    Toast.makeText(FullImageActivity.this,"下载失败"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                // Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
