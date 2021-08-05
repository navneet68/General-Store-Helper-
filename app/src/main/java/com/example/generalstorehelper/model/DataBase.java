package com.example.generalstorehelper.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
//DEFINING ESSENTIAL DAO(ABSTRACT) , AND GET-INSTANCE METHOD IMPLEMENTING SINGLETON CLASS FEATURE
    public abstract DAO DAO();
    private static DataBase instance;
    public static DataBase getInstance(Context AppContext) {
        if (instance == null) {
            instance = Room.databaseBuilder(AppContext,
                    DataBase.class, "Items in The Shop").build();
        }
        return instance;
    }
}
