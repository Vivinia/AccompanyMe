package com.example.weixu.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.accompanyme.R;
import com.example.weixu.table.CameraGridView;
import com.example.weixu.table.CameraPhoto;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class PhotoListAdapter extends BaseAdapter {
    private Context context;
    List<CameraPhoto> list;
    private LayoutInflater layoutInflater;
    private OnPhotoClickListener photoListener;

    public PhotoListAdapter(Context context, List<CameraPhoto> list, OnPhotoClickListener photoListener) {
        this.context = context;
        this.list = list;
        this.photoListener=photoListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.cameralist_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.cn_image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoListener.onClick(position);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BmobFile bFile = list.get(position).getUserPhoto();
       // Toast.makeText(context,"ssssss"+bFile.getFileUrl(),Toast.LENGTH_LONG).show();
        if (bFile != null) {
            Glide.with(context).load(bFile.getFileUrl()).into(holder.image);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }

    public interface OnPhotoClickListener{
        void onClick(int i);
    }
}
