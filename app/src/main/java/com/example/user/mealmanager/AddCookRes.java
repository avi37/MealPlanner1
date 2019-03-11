package com.example.user.mealmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCookRes {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("u_id")
    @Expose
    private String u_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("status")
    @Expose
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
