package com.example.weixu.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weixu.accompanyme.R;
import com.example.weixu.table.MusicEntity;
import com.example.weixu.util.ImageUtils;

import java.util.List;

/**
 * Created by weixu on 2017/4/16.
 */

public class SearchResultAdapter  extends BaseAdapter {
        private List<MusicEntity> list;

        private Context context;

        public SearchResultAdapter(List<MusicEntity> list, Context c) {
            super();
            this.list = list;
            this.context = c;
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(context,
                        R.layout.item_online_search_list, null);
                holder.tvMusicName = (TextView) convertView
                        .findViewById(R.id.tv_search_list_title);
                holder.tvMusicAritist = (TextView) convertView
                        .findViewById(R.id.tv_search_list_airtist);
                holder.ivMusicImage = (ImageView) convertView
                        .findViewById(R.id.iv_search_list);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MusicEntity music = list.get(position);
            holder.tvMusicName.setText(music.getMusciName());
            holder.tvMusicAritist.setText(music.getAirtistName());
            ImageUtils.disPlay(music.getSmallAlumUrl(), holder.ivMusicImage,
                    R.drawable.ic_menu_gallery);
            return convertView;
        }

        class ViewHolder {
            TextView tvMusicName, tvMusicAritist;
            ImageView ivMusicImage;
        }

    }
