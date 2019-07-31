package com.example.weixu.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.accompanyme.PictureActivity;
import com.example.weixu.accompanyme.R;
import com.example.weixu.table.VideoLink;
import com.example.weixu.table.VideoPicture;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyPictureListAdapter extends RecyclerView.Adapter<MyPictureListAdapter.ViewHolder> {
    private ArrayList<String> mPictureClass;
    private ArrayList<String> mPictureNumber;
    Context context;

    public MyPictureListAdapter(ArrayList<String> mPictureClass, ArrayList<String> mPictureNumber, Context context) {
        this.mPictureClass = mPictureClass;
        this.mPictureNumber = mPictureNumber;
        this.context = context;
    }

    @NonNull
    @Override
    public MyPictureListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picturelist_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Bmob.initialize(context, "96556b6d6dbe89f2ff4a7c1553d882ec");
        //final ArrayList<VideoPicture> l=new ArrayList<VideoPicture>();
        BmobQuery<VideoPicture> q = new BmobQuery<VideoPicture>();
        q.findObjects(new FindListener<VideoPicture>() {
            @Override
            public void done(List<VideoPicture> list, BmobException e) {
                String newStr = list.get(i * 3).getPicture().getUrl().replaceFirst("bmob-cdn-10503.b0.upaiyun.com", "bmob.dustray.cn");
                //newStr+="!fnfx/300x300";

                Glide.with(context).load(newStr).into(viewHolder.ivPicture);   //显示图片
            }
        });
        viewHolder.tvClass.setText(mPictureClass.get(i));
        viewHolder.tvNumber.setText(mPictureNumber.get(i));
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("class", i + "");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPictureClass.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;
        ImageView ivPicture;
        TextView tvClass, tvNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvClass = itemView.findViewById(R.id.tvClass);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            itemLayout = itemView.findViewById(R.id.layout);
        }
    }
}
