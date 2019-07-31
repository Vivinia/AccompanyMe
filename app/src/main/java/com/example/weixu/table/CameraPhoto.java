package com.example.weixu.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class CameraPhoto extends BmobObject {
    private String userEmail;
    private String cameraClass;
    private BmobFile userPhoto;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCameraClass() {
        return cameraClass;
    }

    public void setCameraClass(String cameraClass) {
        this.cameraClass = cameraClass;
    }

    public BmobFile getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(BmobFile userPhoto) {
        this.userPhoto = userPhoto;
    }
}
