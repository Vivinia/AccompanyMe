package com.example.weixu.accompanyme;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.table.User;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EntertainmentFragment.OnFragmentInteractionListener, View.OnClickListener {

    private SharedPreferences pref;
    private Editor editor;
    private AppCompatImageButton acibEntertainment, acibDynamic, acibIntelligence;
    private EntertainmentFragment entertainmentFragment;
    private DynamicFragment dynamicFragment;
    private IntelligenceFragment intelligenceFragment;
    private String userBabyName, babySex, imgUrl, userParentEmail, objectId;
    private int babyAge;
    private TextView tvUserBabyName, tvBabySex, tvBabyAge;
    private NavigationView navigationView;
    private ImageView ivUserBabyHead, ivCleanUserInfo;
    private Intent intent;
    private User user;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "96556b6d6dbe89f2ff4a7c1553d882ec");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        InitControl();//初始化控件
        setDefaultFragment();//设置初始fragment
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        pref = getSharedPreferences("userBabyInfo", MODE_PRIVATE);

        imgUrl = pref.getString("userBabyHeadUrl", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 判断是否登录并显示
        judgeLogin();
    }

    private void judgeLogin() {
        // 获得SharedPreferences对象
        pref = getSharedPreferences("userBabyInfo", MODE_PRIVATE);
        userBabyName = pref.getString("userBabyName", "");
        userParentEmail = pref.getString("userParentEmail", "");    //获取保存下的登录邮箱
        babyAge = pref.getInt("userBabyAge", 0);
        babySex = pref.getString("userBabySex", "");
        if (!userBabyName.equals("")) {
            tvUserBabyName.setText(userBabyName);
            tvBabySex.setText("性别:" + babySex);
            tvBabyAge.setText("年龄:" + babyAge);
            ivCleanUserInfo.setVisibility(View.VISIBLE);    //退出登录按钮设置可见
            if (!imgUrl.equals("")) {     //不为空，则有图片信息
                String newStr = imgUrl.replaceFirst("bmob-cdn-10503.b0.upaiyun.com", "bmob.dustray.cn");
                Glide.with(MainActivity.this).load(newStr).into(ivUserBabyHead);   //显示头像
            }
        }
    }

    /**
     * 设置默认fragment为EntertainmentFragment
     */
    private void setDefaultFragment() {
        //FragmentManager管理类
        FragmentManager fm = getSupportFragmentManager();
        //事务
        FragmentTransaction transaction = fm.beginTransaction();  //新建事务并初始化
        entertainmentFragment = new EntertainmentFragment();
        transaction.replace(R.id.frag_main_content, entertainmentFragment);  //销毁以前的Fragment替换回默认的

        transaction.commit(); //提交事务
    }

    /**
     * 控件初始化
     */
    private void InitControl() {
        acibEntertainment = (AppCompatImageButton) findViewById(R.id.acib_entertainment);
        acibDynamic = (AppCompatImageButton) findViewById(R.id.acib_dynamic);
        acibIntelligence = (AppCompatImageButton) findViewById(R.id.acib_intelligence);

        View view = navigationView.getHeaderView(0);
        tvUserBabyName = (TextView) view.findViewById(R.id.tvUserBabyName);
        ivUserBabyHead = (ImageView) view.findViewById(R.id.ivUserBabyHead);
        ivCleanUserInfo = (ImageView) view.findViewById(R.id.ivCleanUserInfo);
        tvBabySex = (TextView) view.findViewById(R.id.tvBabySex);
        tvBabyAge = (TextView) view.findViewById(R.id.tvBabyAge);

        acibEntertainment.setOnClickListener(this);
        acibDynamic.setOnClickListener(this);
        acibIntelligence.setOnClickListener(this);
        tvUserBabyName.setOnClickListener(this);
        ivUserBabyHead.setOnClickListener(this);
        ivCleanUserInfo.setOnClickListener(this);
    }

    /**
     * 重写硬件返回键事件
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 右上角菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 右上角菜单内项目选择事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ab_search_music) {
            Intent intent = new Intent(this, MusicActivity_.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑菜单项目选择事件
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_babyGrowLine) {
            if (userBabyName.equals("")) {        //如果没有登录，则不能点开圈子
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, GrowLineActivity.class);
                startActivity(intent);
            }
        } else {
            if (id == R.id.nav_babyFood) {    //饮食
                Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ArticleClass", "1");
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (id == R.id.nav_babyHealthy) {    //健康
                Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ArticleClass", "2");
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (id == R.id.nav_babyLife) {    //生活
                Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ArticleClass", "3");
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (id == R.id.nav_babyEarlyTeach) {     //早教
                Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ArticleClass", "4");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * fragment接口自动生成方法
     *
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * 页面内点击事件
     *
     * @param
     */
    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.acib_entertainment:
                if (entertainmentFragment == null)
                    entertainmentFragment = new EntertainmentFragment();
                transaction.replace(R.id.frag_main_content, entertainmentFragment);
                break;
            case R.id.acib_dynamic:
                if (userBabyName.equals("")) {        //如果没有登录，则不能点开圈子
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (dynamicFragment == null)
                        dynamicFragment = new DynamicFragment();
                    transaction.replace(R.id.frag_main_content, dynamicFragment);
                }
                break;
            case R.id.acib_intelligence:
                if (intelligenceFragment == null)
                    intelligenceFragment = new IntelligenceFragment();
                transaction.replace(R.id.frag_main_content, intelligenceFragment);
                break;
            case R.id.tvUserBabyName:       //点击用户昵称
                openBabyName();
                break;
            case R.id.ivUserBabyHead:       //点击用户头像
                openBabyPicture();
                break;
            case R.id.ivCleanUserInfo:     //退出登录
                cleanInfo();
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        transaction.commit();
    }

    //清空信息
    private void cleanInfo() {
        editor = pref.edit();
        editor.putString("userBabyHeadUrl", null);
        editor.putString("userBabyName", null);
        editor.putString("userBabySex", null);
        editor.putString("userParentEmail", null);
        editor.putString("userParentPass", null);
        editor.putInt("userBabyAge", 0);
        editor.commit();
    }

    private void openBabyPicture() {
        if (userBabyName != "") {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");// 图片
            startActivityForResult(intent, 1); // 跳转，传递打开相册请求码
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    //昵称的点击事件，如果已登录，就显示真正的昵称，未登录，则可以点击登录
    public void openBabyName() {
        if (userBabyName != "") {
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    // 将获取到的uri转换为String型
                    String[] images = {MediaStore.Images.Media.DATA};// 将图片URI转换成存储路径
                    Cursor cursor = this
                            .managedQuery(uri, images, null, null, null);
                    int index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_url = cursor.getString(index);

                    imgUrl = img_url;      //保存为全局变量
                    //显示图片
                    //showPicture(img_url);
                    //根据邮箱查询用户登陆信息
                    getInfoByEmail(img_url);

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getInfoByEmail(final String img_url) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("userParentEmail", userParentEmail);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (User u : list) {
                            objectId = u.getObjectId();   //获取登录的id
                        }
                    }
                    //图片上传
                    uplodePicture(img_url);
                } else {
                    Toast.makeText(MainActivity.this, "设置失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uplodePicture(String img_url) {
        final BmobFile icon = new BmobFile(new File(img_url));
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    user = new User();
                    // Toast.makeText(MainActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                    user.setUserBabyHead(icon);
                    updatePicture();     //修改
                } else {
                    Toast.makeText(MainActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePicture() {
        user.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "头像修改成功", Toast.LENGTH_SHORT).show();
                    //重新保存当前用户更改的信息
                    saveUpdatePicture();
                } else {
                    Toast.makeText(MainActivity.this, "头像修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //重新保存当前用户更改的信息
    private void saveUpdatePicture() {
        // ivUserBabyHead.setImageBitmap(BitmapFactory.decodeFile(imgUrl));

        editor = pref.edit();
        editor.putString("userBabyHeadUrl", imgUrl);   //保存图片url
        editor.commit();   //提交
       // Toast.makeText(MainActivity.this, "xxx" + imgUrl, Toast.LENGTH_SHORT).show();
        if (!imgUrl.equals("")) {     //不为空，则有图片信息
            String newStr = imgUrl.replaceFirst("bmob-cdn-10503.b0.upaiyun.com", "bmob.dustray.cn");
            Glide.with(MainActivity.this).load(newStr).into(ivUserBabyHead);   //显示头像
        }
    }

    //显示图片
    private void showPicture(String imgpath) {
        ivUserBabyHead.setImageBitmap(BitmapFactory.decodeFile(imgpath));
    }
}
