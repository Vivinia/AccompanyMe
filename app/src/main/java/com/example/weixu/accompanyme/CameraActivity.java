package com.example.weixu.accompanyme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.weixu.adpter.CameraNameAdapter;
import com.example.weixu.table.CameraGridView;
import com.example.weixu.table.CameraPhoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class CameraActivity extends AppCompatActivity {

    private GridView gvCamera;
    private CameraNameAdapter mAdapter;
    private SharedPreferences pref;
    private String userEmail;
    private List<Map<String, Object>> dataList;
    private String strCameraName;
    private EditText etInputCameraName;
    private CameraGridView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bmob.initialize(CameraActivity.this, "96556b6d6dbe89f2ff4a7c1553d882ec");
        gvCamera = findViewById(R.id.gvCamera);

        initData();
    }

    private void initData() {
        dataList = new ArrayList<Map<String, Object>>();
        pref = getSharedPreferences("userBabyInfo", MODE_PRIVATE);
        userEmail = pref.getString("userParentEmail", "");// 登录的邮箱名
        BmobQuery<CameraGridView> query = new BmobQuery<CameraGridView>();
        query.addWhereEqualTo("userEmail", userEmail);   //查询当前账号的相册目录
        query.findObjects(new FindListener<CameraGridView>() {
            @Override
            public void done(List<CameraGridView> list, BmobException e) {
//                for(CameraGridView d :list){
//                    String newStr=d.getCameraPhoto().getUrl().replaceFirst("bmob-cdn-10503.b0.upaiyun.com","bmob.dustray.cn");
//
//                    d.getCameraPhoto().setUrl(newStr);
//                }
                mAdapter = new CameraNameAdapter(CameraActivity.this, list);
                gvCamera.setAdapter(mAdapter);
            }
        });

    }

    /*添加相册*/
    public void InsertCamera(View view) {
        LayoutInflater lf = (LayoutInflater) CameraActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) lf.inflate(R.layout.activity_insert_camera, null);
        etInputCameraName = vg.findViewById(R.id.etInputCameraName);
        new AlertDialog.Builder(CameraActivity.this)
                .setView(vg)
                .setTitle("新建相册")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (etInputCameraName.getText() == null || etInputCameraName.getText().toString().equals("")) {
                                    Toast.makeText(CameraActivity.this, "请输入相册名称", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                strCameraName = etInputCameraName.getText().toString();
                                InsertNewCamera();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    /*根据相册名创建相册*/
    public void InsertNewCamera() {
        //upload("camera.jpg");   //上传封面
        createCamera();
    }

    //图片上传
    private void upload(String imgpath) {
        copyAssetAndWrite(imgpath);
        final File dataFile = new File(getCacheDir(), imgpath);
        final BmobFile icon = new BmobFile(dataFile);
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    camera = new CameraGridView();
                    Toast.makeText(CameraActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                    camera.setCameraPhoto(icon);
                    createCamera();
                } else {
                    Toast.makeText(CameraActivity.this, "图片上传失败" + e.toString() + "///" + dataFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createCamera() {
        int num = gvCamera.getCount();
        camera = new CameraGridView();
        camera.setCameraClass(((num + 1) + ""));
        camera.setCameraName(strCameraName);
        camera.setUserEmail(userEmail);
        camera.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(CameraActivity.this, "相册创建成功",
                            Toast.LENGTH_SHORT).show();
                    initData();
                    //finish();
                }
            }
        });

    }

    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(String fileName) {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return true;
                }
            }
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
