package com.example.generalstorehelper.ManageItems;

import androidx.lifecycle.ViewModel;
//VIEW-MODEL CLASS SERVING THE PURPOSE OF SAVING TEXT WRITTEN IN SEARCH WHEN SCREEN IS ROTATED OR SO
public class SearchAndEditViewModel extends ViewModel {
    private String search="";

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }
}
