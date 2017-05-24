package com.example.weixu.accompanyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VideoWebActivity extends AppCompatActivity {
    private Intent intent;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_web);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.web_view);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        intent = this.getIntent();// 获取Intent对象
        Bundle bundle = intent.getExtras();// Bundle存取数据
        String url = bundle.getString("_weblink");

        String temp = url.substring(0, 4);
        if (!temp.equals("http")) {
            url = "http://" + url;
        }
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
