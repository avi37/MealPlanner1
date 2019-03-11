package com.example.user.mealmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MealItemsRes {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("img_url")
    @Expose
    private String imgUrl;

    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;

    @SerializedName("proteins")
    @Expose
    private String proteins;

    @SerializedName("fats")
    @Expose
    private String fats;

    @SerializedName("carbs")
    @Expose
    private String carbs;

    @SerializedName("calories")
    @Expose
    private String calories;



    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getFats() {
        return fats;
    }

    public void setFats(String fats) {
        this.fats = fats;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

}
