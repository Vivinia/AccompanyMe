package com.example.weixu.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.accompanyme.PhotoListActivity;
import com.example.weixu.accompanyme.R;
import com.example.weixu.table.CameraGridView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class CameraNameAdapter extends BaseAdapter {
    private Context context;
    List<CameraGridView> list;
    private LayoutInflater layoutInflater;

    public CameraNameAdapter(Context context, List<CameraGridView> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.cameraname_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.cn_image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotoListActivity.class);
                    intent.putExtra("CameraClass",list.get(position).getCameraClass());
                    context.startActivity(intent);
                }
            });
            holder.text = (TextView) convertView.findViewById(R.id.cn_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //BmobFile bFile = list.get(position).getCameraPhoto();
        //Toast.makeText(context,"ssssss"+bFile.getFileUrl(),Toast.LENGTH_LONG).show();
       // if (bFile != null) {
            holder.text.setText(list.get(position).getCameraName());
     //       Glide.with(context).load(bFile.getFileUrl()).into(holder.image);
      //  }
        holder.image.setImageResource(R.drawable.photo_image);
        return convertView;
    }

    class ViewHolder {
        TextView text;
        ImageView image;
    }
}
