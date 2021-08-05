package com.example.generalstorehelper.ManageItems.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.generalstorehelper.R;
import com.example.generalstorehelper.model.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterRV extends RecyclerView.Adapter<AdapterRV.ViewHolder> {
    private List<Item> listToShow = new ArrayList<>();
    public final onItemViewClick instance;

    //CONSTRUCTOR
    public AdapterRV(onItemViewClick instance) {
        this.instance = instance;
    }

    //CREATING VIEW AND VIEW HOLDER
    @NonNull
    @NotNull
    @Override
    public AdapterRV.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_for_list, parent, false);

        return new ViewHolder(view);
    }

    //BINDING DATA TO VIEWS OF VIEW HOLDER
    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterRV.ViewHolder holder, int position) {
        Item item = listToShow.get(position);
        holder.getNameView().setText(item.getName());
        String q = item.getQuantity() + item.getUnit();
        holder.getQuantityView().setText(q);
        String p = item.getSP() + "Rs";
        holder.getPriceView().setText(p);
        holder.itemView.setOnClickListener(v -> instance.startFragment(item));
    }

    //GETTING A COUNT OF NUMBER OF ITEMS TO SHOW ON LIST
    @Override
    public int getItemCount() {
        return listToShow.size();
    }

    //FOR SETTING UP THE DATA TO BE SHOWN AND CHANGING IT ACCORDINGLY
    public void setListToShow(List<Item> listToShow) {
        this.listToShow = listToShow;
    }

    public List<Item> getListToShow() {
        return listToShow;
    }

    //CHILD VIEW HOLDER CLASS
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //ITEM VIEWS - CHILD VIEWS
        private final TextView name;
        private final TextView quantity;
        private final TextView price;

        //CONSTRUCTOR
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_in_list);
            quantity = itemView.findViewById(R.id.quantity_listView);
            price = itemView.findViewById(R.id.price_list_View);
        }

        //METHODS USED TO RETURN CHILD VIEWS OF ITEM VIEW , TO BIND THEM WITH DATA IN ON_BIND_VIEW_HOLDER FUNCTION
        public TextView getNameView() {
            return name;
        }
        public TextView getQuantityView() {
            return quantity;
        }
        public TextView getPriceView() {
            return price;
        }
    }

}
