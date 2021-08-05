package com.example.generalstorehelper.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Fts4
@Entity(tableName = "Items")
public class Item implements Serializable{
//Initializing attributes of Item
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    public int id;

    @ColumnInfo(name = "Name/Description")
    String name;

    @ColumnInfo(name = "Mass/Volume")
    Double quantity;

    @ColumnInfo(name = "Unit")
    String unit;

    @ColumnInfo(name = "Selling Price")
    Double SP;

    @ColumnInfo(name = "Cost Price")
    Double  CP;

    @ColumnInfo(name = "Availability")
    Double inStore;

    @ColumnInfo(name = "Barcodes")
    String Barcodes;

    @ColumnInfo(name = "noBarcodes")
    int noBarcodes;

//Constructors Defined


    public Item(String name, Double quantity, String unit, Double SP, Double CP, Double inStore, String Barcodes, int noBarcodes) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.SP = SP;
        this.CP = CP;
        this.inStore = inStore;
        this.Barcodes = Barcodes;
        this.noBarcodes = noBarcodes;
    }

    //Getters and Setters
    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getSP() {
        return SP;
    }

    public void setSP(Double SP) {
        this.SP = SP;
    }

    public Double getCP() {
        return CP;
    }

    public void setCP(Double CP) {
        this.CP = CP;
    }

    public Double getInStore() {
        return inStore;
    }

    public void setInStore(Double inStore) {
        this.inStore = inStore;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBarcodes() {
        return Barcodes;
    }

    public void setBarcodes(String barcodes) {
        Barcodes = barcodes;
    }

    public int getNoBarcodes() {
        return noBarcodes;
    }

    public void setNoBarcodes(int noBarcodes) {
        this.noBarcodes = noBarcodes;
    }

    public boolean isSameAs(Item item){
        boolean isSame = true;
        if(this.id!=item.getId())
            isSame = false;
        if(isSame && !Objects.equals(this.name, item.getName()))
            isSame = false;
        if(isSame && !Objects.equals(this.quantity, item.getQuantity()))
            isSame = false;
        if(isSame && !Objects.equals(this.SP, item.getSP()))
            isSame = false;
        if(isSame && !Objects.equals(this.CP, item.getCP()))
            isSame = false;
        if(isSame && !Objects.equals(this.inStore, item.getInStore()))
            isSame = false;
        if(isSame && !Objects.equals(this.unit, item.getUnit()))
            isSame = false;
        if(isSame && !Objects.equals(this.Barcodes, item.getBarcodes()))
            isSame = false;


        return isSame;
    }

}
