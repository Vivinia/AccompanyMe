package com.example.weixu.music.response;

import java.util.List;

public class NeteaseCloudPlayerUrlResponse {

    public List<DataItem> data;

    public static class DataItem {
        public String url;
        public String type;
    }
}
