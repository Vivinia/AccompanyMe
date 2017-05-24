package com.example.weixu.accompanyme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.weixu.adpter.DynamicAdapter;
import com.example.weixu.helper.AnimationHelper;
import com.example.weixu.table.Dynamic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class DynamicFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // private FloatingActionButton fabInsertPicture, fabOpenCamera, fabOpenAlbum;  //添加图片
    private ImageView ivOpenInsertDynamic;
    private Intent intent;
    private SharedPreferences pref;
    private ListView lvDynamic;
    private String userName;
    private static int REQUEST_CAMERA = 1;
    private static int REQUEST_ALBUM = 2;
    private String mFilePath;
    private Bitmap bitmapCamera, bitmapAlbum;

    private boolean isOpenInsert = false;


    public DynamicFragment() {
    }


    public static DynamicFragment newInstance(String param1, String param2) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getActivity(), "96556b6d6dbe89f2ff4a7c1553d882ec");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        InitControl();
        super.onActivityCreated(savedInstanceState);
    }
    private void InitControl() {
        lvDynamic = (ListView) getView().findViewById(R.id.lvDynamic);
        ivOpenInsertDynamic = (ImageView) getView().findViewById(R.id.ivOpenInsertDynamic);
        ivOpenInsertDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateStart();
                Intent intent = new Intent(getActivity(), InsertDynamicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDate() {
        BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();   //查询Bmob数据库中信息
        query.order("-updatedAt");
        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> list, BmobException e) {
                try {
                    DynamicAdapter adapter = new DynamicAdapter(getActivity(), R.layout.dynamic_list_item, list);
                    lvDynamic.setAdapter(adapter);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDate();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dynamic, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        rotateStart();
    }

    public void rotateStart() {
        AnimationHelper ah = new AnimationHelper();
                /*Open*/
        Animation rotateAnimOpen = ah.getRotateAnimation(0, 360);  //实例化动画类
                /*Close*/
        Animation rotateAnimClose = ah.getRotateAnimation(0, -360);  //实例化动画类
                /**/
        //打开按钮动画监听事件：动画结束时更改控件实际位置
        rotateAnimOpen.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //清除动画，否则会闪动
                ivOpenInsertDynamic.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //关闭按钮动画监听事件：动画结束时更改控件实际位置
        rotateAnimClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //清除动画，否则会闪动
                ivOpenInsertDynamic.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (!isOpenInsert) {
            //动画开始
            ivOpenInsertDynamic.startAnimation(rotateAnimOpen);  //指定最下方的控件开始动画
            //控件显示
            isOpenInsert=true;
        } else {
            //动画开始
            ivOpenInsertDynamic.startAnimation(rotateAnimClose);  //指定最下方的控件开始动画
            //控件隐藏
            isOpenInsert=false;
        }
    }
}
