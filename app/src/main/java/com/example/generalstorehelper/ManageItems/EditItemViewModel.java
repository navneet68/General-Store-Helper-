package com.example.generalstorehelper.ManageItems;

import androidx.lifecycle.ViewModel;

import com.example.generalstorehelper.model.Item;

import java.util.ArrayList;

public class EditItemViewModel extends ViewModel {
   Item item ;

    public void addBarcodes(ArrayList<String> barcodes) {
        StringBuilder x= new StringBuilder();
        for (int i = 0; i < barcodes.size(); i++) {
                x.append("/").append(barcodes.get(i)).append("/");
        }
        item.setBarcodes(item.getBarcodes()+x.toString());
        item.setNoBarcodes(item.getNoBarcodes()+barcodes.size());
    }

    public String sizeInString() {
        return item.getNoBarcodes() + " Barcodes Added";
    }
}
