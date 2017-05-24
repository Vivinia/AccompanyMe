package com.example.weixu.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by weixu on 2017/4/6.
 */

public class VideoLink extends BmobObject {
    private String webVideoName;
    private String webVideoLink;
    private String webVideoInfoName;
    private BmobFile webPicture;

    public BmobFile getWebPicture() {
        return webPicture;
    }

    public void setWebPicture(BmobFile webPicture) {
        this.webPicture = webPicture;
    }

    public String getWebVideoName() {
        return webVideoName;
    }

    public void setWebVideoName(String webVideoName) {
        this.webVideoName = webVideoName;
    }

    public String getWebVideoLink() {
        return webVideoLink;
    }

    public void setWebVideoLink(String webVideoLink) {
        this.webVideoLink = webVideoLink;
    }

    public String getWebVideoInfoName() {
        return webVideoInfoName;
    }

    public void setWebVideoInfoName(String webVideoInfoName) {
        this.webVideoInfoName = webVideoInfoName;
    }
}
