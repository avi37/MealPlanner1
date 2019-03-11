package com.example.user.mealmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyRes {

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

    @SerializedName("dob")
    @Expose

    private String dob;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("new_pwd")
    @Expose
    private String new_pwd;

    @SerializedName("cook_type")
    @Expose
    private String cook_type;

    @SerializedName("meal_type")
    @Expose
    private String meal_type;

    @SerializedName("recipe_name")
    @Expose
    private String recipe_name;

    @SerializedName("meal_items")
    @Expose
    private String meal_items;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("m_id")
    @Expose
    private String m_id;

    @SerializedName("date")
    @Expose
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getMeal_items() {
        return meal_items;
    }

    public void setMeal_items(String meal_items) {
        this.meal_items = meal_items;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getCook_type() {
        return cook_type;
    }

    public void setCook_type(String cook_type) {
        this.cook_type = cook_type;
    }

    public String getNew_pwd() {
        return new_pwd;
    }

    public void setNew_pwd(String new_pwd) {
        this.new_pwd = new_pwd;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
