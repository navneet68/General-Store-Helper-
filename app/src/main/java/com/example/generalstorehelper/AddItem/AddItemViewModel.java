package com.example.generalstorehelper.AddItem;


import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
//To store and manage data of Add item Activity
public class AddItemViewModel extends ViewModel {
    String name="";
    String quantity="";
    String SP="";
    String CP="";
    String inStore="";
    String unit = "Kg";
    ArrayList<String> Barcodes = new ArrayList<>();

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSP() {
        return SP;
    }

    public void setSP(String SP) {
        this.SP = SP;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getInStore() {
        return inStore;
    }

    public void setInStore(String inStore) {
        this.inStore = inStore;
    }

    public void addBarcodes(ArrayList<String> barcodes) {
        ArrayList<String> x = new ArrayList<>();
        if (Barcodes != null)
            x.addAll(Barcodes);
        if (barcodes != null)
            x.addAll(barcodes);
        Barcodes = x;
    }

    public String sizeInString() {
        return Barcodes.size() + " Barcodes Added";
    }

    public void clearAll(){
        name="";
        quantity="";
        SP="";
        CP="";
        inStore="";
        unit = "Kg";
        Barcodes = new ArrayList<>();
    }

}
