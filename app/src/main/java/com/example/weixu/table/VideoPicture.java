package com.example.weixu.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class VideoPicture extends BmobObject { private BmobFile PictureVideo;
    private BmobFile Picture;

    public BmobFile getPicture() {
        return Picture;
    }

    public void setPicture(BmobFile picture) {
        Picture = picture;
    }

    public BmobFile getPictureVideo() {
        return PictureVideo;
    }

    public void setPictureVideo(BmobFile pictureVideo) {
        PictureVideo = pictureVideo;
    }
}
