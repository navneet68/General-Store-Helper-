package com.example.generalstorehelper.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {

    @Query("SELECT *,rowid FROM Items WHERE `Name/Description` LIKE  '%'||:name ||'%' ")
    List<Item> findByName(String name);

    @Query("SELECT *,rowid FROM Items WHERE `Barcodes` LIKE  '%'||:barcode ||'%' ")
    List<Item> findByBarcode(String barcode);

//Basic Queries
    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

}
