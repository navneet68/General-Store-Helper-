package com.example.generalstorehelper.ManageItems.RecyclerView;

import com.example.generalstorehelper.model.Item;
//Interface to perform actions in Search and Edit Activity when an item is clicked in Recycler View
public interface onItemViewClick {
    void startFragment(Item item);
}
