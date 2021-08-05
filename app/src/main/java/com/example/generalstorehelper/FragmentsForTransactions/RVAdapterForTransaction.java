package com.example.generalstorehelper.FragmentsForTransactions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.generalstorehelper.R;
import com.example.generalstorehelper.model.Item;
import com.example.generalstorehelper.model.ItemForTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterForTransaction extends RecyclerView.Adapter<RVAdapterForTransaction.ViewHolder> {
    List<ItemForTransaction> listToShow = new ArrayList<>();
    public OnEntryViewClick instance;

    public RVAdapterForTransaction(OnEntryViewClick instance) {
        this.instance = instance;
    }

    public void setListToShow(List<ItemForTransaction> listToShow) {
        this.listToShow = listToShow;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item_view_list, parent, false);

        return new RVAdapterForTransaction.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ItemForTransaction entry = listToShow.get(position);
        Item item = entry.getItem();
        String n = item.getName()+"\n"+"1 Count = "+item.getQuantity().toString()+ " "+item.getUnit();
        holder.getN().setText(n);
        String r = item.getSP().toString()+" Rs";
        holder.getR().setText(r);
        double count= entry.getQty()/item.getQuantity();
        String q = count +"\n"+"("+entry.getQty()+" "+item.getUnit()+")";
        holder.getQ().setText(q);
        String p = entry.getPrice().toString();
        holder.getP().setText(p);
        holder.itemView.setOnClickListener(v -> instance.startFragment(entry));


    }

    @Override
    public int getItemCount() {
        return listToShow.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView n,r,q,p;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            n=itemView.findViewById(R.id.n);
            r=itemView.findViewById(R.id.r);
            q=itemView.findViewById(R.id.q);
            p=itemView.findViewById(R.id.p);

        }

        public TextView getN() {
            return n;
        }

        public TextView getR() {
            return r;
        }

        public TextView getQ() {
            return q;
        }

        public TextView getP() {
            return p;
        }
    }
}
