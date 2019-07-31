package com.example.weixu.accompanyme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AriticleActivity extends AppCompatActivity {

    private String articleUrl;
    private WebView wbShowArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariticle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getData();
        setWeb();
    }

    private void setWeb() {
        //支持javascript
        wbShowArticle.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        wbShowArticle.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wbShowArticle.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        wbShowArticle.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        wbShowArticle.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wbShowArticle.getSettings().setLoadWithOverviewMode(true);
        wbShowArticle.getSettings().setTextZoom(100);
        wbShowArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        wbShowArticle.loadUrl(articleUrl);
    }

    private void getData() {
        wbShowArticle=findViewById(R.id.wbShowArticle);
        Bundle bundle=this.getIntent().getExtras();
        articleUrl=bundle.getString("articleLink","");    //获取文章url
        articleUrl=articleUrl.replaceFirst("bmob-cdn-10503.b0.upaiyun.com","bmob.dustray.cn");
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
