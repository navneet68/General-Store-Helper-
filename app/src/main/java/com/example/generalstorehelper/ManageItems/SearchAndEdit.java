package com.example.generalstorehelper.ManageItems;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.generalstorehelper.DialogueBoxes.DescriptionDialogueFragment;
import com.example.generalstorehelper.ManageItems.RecyclerView.AdapterRV;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.Repository.Repo;
import com.example.generalstorehelper.databinding.ActivitySearchAndEditBinding;
import com.example.generalstorehelper.model.Item;

import java.util.List;

public class SearchAndEdit extends AppCompatActivity{
    private ActivitySearchAndEditBinding binding;
    private SearchAndEditViewModel vModel;
    private AdapterRV adapterRV;

    //SETTING UP ACTIONS , WHEN ACTIVITY IS CREATED
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_search_and_edit);
        vModel= new ViewModelProvider(this).get(SearchAndEditViewModel.class);
        DescriptionDialogueFragment.instance= this::update;
        assignListener();
        setUI();



    }

    //Setting up UI with Data
    private void setUI(){

        //Defining adapterRV is tricky, and messy, i have given the list
        adapterRV = new AdapterRV(item -> {
            //Basically , this onItemViewClick Listener will open a custom Dialog-fragment when item is clicked
            if (!DescriptionDialogueFragment.isOpen()){
                FragmentManager fragmentManager = SearchAndEdit.this.getSupportFragmentManager();
                DescriptionDialogueFragment newFragment = new DescriptionDialogueFragment(item);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();
            }
        });

        binding.RV.setAdapter(adapterRV);
        binding.RV.setLayoutManager(new LinearLayoutManager(this));
        binding.search.setText(vModel.getSearch());
    }

    //Deciding what to do if text changed in Search bar, it will update the View accordingly.
    private void assignListener() {
        TextWatcher textWatcherForSearch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.setSearch(s.toString());
                update();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.search.addTextChangedListener(textWatcherForSearch);
    }

    //Refresh the Recycler List
    private void update() {
        List<Item> oldList = adapterRV.getListToShow();
        MutableLiveData<List<Item>> _newList = new MutableLiveData<>();

        AsyncTask.execute(() -> _newList.postValue(Repo.getDao().findByName(vModel.getSearch())));
        _newList.observe(this, newList -> {
            adapterRV.setListToShow(newList);
            DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return newList.get(newItemPosition).isSameAs(oldList.get(oldItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return newList.get(newItemPosition).isSameAs(oldList.get(oldItemPosition));
                }
            })
                    .dispatchUpdatesTo(adapterRV);
        });




    }

}