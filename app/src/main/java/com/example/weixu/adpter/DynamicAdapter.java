package com.example.weixu.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.accompanyme.FullImageActivity;
import com.example.weixu.accompanyme.R;
import com.example.weixu.table.Dynamic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by weixu on 2017/4/12.
 */

public class DynamicAdapter extends ArrayAdapter<Dynamic> {
    private int resourceId;
    private Context con;
    private ViewHolder viewHolder; // 实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率

    public DynamicAdapter(@NonNull Context context, @LayoutRes int resource, List<Dynamic> dynamicList) {
        super(context, resource,dynamicList);
        con = context;
        resourceId = resource; // 获取子布局
    }

    private Handler handler = new Handler() {};

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId, null); // convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建得到子布局，非固定的，和子布局id有关
            viewHolder.tvDynamicBabyName = (TextView) view
                    .findViewById(R.id.tvDynamicBabyName); // 获取控件,只需要调用一遍，调用过后保存在ViewHolder中
            viewHolder.tvDynamicContent = (TextView) view
                    .findViewById(R.id.tvDynamicContent);
            viewHolder.tvDynamicDate = (TextView) view
                    .findViewById(R.id.tvDynamicDate);
            viewHolder.ivShowDynamicPicture = (ImageView) view
                    .findViewById(R.id.ivShowDynamicPicture);
            view.setTag(viewHolder);
        } else { // convertView不为空代表布局被加载过，只需要将convertView的值取出即可
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
         final Dynamic dynamic=getItem(position);
        viewHolder.tvDynamicBabyName.setText(dynamic.getDynamicUserName());
        viewHolder.tvDynamicContent.setText(dynamic.getDynamicContent());
        viewHolder.tvDynamicDate.setText(dynamic.getCreatedAt());

        Glide.with(con).load(dynamic.getDynamicPicture().getUrl()).into(viewHolder.ivShowDynamicPicture);
        viewHolder.ivShowDynamicPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, FullImageActivity.class);
                intent.putExtra("imageUrl",dynamic.getDynamicPicture().getUrl());
                con.startActivity(intent);
            }
        });

        return view;
    }
}

class ViewHolder { // 当布局加载过后，保存获取到的控件信息
    TextView tvDynamicBabyName;// 宝宝昵称
    TextView tvDynamicContent;// 动态内容
    TextView tvDynamicDate;// 动态时间
    ImageView ivShowDynamicPicture;// 动态配图
}
