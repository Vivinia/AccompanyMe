package com.example.weixu.accompanyme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.weixu.adpter.SearchResultAdapter;
import com.example.weixu.listener.OnLoadSearchFinishListener;
import com.example.weixu.table.MusicEntity;
import com.example.weixu.util.ImageUtils;
import com.example.weixu.util.SearchUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class SearchMusicActivity extends AppCompatActivity {
    private ListView lvSearchReasult;

    private List<MusicEntity> listSearchResult;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music);
        ImageUtils.initImageLoader(this);// ImageLoader初始化
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        listSearchResult = new ArrayList<MusicEntity>();
        dialog = new ProgressDialog(this);
        dialog.setTitle("加载中。。。");
        lvSearchReasult = (ListView) findViewById(R.id.lv_search_list);
        Button btSearch = (Button) findViewById(R.id.bt_online_search);
        final EditText edtKey = (EditText) findViewById(R.id.edt_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();// 进入加载状态，显示进度条
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        SearchUtils.getIds(edtKey.getText().toString(),
                                new OnLoadSearchFinishListener() {
                                    @Override
                                    public void onLoadSucess(
                                            List<MusicEntity> musicList) {
                                        dialog.dismiss();// 加载完成，取消进度条
                                        Message msg = new Message();
                                        msg.what = 0;
                                        mHandler.sendMessage(msg);
                                        listSearchResult = musicList;
                                    }

                                    @Override
                                    public void onLoadFiler() {
                                        dialog.dismiss();// 加载失败，取消进度条
                                    }
                                });

                    }
                }).start();
            }
        });
        lvSearchReasult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                MusicEntity music = listSearchResult.get(arg2);
                Intent i = new Intent(SearchMusicActivity.this,
                        MusicDetailActivity.class);
                i.putExtra("music", music);
                startActivity(i);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    SearchResultAdapter adapter = new SearchResultAdapter(
                            listSearchResult, SearchMusicActivity.this);
                    lvSearchReasult.setAdapter(adapter);
                    break;
            }
        }

        ;
    };

    public void goBack(View view) {
        finish();
    }

    @Override
    public void onDestroy() {
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
