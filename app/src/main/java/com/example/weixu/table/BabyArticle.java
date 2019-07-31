package com.example.weixu.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class BabyArticle extends BmobObject {
    private String title;
    private String url;
    private String type;
    private String content;
    private BmobFile articlePicture;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getArticlePicture() {
        return articlePicture;
    }

    public void setArticlePicture(BmobFile articlePicture) {
        this.articlePicture = articlePicture;
    }
}
