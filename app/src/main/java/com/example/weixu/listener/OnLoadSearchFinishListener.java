package com.example.weixu.listener;

import com.example.weixu.table.MusicEntity;

import java.util.List;

/**
 * Created by weixu on 2017/4/16.
 */

public interface OnLoadSearchFinishListener {
    void onLoadSucess(List<MusicEntity> musicList);

    void onLoadFiler();
}
