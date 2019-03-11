package com.example.user.mealmanager;

public class CreatedMealModel {

    private String recipeName;

    public CreatedMealModel(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

}
