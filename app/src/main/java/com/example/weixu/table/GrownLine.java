package com.example.weixu.table;

import cn.bmob.v3.BmobObject;

public class GrownLine extends BmobObject {
    private String userEmail;
    private int babyAge;
    private String babyWeight;
    private String babyTall;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getBabyAge() {
        return babyAge;
    }

    public void setBabyAge(int babyAge) {
        this.babyAge = babyAge;
    }

    public String getBabyWeight() {
        return babyWeight;
    }

    public void setBabyWeight(String babyWeight) {
        this.babyWeight = babyWeight;
    }

    public String getBabyTall() {
        return babyTall;
    }

    public void setBabyTall(String babyTall) {
        this.babyTall = babyTall;
    }
}
