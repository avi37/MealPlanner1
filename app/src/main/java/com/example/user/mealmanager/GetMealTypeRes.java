package com.example.user.mealmanager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMealTypeRes {

    @SerializedName("recipe_name")
    @Expose
    private String recipe_name;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("food_type")
    @Expose
    private String food_type;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("meal_items")
    @Expose
    private List<MealItemsRes> mealItems = null;

    @SerializedName("nop")
    @Expose

    private String nop;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("meal_type")
    @Expose
    private String meal_type;


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNop() {

        return nop;
    }

    public void setNop(String nop) {
        this.nop = nop;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MealItemsRes> getMealItems() {
        return mealItems;
    }

    public void setMealItems(List<MealItemsRes> mealItems) {
        this.mealItems = mealItems;
    }


}
