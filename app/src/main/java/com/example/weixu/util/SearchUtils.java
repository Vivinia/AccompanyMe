package com.example.weixu.util;

import com.example.weixu.listener.OnLoadSearchFinishListener;
import com.example.weixu.table.MusicEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixu on 2017/4/16.
 */

public class SearchUtils {
    /** 搜索关键字地址 */
    public static String KEY_SEARCH_URL = "https://www.xiami.com/search/search?key=";
    /** ID接口地址 */
    public static String ID_SEARCH_URL = "https://www.xiami.com/song/playlist/id/";

    /**
     * 抓取歌曲id
     *
     * @param
     * @param listener
     *            完成监听
     */
    public static void getIds(String input, OnLoadSearchFinishListener listener) {
        List<String> allIds = new ArrayList<String>();
        String key = deCondeKey(input);// 解析用户输入关键字为 UTF-8
        Document document = null;
        try {
            document = Jsoup.connect(KEY_SEARCH_URL + key).get();// jsoup连接最终拼接而成的请求字符串
            Elements elements = document.getElementsByClass("track_list");// 选择类标签
            if (elements.size() != 0) {
                Elements all = elements.get(0).getElementsByClass("chkbox");
                int size = all.size();
                for (int i = 0; i < size; i++) {
                    String id = all.get(i).select("input").attr("value");
                    if (!StringUtils.isEmpty(id)) {
                        allIds.add(id);// 不为空的话加入id list中，便于初次抓取完以后统一请求
                    }
                }
                if (listener != null) {
                    if (allIds.size() == 0) {
                        listener.onLoadFiler();// id list大小为0 说明没有获取到数据，抓取失败
                    } else {
                        // 统一请求id接口地址进行再次抓取
                        listener.onLoadSucess(getOnlineSearchList(allIds));
                    }
                }
            }

        } catch (IOException e) {
            listener.onLoadFiler();
            e.printStackTrace();
        }
    }

    /**
     * 根据id 获取歌曲数据
     *
     * @param ids
     *            封装id 的list
     * @return 封装好的list<music> 用于listview展示
     */
    private static List<MusicEntity> getOnlineSearchList(List<String> ids) {
        List<MusicEntity> musicList = new ArrayList<MusicEntity>();
        int idSize = ids.size();
        for (int i = 0; i < idSize; i++) {
            String postUrl = ID_SEARCH_URL + ids.get(i);
            try {
                Document d = Jsoup.connect(postUrl).get();// 连接相应ID的接口地址
                Elements element = d.select("data");
                for (Element e : element) {
                    MusicEntity music = new MusicEntity();
                    music.setMusicId(ids.get(i));
                    music.setMusciName(getSubString(e.select("title").text()));
                    music.setAirtistName(e.select("artist").text());
                    music.setSmallAlumUrl(e.select("pic").text());
                    music.setBigAlumUrl(e.select("album_pic").text());
                    music.setLrcUrl(e.select("lyric").text());
                    music.setAlbumName(e.select("album_name").text());
                    // 对加密过后的歌曲在线地址进行解密
                    music.setPath(StringUtils.decodeMusicUrl(e.select(
                            "location").text()));
                    musicList.add(music);// 数据获取成功 封装入list
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return musicList;
    }

    /**
     * 解析歌名
     *
     * @return
     */
    private static String getSubString(String name) {
        int start = name.indexOf("[", 3) + 1;
        int end = name.indexOf("]");
        return name.substring(start, end);
    }

    private static String deCondeKey(String input) {
        try {
            String key = URLEncoder.encode(input, "UTF-8");
            return key;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

