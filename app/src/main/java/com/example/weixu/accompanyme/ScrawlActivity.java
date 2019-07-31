package com.example.weixu.accompanyme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ScrawlActivity extends AppCompatActivity {
    private ImageView iv;
    private Bitmap baseBitmap;
    private Canvas canvas;
    private Paint paint;
    int xx, yy, yyyy, xxxx;
    int startX;
    int startY;

    private String[] wideList={"5","10","15","20","25","30"};//单选列表
    private String[] colorList={"红","黄","绿","蓝","青","灰","黑"};//单选列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrawl);
        init();

        //触摸方法
        touch();
    }
    //触摸方法
    private void touch() {
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取手按下时的坐标
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取手移动后的坐标
                        int stopX = (int) event.getX();
                        int stopY = (int) event.getY();
                        // 在开始和结束坐标间画一条线
                        xx = startX;
                        yy = startY;
                        xxxx = stopX;
                        yyyy = stopY;
                        Drows(event);
                        break;
                }
                return true;
            }
        });
    }
    private void Drows(MotionEvent event) {
        canvas.drawLine(xx, yy, xxxx, yyyy, paint);
        startX = (int) event.getX();
        startY = (int) event.getY();
        iv.setImageBitmap(baseBitmap);
    }

    private void init() {
        iv = (ImageView) findViewById(R.id.iv);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int high = outMetrics.heightPixels;

        // 创建一张空白图片
        baseBitmap = Bitmap.createBitmap(width, high, Bitmap.Config.ARGB_8888);
        // 创建一张画布
        canvas = new Canvas(baseBitmap);
        // 画布背景为白色
        canvas.drawColor(Color.WHITE);
        // 创建画笔
        paint = new Paint();
        // 画笔颜色为红色
        paint.setColor(Color.RED);
        // 宽度5个像素
        paint.setStrokeWidth(5);
        // 先将白色背景画上
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);
        iv.setImageBitmap(baseBitmap);

        //定义路径类，用于保存
        class DrawPath {
            public Path mpath;// 路径
            public Paint mpaint;// 画笔
        }
    }
    public void clickBt(View view) {
        switch (view.getId()){
            case R.id.btSave:
                save();  //保存
                break;
            case R.id.btClean:
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(baseBitmap, new Matrix(), paint);
                iv.setImageBitmap(baseBitmap);
                break;
            case R.id.btChangeColor:
                changeColor();
                break;
            case R.id.btChangeWide:
                changeWide();
                break;
        }
    }

    private void changeColor() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(colorList,-1,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int i) {
                String color=colorList[i];
                switch (color){
                    case "红":
                        paint.setColor(Color.RED);
                        break;
                    case "黄":
                        paint.setColor(Color.YELLOW);
                        break;
                    case "绿":
                        paint.setColor(Color.GREEN);
                        break;
                    case "蓝":
                        paint.setColor(Color.BLUE);
                        break;
                    case "青":
                        paint.setColor(Color.CYAN);
                        break;
                    case "灰":
                        paint.setColor(Color.GRAY);
                        break;
                    case "黑":
                        paint.setColor(Color.BLACK);
                        break;
                }
                dialog.dismiss();//关闭对话框
            }
        });
        /*添加对话框中取消按钮点击事件*/
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();//关闭对话框
            }
        });
        AlertDialog dialog=builder.create();//获取dialog
        dialog.show();//显示对话框  
    }

    private void changeWide() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(wideList,-1,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int i) {
                String wide=wideList[i];
                paint.setStrokeWidth(Integer.parseInt(wide));
                dialog.dismiss();//关闭对话框
            }
        });
        /*添加对话框中取消按钮点击事件*/
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();//关闭对话框
            }
        });
        AlertDialog dialog=builder.create();//获取dialog
        dialog.show();//显示对话框  
    }

    private void save() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "Pictures/"+System.currentTimeMillis() + ".jpg");
            OutputStream stream = new FileOutputStream(file);
            baseBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            // 模拟一个广播，通知系统sdcard被挂载
           // MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
            Toast.makeText(this, "保存图片成功", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "保存图片失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
}
