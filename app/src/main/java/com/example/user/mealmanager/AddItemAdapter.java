package com.example.user.mealmanager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AddItemAdapter extends ArrayAdapter<AddedItemModel> {

    Context context;
    int layoutResourceId;
    ArrayList<AddedItemModel> addedItemModels;


    public AddItemAdapter(@NonNull Context context, int layoutResourceId, @NonNull ArrayList<AddedItemModel> addedItemModels) {
        super(context, layoutResourceId, addedItemModels);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.addedItemModels = addedItemModels;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        ItemWrapper itemWrapper = null;

        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            itemWrapper = new ItemWrapper();
            itemWrapper.textView_item = item.findViewById(R.id.added_meal_item_tv_name);
            itemWrapper.imageButton_removeItem = item.findViewById(R.id.added_meal_item_ib_remove);
            item.setTag(itemWrapper);
        } else {
            itemWrapper = (ItemWrapper) item.getTag();
        }

        final AddedItemModel addedItemModel = addedItemModels.get(position);
        itemWrapper.textView_item.setText(addedItemModel.getItem_name());
        itemWrapper.imageButton_removeItem.setTag(position);
        itemWrapper.imageButton_removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();

                ((SelectMenuActivity) context).setRemoved_item_index(index);

                addedItemModels.remove(index);

                notifyDataSetChanged();

                ((SelectMenuActivity) context).clearRemovedItemData();
            }
        });

        return item;
    }

    static class ItemWrapper {
        TextView textView_item;
        ImageButton imageButton_removeItem;
    }

    public String[] getAllMealItems() {

        String[] new_list = new String[addedItemModels.size()];

        for (int i = 0; i < addedItemModels.size(); i++) {
            new_list[i] = addedItemModels.get(i).getItem_name();
        }

        return new_list;
    }

}
