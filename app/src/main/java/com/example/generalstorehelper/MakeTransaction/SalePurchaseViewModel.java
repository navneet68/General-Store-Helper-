package com.example.generalstorehelper.MakeTransaction;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.generalstorehelper.model.Item;
import com.example.generalstorehelper.model.ItemForTransaction;

import java.util.ArrayList;
import java.util.List;

public class SalePurchaseViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ItemForTransaction>> Transaction= new MutableLiveData<>(new ArrayList<>());

    public int choice = 0;

    public MutableLiveData<ArrayList<ItemForTransaction>> getTransaction() {
        return Transaction;
    }

    private ItemForTransaction itemForTransaction;

    public ItemForTransaction getItemForTransaction() {
        return itemForTransaction;
    }

    public void setItemForTransaction(ItemForTransaction _itemForTransaction) {
        ArrayList<ItemForTransaction> temp ;
        if(choice==1){
            temp = Transaction.getValue();
            assert temp != null;
            temp.add(_itemForTransaction);
            Transaction.setValue(temp);
        }
        if(choice==2){
            temp = Transaction.getValue();
            assert temp != null;
            int i=temp.indexOf(itemForTransaction);
            temp.remove(i);
            if(_itemForTransaction!=null)
            temp.add(i,_itemForTransaction);
            Transaction.setValue(temp);
        }
        itemForTransaction=null;
        if(choice==0)
        itemForTransaction = _itemForTransaction;


    }

    private final MutableLiveData<String> barcodeToSearch = new MutableLiveData<>();

    public MutableLiveData<String> getBarcodeToSearch() {
        return barcodeToSearch;
    }

    private final MutableLiveData<String> nameToSearch = new MutableLiveData<>("");

    public MutableLiveData<String> getNameToSearch() {
        return nameToSearch;
    }

    private final MutableLiveData<List<Item>> SearchResult = new MutableLiveData<>();

    public MutableLiveData<List<Item>> getSearchResult() {
        return SearchResult;
    }

}
