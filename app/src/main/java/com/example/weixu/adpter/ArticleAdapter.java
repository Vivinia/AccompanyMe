package com.example.weixu.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weixu.accompanyme.AriticleActivity;
import com.example.weixu.accompanyme.ArticleListActivity;
import com.example.weixu.accompanyme.R;
import com.example.weixu.table.BabyArticle;
import com.example.weixu.table.Dynamic;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context context;
    private List<BabyArticle> mArticleTitleList;
    private String ArticleClass;

    public ArticleAdapter(Context context, List<BabyArticle> mArticleTitleList, String ArticleClass) {    //传递分类
        this.context = context;

        this.mArticleTitleList = mArticleTitleList;
        this.ArticleClass=ArticleClass;
    }

    @NonNull
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {
        Bmob.initialize(context, "96556b6d6dbe89f2ff4a7c1553d882ec");
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_list_item,viewGroup,false);
        final ArticleAdapter.ViewHolder viewHolder=new ArticleAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Glide.with(context).load(mArticleTitleList.get(i).getArticlePicture().getUrl()).into(viewHolder.ivArticlePicture);   //显示图片
        viewHolder.tvArticleTitle.setText(mArticleTitleList.get(i).getTitle());
        viewHolder.llArticleList.setOnClickListener(new View.OnClickListener() {      //点击事件
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AriticleActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("articleLink",mArticleTitleList.get(i).getUrl());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArticleTitleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout llArticleList;
        private ImageView ivArticlePicture;
        private TextView tvArticleTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llArticleList=itemView.findViewById(R.id.llArticleList);
            ivArticlePicture=itemView.findViewById(R.id.ivArticlePicture);
            tvArticleTitle=itemView.findViewById(R.id.tvArticleTitle);
        }
    }
}
