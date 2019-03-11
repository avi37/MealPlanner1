package com.example.user.mealmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyRecipesRes {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("recipe_name")
    @Expose
    private String recipe_name;

    @SerializedName("food_type")
    @Expose
    private String food_type;

    @SerializedName("meal_items")
    @Expose
    private String meal_items;

    @SerializedName("fats")
    @Expose
    private String fats;

    @SerializedName("carbs")
    @Expose
    private String carbs;

    @SerializedName("proteins")
    @Expose
    private String proteins;

    @SerializedName("calories")
    @Expose
    private String calories;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("img_url")
    @Expose
    private String imgUrl;

    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;


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

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getMeal_items() {
        return meal_items;
    }

    public void setMeal_items(String meal_items) {
        this.meal_items = meal_items;
    }

}
