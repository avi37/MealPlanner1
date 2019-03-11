package com.example.user.mealmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class CreatedMealAdapter extends RecyclerView.Adapter<CreatedMealAdapter.MyViewHolder> {

    private Context mContext;
    private List<CreatedMealModel> cardList;


    public CreatedMealAdapter(Context mContext, List<CreatedMealModel> cardList) {
        this.mContext = mContext;
        this.cardList = cardList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recipe_name;
        protected ListView listView_items;

        public MyViewHolder(View view) {
            super(view);
            recipe_name = view.findViewById(R.id.rep_meal_cardView_tv_recipeName);
            listView_items = view.findViewById(R.id.rep_meal_cardView_list_items);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.created_meal_item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CreatedMealModel createdMealModel = cardList.get(position);
        holder.recipe_name.setText(createdMealModel.getRecipeName());
        //holder.listView_items.setAdapter();


    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


}
