package com.example.user.mealmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CookNamesRes {

    @SerializedName("data")
    @Expose
    private List<CookNamesResInn> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<CookNamesResInn> getData() {
        return data;
    }

    public void setData(List<CookNamesResInn> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
