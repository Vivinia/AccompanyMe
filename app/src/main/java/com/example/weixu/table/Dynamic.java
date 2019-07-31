package com.example.weixu.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by weixu on 2017/4/11.
 */

public class Dynamic extends BmobObject {
    private String dynamicUserName;
    private String dynamicContent;
    private String userEmail;
    private BmobFile dynamicPicture;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDynamicUserName() {
        return dynamicUserName;
    }

    public void setDynamicUserName(String dynamicUserName) {
        this.dynamicUserName = dynamicUserName;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public BmobFile getDynamicPicture() {
        return dynamicPicture;
    }

    public void setDynamicPicture(BmobFile dynamicPicture) {
        this.dynamicPicture = dynamicPicture;
    }
}
