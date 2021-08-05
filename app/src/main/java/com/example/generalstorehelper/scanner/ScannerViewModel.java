package com.example.generalstorehelper.scanner;


import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
//To save my scanned and stored barcodes , on rotating the activity
public class ScannerViewModel extends ViewModel {
    private String text="";
    private ArrayList<String> Barcodes;
    public ArrayList<String> getBarcodes(){
        if (Barcodes == null){
            Barcodes = new ArrayList<>();
        }
        return Barcodes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addBarcode(String newBarcode){
        if (Barcodes == null){
            Barcodes = new ArrayList<>();
        }
        Barcodes.add(newBarcode);
    }

}
