package com.example.generalstorehelper.Repository;

import android.content.Context;

import com.example.generalstorehelper.model.DAO;
import com.example.generalstorehelper.model.DataBase;

//ONE PLACE TO ACCESS ALL THE INFORMATION, YOU JUST NEED TO INITIALIZE REPO OBJECT ONCE IN MAIN ACTIVITY
public class Repo {

    private static DAO Dao;

    public Repo(Context appContext) {
        if(Dao==null){
            Dao=DataBase.getInstance(appContext).DAO();
        }
            
    }
    public static DAO getDao(){
        return Dao;
    }

}
