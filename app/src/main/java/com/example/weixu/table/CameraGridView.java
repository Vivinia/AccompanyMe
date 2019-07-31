package com.example.weixu.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class CameraGridView extends BmobObject{
    private BmobFile cameraPhoto;
    private String cameraName;
    private String cameraClass;
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BmobFile getCameraPhoto() {
        return cameraPhoto;
    }

    public void setCameraPhoto(BmobFile cameraPhoto) {
        this.cameraPhoto = cameraPhoto;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraClass() {
        return cameraClass;
    }

    public void setCameraClass(String cameraClass) {
        this.cameraClass = cameraClass;
    }
}
