package com.example.weixu.accompanyme;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weixu.music.DownloadReceiver;
import com.example.weixu.music.NeteaseCloudSongAdapter;
import com.example.weixu.music.SongItem;
import com.example.weixu.music.handler.ImageViewCallbackHandler;
import com.example.weixu.music.handler.SimpleCallbackHandler;
import com.example.weixu.music.request.NeteaseCloud;
import com.example.weixu.music.response.NeteaseCloudSearchSongResponse;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@EActivity(R.layout.activity_music)
public class MusicActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String LOG_TAG = "MusicActivity";
    private static final int REQUEST_CODE_DOWNLOAD = 1;

    @ViewById(R.id.search_result)
    protected RecyclerView searchResult;
    @ViewById(R.id.player_cover)
    protected ImageView player_cover;//播放器封面
    @ViewById(R.id.song_name)
    protected TextView songName;
    @ViewById(R.id.singer_name)
    protected TextView singerName;
    @ViewById(R.id.player_button)
    protected ImageView playButton;
    @ViewById(R.id.player_seek_bar)
    protected AppCompatSeekBar seekBar;
    @ViewById(R.id.progress_bar)
    protected ProgressBar progressBar;
    SearchView sv;

    public OkHttpClient httpClient;
    private MediaPlayer mediaPlayer;
    private boolean seekBarChanging = false;
    private Timer seekBarTimer;
    private SongItem currentPlaySongItem = null;
    private DownloadReceiver downloadReceiver = new DownloadReceiver();
    public String downloadPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        httpClient = new OkHttpClient.Builder().build();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        seekBarTimer = new Timer();
        //注册广播

        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //加载下载路径
        File defaultPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        downloadPath = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.preference_download_path_key), defaultPath.getAbsolutePath());
    }

    @AfterViews
    public void afterView() {
        searchResult.setLayoutManager(new LinearLayoutManager(this));
        searchResult.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!seekBarChanging && fromUser && seekBar.getMax() > 0) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarChanging = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarChanging = false;
            }
        });
        seekBarTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (seekBar.getMax() > 0 && !seekBarChanging && mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
         sv = (SearchView) searchMenuItem.getActionView();
        sv.setQueryHint(getString(R.string.activity_main_search_hint));
        sv.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
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

    /**
     * 处理用户搜索请求
     * @param query 用户的搜索
     * @return 是否处理成功
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        return searchNeteaseCloud(query);
    }
    protected boolean searchNeteaseCloud(String query) {
        Request req = new NeteaseCloud().searchSong(query, 1, 100);
        httpClient.newCall(req).enqueue(new SimpleCallbackHandler<NeteaseCloudSearchSongResponse>(MusicActivity.this) {
            @Override
            public void onResult(Call call, final NeteaseCloudSearchSongResponse response) {
                displaySearchResponse(new NeteaseCloudSongAdapter(response, MusicActivity.this));
            }
        });
        return true;
    }


    @UiThread
    protected void displaySearchResponse(RecyclerView.Adapter adapter) {
        searchResult.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        sv.clearFocus();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * 播放音乐
     * @param songItem 播放的歌曲
     */
    public void playMusic(SongItem songItem) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songItem.downloadUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mp.getDuration());
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.stop);
                }
            });
        } catch (IOException|NullPointerException e) {
            Toast.makeText(this, R.string.toast_player_url_error, Toast.LENGTH_SHORT).show();
        }
        currentPlaySongItem = songItem;
        songName.setText(songItem.name);
        singerName.setText(songItem.artist);
        if (songItem.albumImage == null) {
            player_cover.setVisibility(View.GONE);
        } else {
            player_cover.setVisibility(View.VISIBLE);
            httpClient.newCall(new Request.Builder().url(songItem.albumImage).build())
                    .enqueue(new ImageViewCallbackHandler(MusicActivity.this, player_cover));
        }
    }

    @Override
    protected void onPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }
        super.onDestroy();
    }

    @Click(R.id.player_button)
    public void onPlayButtonClick(ImageView v) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            v.setImageResource(R.drawable.play);
        } else if (seekBar.getMax() > 0) {
            mediaPlayer.start();
            v.setImageResource(R.drawable.stop);
        }
    }

    @Click(R.id.download_button)
    public void onDownloadButtonClick() {
        if (currentPlaySongItem == null || TextUtils.isEmpty(currentPlaySongItem.downloadUrl)) {
            return;
        }
        int hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            downloadMusic();
        } else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            boolean showRequest = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean isFirstRequest = sp.getBoolean("is_first_request_download", true);
            if (!isFirstRequest && !showRequest) {
                Toast.makeText(this, R.string.toast_permission_write_fail, Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_DOWNLOAD);
                sp.edit().putBoolean("is_first_request_download", false).apply();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_DOWNLOAD) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadMusic();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void downloadMusic() {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(currentPlaySongItem.downloadUrl));
        Log.i(LOG_TAG, "download path:" + downloadPath);
        req.setDestinationUri(Uri.fromFile(new File(new File(downloadPath), currentPlaySongItem.getFilename())));
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setAllowedOverMetered(false);
        req.allowScanningByMediaScanner();
        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            long downloadId = downloadManager.enqueue(req);
            downloadReceiver.enqueue(downloadId, currentPlaySongItem);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
