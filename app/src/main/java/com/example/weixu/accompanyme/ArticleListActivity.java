package com.example.weixu.accompanyme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.weixu.adpter.ArticleAdapter;
import com.example.weixu.adpter.VideoLinkAdapter;
import com.example.weixu.table.BabyArticle;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ArticleListActivity extends AppCompatActivity {

    private String ArticleClass;
    private RecyclerView rvArticle;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bmob.initialize(ArticleListActivity.this, "96556b6d6dbe89f2ff4a7c1553d882ec");
        Init();
        getData();
    }

    private void Init() {
        rvArticle = findViewById(R.id.rvArticle);
    }

    private void getData() {
        Bundle bundle = this.getIntent().getExtras();
        ArticleClass = bundle.getString("ArticleClass", "");
        BmobQuery<BabyArticle> query = new BmobQuery<>();
        query.addWhereEqualTo("type", ArticleClass);    //设置文章分类
        query.findObjects(new FindListener<BabyArticle>() {
            @Override
            public void done(List<BabyArticle> list, BmobException e) {
                if (e == null) {
                    for (BabyArticle d : list) {
                        String newStr = d.getArticlePicture().getUrl().replaceFirst("bmob-cdn-10503.b0.upaiyun.com", "bmob.dustray.cn");
                        //newStr+="!fnfx/300x300";
                        d.getArticlePicture().setUrl(newStr);
                    }
                    mLayoutManager = new LinearLayoutManager(ArticleListActivity.this, LinearLayoutManager.VERTICAL, false);  //设置布局管理器
                    mAdapter = new ArticleAdapter(ArticleListActivity.this, list, ArticleClass);
                    rvArticle.setLayoutManager(mLayoutManager);   //设置布局管理器
                    rvArticle.setAdapter(mAdapter);
                }else{
                    Toast.makeText(ArticleListActivity.this,"s"+e.toString(),Toast.LENGTH_LONG).show();
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
