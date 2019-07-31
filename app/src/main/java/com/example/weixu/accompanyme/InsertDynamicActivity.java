package com.example.weixu.accompanyme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.weixu.table.CameraPhoto;
import com.example.weixu.table.Dynamic;

import java.io.ByteArrayInputStream;
import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class InsertDynamicActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private ImageView ivDynamicPicture;
    private String userBabyName;
    private EditText etDynamicContent;
    private Dynamic dynamic;
    private Intent intent;
    private Button btDynamicSubmit;
    private CameraPhoto photo;

    private boolean isUploadOver=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_dynamic);
        Bmob.initialize(this, "96556b6d6dbe89f2ff4a7c1553d882ec");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        ivDynamicPicture = (ImageView) findViewById(R.id.ivDynamicPicture);
        etDynamicContent = (EditText) findViewById(R.id.etDynamicContent);
        btDynamicSubmit= (Button) findViewById(R.id.btDynamicSubmit);
    }

    public void InsertDynamicOnclick(View view){
        switch (view.getId()){
            case R.id.ivDynamicPicture:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");// 图片
                startActivityForResult(intent, 1); // 跳转，传递打开相册请求码
                break;
            case R.id.btDynamicSubmit:
                submit();
                break;
        }
    }
    // 向服务器保存数据
    private void submit() {
        String dynamicContent = etDynamicContent.getText().toString();
        // 获得SharedPreferences对象
        pref = getSharedPreferences("userBabyInfo", MODE_PRIVATE);
        String dynamicUserName = pref.getString("userBabyName", "");
        String dynamicUserEmail=pref.getString("userParentEmail","");// 登录的邮箱名
        if (dynamicContent.equals("")) {
            Toast.makeText(InsertDynamicActivity.this, "输入内容不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        /*将图片保存到动态圈*/
        photo.setCameraClass("1");
        photo.setUserEmail(dynamicUserEmail);
        photo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Toast.makeText(InsertDynamicActivity.this, "动态圈图片保存成功",
                        Toast.LENGTH_SHORT).show();
            }
        });
        dynamic.setDynamicUserName(dynamicUserName);
        dynamic.setDynamicContent(dynamicContent);
        dynamic.setUserEmail(dynamicUserEmail);
        dynamic.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(InsertDynamicActivity.this, "发表成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case 1:
                    //btDynamicSubmit.setBackgroundResource(R.drawable.btn_long_white);
                    btDynamicSubmit.setClickable(false);
                    Uri uri = data.getData();
                    // 将获取到的uri转换为String型
                    String[] images = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
                    Cursor cursor = this
                            .managedQuery(uri, images, null, null, null);
                    int index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_url = cursor.getString(index);
                    //显示图片
                    showPicture(img_url);
                    //图片上传
                    upload(img_url);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //显示图片
    private void showPicture(String imgpath) {
        ivDynamicPicture.setImageBitmap(BitmapFactory.decodeFile(imgpath));
        ivDynamicPicture.setPadding(0,0,0,0);
        ivDynamicPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //图片上传
    private void upload(String imgpath){
        final BmobFile icon=new BmobFile(new File(imgpath));

    //    Toast.makeText(InsertDynamicActivity.this,"图片上传路径"+icon.getFilename(),Toast.LENGTH_SHORT).show();
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    dynamic = new Dynamic();
                    Toast.makeText(InsertDynamicActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                    dynamic.setDynamicPicture(icon);
                    btDynamicSubmit.setEnabled(true);
                    btDynamicSubmit.setClickable(true);
                    btDynamicSubmit.setBackgroundResource(R.drawable.xml_btn_color_accent);
                    photo=new CameraPhoto();   //创建动态圈表，将动态中的图片保存
                    photo.setUserPhoto(icon);
                }else{
                    Toast.makeText(InsertDynamicActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
