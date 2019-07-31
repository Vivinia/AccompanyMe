package com.example.weixu.accompanyme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.adpter.CameraNameAdapter;
import com.example.weixu.adpter.PhotoListAdapter;
import com.example.weixu.table.CameraGridView;
import com.example.weixu.table.CameraPhoto;
import com.example.weixu.table.Dynamic;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PhotoListActivity extends AppCompatActivity implements PhotoListAdapter.OnPhotoClickListener {
    private GridView photoListGrid;
    private SharedPreferences pref;
    private List<String> photoList;
    private PhotoListAdapter adapter;


    private ViewPager viewPager;
    private ImageView ivInsertPhoto;
    private PhotoAdapter pagerAdapter;
    private List<CameraPhoto> cList;
    private TextView textView;
    private List<ImageView> imageTempList;
    private String cameraClass,userEmail;
    private CameraPhoto photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        cameraClass=intent.getStringExtra("CameraClass");
        photoListGrid = findViewById(R.id.photo_list_grid);
        ivInsertPhoto=findViewById(R.id.ivInsertPhoto);
        initData();
    }

    private void initData() {
        photoList = new ArrayList<>();
        pref = getSharedPreferences("userBabyInfo", MODE_PRIVATE);
        userEmail = pref.getString("userParentEmail", "");// 登录的邮箱名
        BmobQuery<CameraPhoto> query = new BmobQuery<>();
        query.addWhereEqualTo("userEmail", userEmail);   //查询当前账号的相册目录
        query.addWhereEqualTo("cameraClass",cameraClass);   //设置当前为该账号第几个相册
        query.findObjects(new FindListener<CameraPhoto>() {
            @Override
            public void done(List<CameraPhoto> list, BmobException e) {
                for(CameraPhoto d :list){
                    String newStr=d.getUserPhoto().getUrl().replaceFirst("bmob-cdn-10503.b0.upaiyun.com","bmob.dustray.cn");

                    d.getUserPhoto().setUrl(newStr);
                }
                cList = list;
                adapter = new PhotoListAdapter(PhotoListActivity.this, list, PhotoListActivity.this);
                photoListGrid.setAdapter(adapter);
            }
        });
        if(cameraClass.equals("1"))
            ivInsertPhoto.setVisibility(View.GONE);
    }


    private void createPopup(int position) {

        View layout = getLayoutInflater().inflate(R.layout.activity_photo_view, null);
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        pop.setBackgroundDrawable(new ColorDrawable(0x000000));//支持点击Back虚拟键退出
        initPopView(layout);
        //setSelect(position);//通过传值获取点击图片位置,从而显示当前图片
        viewPager.setCurrentItem(position);
        textView.setText(position+1 + "/"+cList.size());
        pop.setClippingEnabled(false);
//        pop.showAtLocation(findViewById(R.id.title), Gravity.NO_GRAVITY, 0, 0);
        pop.showAtLocation(viewPager, Gravity.NO_GRAVITY, 0, getStatusBarHeight(this));
    }

    /**
     * 获取状态通知栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //Log.d(TAG, "statusBarHeight:"+frame.top+"px");
        return frame.top;
    }


    private void initPopView(View layout) {
        viewPager = layout.findViewById(R.id.ViewPager);
        textView = layout.findViewById(R.id.text);
        imageTempList = new ArrayList<>();
        pagerAdapter = new PhotoAdapter(this);

        viewPager.setAdapter(pagerAdapter);

        //设置滑动监听事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             //   Toast.makeText(PhotoListActivity.this, "ssssss" + position, Toast.LENGTH_LONG).show();
            }

            //滑动到第几张图片的调用的方法，position当前显示图片位置
            @Override
            public void onPageSelected(int position) {
               // Toast.makeText(PhotoListActivity.this, "11111111111" + position, Toast.LENGTH_LONG).show();
                setSelect(position);
                int s = (position + 1);
                textView.setText(s + "/"+cList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //启动查看图片时，显示你点击图片
    public void setSelect(int i) {
        viewPager.setCurrentItem(i);
        textView.setText(i + "/"+cList.size());
    }

    @Override
    public void onClick(int i) {
        createPopup(i);
    }

    class PhotoAdapter extends PagerAdapter {
        private Context context;

        public PhotoAdapter(Context context) {
            this.context = context;
        }

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
           // Toast.makeText(context, "isViewGromObject"+cList.size(), Toast.LENGTH_LONG).show();
            return cList.size();

            //return Integer.MAX_VALUE;    返回一个比较大的值，目的是为了实现无限轮播
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View view, Object object) {


            //Toast.makeText(context, "isViewGromObject", Toast.LENGTH_LONG).show();
            return view == object;
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，
        // 我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Toast.makeText(context, "instantiateItem" + position, Toast.LENGTH_LONG).show();
            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView .setLayoutParams(layoutParams);
            //imageView.setImageResource(R.drawable.btn_long_red);

            //使图片具有放缩功能
            PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
            mAttacher.update();

            container.addView(imageView);
            Glide.with(context).load(cList.get(position).getUserPhoto().getFileUrl()).into(imageView);
            //imageTempList.add(imageView);
            return imageView;
        }

        //PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           // Toast.makeText(context, "destroyItem" + position, Toast.LENGTH_LONG).show();
            ImageView imageView = (ImageView) object;
            if (imageView == null)
                return;
            container.removeView(imageView);
            // imageTempList.remove(position);
        }


    }

    /**
     * 添加图片
     * @param view
     */
    public void insertPhoto(View view) {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");//图片
        startActivityForResult(galleryIntent,1);//跳转，传递打开相册请求码

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) {
            return;
        }else{
            switch (requestCode){
                case 1:
                    Uri uri=data.getData();
                    String[] images={MediaStore.Images.Media.DATA};//将获取到的
                    Cursor cursor=this.managedQuery(uri,images,null,null,null);
                    int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_uri=cursor.getString(index);
                    /*显示图片*/
                    UpPicture(img_uri);
                    break;
            }
        }

    }

    private void UpPicture(String img_uri) {
        final BmobFile icon=new BmobFile(new File(img_uri));
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    photo=new CameraPhoto();
                    Toast.makeText(PhotoListActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                    photo.setUserPhoto(icon);
                    photo.setCameraClass(cameraClass);
                    photo.setUserEmail(userEmail);
                    photo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Toast.makeText(PhotoListActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                            initData();
                        }
                    });
                }else{
                    Toast.makeText(PhotoListActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
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
