package com.example.generalstorehelper.FragmentsForTransactions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.generalstorehelper.MakeTransaction.SalePurchaseViewModel;
import com.example.generalstorehelper.ManageItems.RecyclerView.AdapterRV;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.model.Item;
import com.example.generalstorehelper.model.ItemForTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PortableItemsList extends Fragment {
    private AdapterRV adapterRV;
    private TextView noItems;
    SalePurchaseViewModel vModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.portable_search_result_list,container,false);
        RecyclerView RV = view.findViewById(R.id.RV_sale);
        noItems = view.findViewById(R.id.num_result_found);
        vModel = new ViewModelProvider(requireActivity()).get(SalePurchaseViewModel.class);
        adapterRV = new AdapterRV(item -> {
           if(!AddEditItemDetailsForSale.isOpen && !PortableScanner.isOpen){
               vModel.choice=0;
               vModel.setItemForTransaction(new ItemForTransaction(item));
               Fragment fragment = new AddEditItemDetailsForSale();
               FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
               transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
               transaction.add(android.R.id.content, fragment)
                       .setReorderingAllowed(true)
                       .addToBackStack(null).commit();
           }
        });
        RV.setAdapter(adapterRV);
        RV.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(vModel.getSearchResult().getValue()!=null)
        update(vModel.getSearchResult().getValue());
    }

    public void update(List<Item> listToShow) {
        List<Item> oldList = adapterRV.getListToShow();
        MutableLiveData<List<Item>> _newList = new MutableLiveData<>();

        AsyncTask.execute(() -> _newList.postValue(listToShow));
        _newList.observe(getViewLifecycleOwner(), newList -> {
            adapterRV.setListToShow(newList);
            DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return newList.get(newItemPosition).isSameAs(oldList.get(oldItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return newList.get(newItemPosition).isSameAs(oldList.get(oldItemPosition));
                }
            })
                    .dispatchUpdatesTo(adapterRV);
            String x = newList.size() + " Items Found";
            noItems.setText(x);
        });
    }

}
