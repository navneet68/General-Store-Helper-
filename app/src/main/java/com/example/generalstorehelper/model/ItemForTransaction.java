package com.example.generalstorehelper.model;

public class ItemForTransaction {

    private Item item;

    private Double Qty;

    private Double Price;

    public ItemForTransaction(Item item) {
        this.item = item;
    }

    public ItemForTransaction(Item item, Double qty, Double price) {
        this.item = item;
        Qty = qty;
        Price = price;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getQty() {
        return Qty;
    }

    public Double getPrice() {
        return Price;
    }

}
