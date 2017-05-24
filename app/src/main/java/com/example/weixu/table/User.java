package com.example.weixu.table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by weixu on 2017/4/2.
 */

public class User extends BmobObject{
    private String userParentEmail;  //父母邮箱
    private String userAreBaby;    //注册人是宝宝的谁
    private String userBabyName;  //宝宝名字
    private String userParentPass;  //父母登录密码
    private String userBabyBirthday;  //宝宝生日
    private String userBabySex;   //宝宝性别
    private BmobFile userBabyHead;  //宝宝头像
    private int userBabyAge;  //宝宝年龄

    public String getUserParentEmail() {
        return userParentEmail;
    }

    public void setUserParentEmail(String userParentEmail) {
        this.userParentEmail = userParentEmail;
    }

    public String getUserAreBaby() {
        return userAreBaby;
    }

    public void setUserAreBaby(String userAreBaby) {
        this.userAreBaby = userAreBaby;
    }

    public String getUserBabyName() {
        return userBabyName;
    }

    public void setUserBabyName(String userBabyName) {
        this.userBabyName = userBabyName;
    }

    public String getUserParentPass() {
        return userParentPass;
    }

    public void setUserParentPass(String userParentPass) {
        this.userParentPass = userParentPass;
    }

    public String getUserBabyBirthday() {
        return userBabyBirthday;
    }

    public void setUserBabyBirthday(String userBabyBirthday) {
        this.userBabyBirthday = userBabyBirthday;
    }

    public String getUserBabySex() {
        return userBabySex;
    }

    public void setUserBabySex(String userBabySex) {
        this.userBabySex = userBabySex;
    }

    public BmobFile getUserBabyHead() {
        return userBabyHead;
    }

    public void setUserBabyHead(BmobFile userBabyHead) {
        this.userBabyHead = userBabyHead;
    }

    public int getUserBabyAge() {
        return userBabyAge;
    }

    public void setUserBabyAge(int userBabyAge) {
        this.userBabyAge = userBabyAge;
    }
}
