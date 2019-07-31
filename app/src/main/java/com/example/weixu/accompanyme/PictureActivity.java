package com.example.weixu.accompanyme;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.table.VideoPicture;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class PictureActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ibCogVideo;
    private ImageView ivCogPicture;
    SoundPool mSoundPool;     //一般用来播放短音频
    private ArrayList<VideoPicture> vpList = new ArrayList<VideoPicture>();
    private int index = 0;
    private String classIndex="";
    private String videoUrl;
    HashMap<Integer, Integer> map = new HashMap<>();   //创建集合存放数据
    private boolean[] downloadOverFlag;//音频下载完成标志位
    private boolean isDownloadImageOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        Bmob.initialize(this, "96556b6d6dbe89f2ff4a7c1553d882ec");

        initViews();
        bindViews();
        initDatas();
    }
    /*初始化数据*/
    private void initDatas() {
        Bundle bundle=this.getIntent().getExtras();
        classIndex=bundle.getString("class","");
        // Toast.makeText(this,"分类"+classIndex,Toast.LENGTH_SHORT).show();
        BmobQuery<VideoPicture> query = new BmobQuery<VideoPicture>();   //查询Bmob数据库中的信息
        query.addWhereEqualTo("PictureClass",classIndex);
        query.findObjects(new FindListener<VideoPicture>() {
            @Override
            public void done(List<VideoPicture> list, BmobException e) {
                try {
                    if(e==null) {
                        downloadOverFlag = new boolean[list.size()];
                        for(VideoPicture d :list){
                            String newStr=d.getPicture().getUrl().replaceFirst("bmob-cdn-10503.b0.upaiyun.com","bmob.dustray.cn");
                            //newStr+="!fnfx/300x300";
                            d.getPicture().setUrl(newStr);

                            String newStr2=d.getPictureVideo().getUrl().replaceFirst("bmob-cdn-10503.b0.upaiyun.com","bmob.dustray.cn");
                            //newStr+="!fnfx/300x300";
                            d.getPictureVideo().setUrl(newStr2);
                        }
                        for (int i = 0; i < list.size(); i++) {

                            downloadOverFlag[i] = false;
                            vpList.add(list.get(i));   //获取查询到的数据存入内存
                            downVideo(i);
                        }
                        isDownloadImageOver = true;
                        Glide.with(PictureActivity.this).load(vpList.get(0).getPicture().getUrl()).into(ivCogPicture);   //显示图片

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);  //创建音频对象，参数为（可容纳音频个数，声音类型，音频品质默认为0）

    }

    /*下载音频*/
    private void downVideo( int index) {
        final int no = index;
        vpList.get(no).getPictureVideo().download(new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                videoUrl = s;    //保存音频路径
                map.put(no, mSoundPool.load(videoUrl, 100));   //设置第一个音频
                downloadOverFlag[no] = true;
            }

            @Override
            public void onProgress(Integer integer, long l) {
            }
        });
    }

    /*绑定点击事件*/
    private void bindViews() {
        ibCogVideo.setOnClickListener(this);
        ivCogPicture.setOnClickListener(this);
    }

    /*初始化控件*/
    private void initViews() {
        ibCogVideo = findViewById(R.id.ibCogVideo);
        ivCogPicture = findViewById(R.id.ivCogPicture);
    }

    /*点击事件*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibCogVideo:
                if (downloadOverFlag != null && downloadOverFlag[index])
                    mSoundPool.play(map.get(index), 1, 1, 100, 0, 1);  //参数为（要播放的音频，左声道音量，右声道音量，音频优先级，循环次数，速率）
                else
                    Toast.makeText(this, "资源还未加载完成，请稍后", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivCogPicture:
                if (!isDownloadImageOver) {
                    Toast.makeText(this, "资源还未加载完成，请稍后", Toast.LENGTH_SHORT).show();
                    return;
                }
                index++;
                if (index >= vpList.size()) index = 0;
                Glide.with(PictureActivity.this).load(vpList.get(index).getPicture().getUrl()).into(ivCogPicture);
                break;
        }
    }
}
